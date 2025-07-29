package org.study.hydrowarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydrowarehouse.entity.Dto.UserDto;
import org.study.hydrowarehouse.exception.AppRequestException;
import org.study.hydrowarehouse.exception.ReportException;
import org.study.hydrowarehouse.hateoas.HateoasLinkHelper;
import org.study.hydrowarehouse.hateoas.HypermediaListAssembler;
import org.study.hydrowarehouse.service.UserService;
import org.study.hydrowarehouse.utill.Pagination;
import org.study.hydrowarehouse.utill.ValidatorParam;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class {@link UserController} provides endpoints for accessing user data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class UserController implements HypermediaListAssembler<UserDto> {

    private UserService userService;

    private static final String USER_NOT_FOUND_MESSAGE = "The specified user was not found.";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Updates the current user's information.
     * <p>
     * Retrieves the user based on the authenticated principal's email,
     * updates the user data with the provided {@code UserDto}, and saves the changes.
     * If the user is not found, returns HTTP 404 with an error message.
     *
     * @param userDto   the new user data to update (without ID, which is set internally)
     * @param principal the security principal containing the authenticated user's details
     * @return {@link ResponseEntity} with HTTP status 200 OK if update is successful,
     *         or 404 NOT FOUND with an error message if the user does not exist
     */
    @PostMapping(value = PathPages.USER_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update (@RequestBody UserDto userDto, Principal principal) {
        return userService.findUserByEmail(principal.getName())
                .map(currentUser -> {
                    userDto.setUserDtoId(currentUser.getUserDtoId());
                    userService.update(userDto);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> {
                    Map<String, String> body = Collections.singletonMap(ControllerConstants.MESSAGE, USER_NOT_FOUND_MESSAGE);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                });
    }

    /**
     * Updates the status of a user by their ID.
     *
     * <p>This endpoint allows changing the user's status to a new value such as "ACTIVE", "BLOCKED", or "INACTIVE".
     * The status string must match a valid entry in the {@code user_statuses} table.
     *
     * @param status the new status to assign to the user (e.g., "ACTIVE", "BLOCKED", "INACTIVE")
     * @param userId the ID of the user whose status should be updated
     * @return {@code ResponseEntity} with HTTP 200 OK if the status was successfully updated
     */
    @GetMapping(value = PathPages.USER_UPDATE_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> updateStatus (@RequestParam(ControllerConstants.STATUS) String status,
                                                    @RequestParam(ControllerConstants.ID) Integer userId) {
        userService.changeStatus(userId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Updates the role of a user by their ID.
     * <p>
     * This method accepts the user's ID and the new role name as request parameters.
     * If the operation is successful, it returns HTTP 200 OK. If the user or role is invalid,
     * an exception will be thrown and handled appropriately.
     *
     * @param newRole the new role to assign to the user (e.g., "USER", "ADMIN", "CEO")
     * @param userId  the ID of the user whose role is to be updated
     * @return a {@link ResponseEntity} with HTTP status 200 OK if the role is successfully updated
     */
    @GetMapping(value = PathPages.USER_UPDATE_ROLE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> updateRole (@RequestParam(ControllerConstants.ROLE) String newRole,
                                                    @RequestParam(ControllerConstants.ID) Integer userId) {
        userService.changeRole(userId, newRole);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * The method gives user data by user's id.
     * @param id is the id of the user.
     * @return The object of the userDTO.
     */
    @GetMapping(value = PathPages.USER_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getUser(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return userService.findUserById(Integer.parseInt(id))
                .<ResponseEntity<?>>map(userDto -> ResponseEntity.ok(userDto))
                .orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, USER_NOT_FOUND_MESSAGE);
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(body);
                });
    }

    /**
     * Retrieves the profile information of the currently authenticated user.
     *
     * <p>This endpoint is accessible to authenticated users only and returns
     * user details based on the email extracted from the {@link Principal} object.</p>
     *
     * @param principal the security principal associated with the current authenticated user
     * @return a {@link ResponseEntity} containing the {@link UserDto} with the user's information
     */
    @GetMapping(value = PathPages.USER_CURRENT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        String email = principal.getName();
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    throw new AppRequestException(USER_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
                });
    }

    /**
     * The method returns all users data by page.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @return The object of the CollectionModel includes list of users.
     */
    @GetMapping(value = PathPages.USER_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UserDto> getUsers(@RequestParam(ControllerConstants.PAGE) String page,
                                             @RequestParam(ControllerConstants.SIZE) String size) {
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size);

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                UserController.class,
                PathPages.USER_ALL,
                searchCriteria);
        Link nextLink = HateoasLinkHelper.createNextLink(
                UserController.class,
                PathPages.USER_ALL,
                searchCriteria);

        List<UserDto> userList = userService.findAll(
                Pagination.getOffset(page, size),
                Integer.parseInt(size));
        List<UserDto> nextDataList = userService.findAll(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size));

        return createPaginatedModel(userList, nextDataList, previousLink, nextLink);
    }

    /**
     * Retrieves a paginated list of users filtered by their status.
     * <p>
     * The method supports pagination using page and size parameters, and includes HATEOAS links
     * for navigating to previous and next pages.
     *
     * @param page   the current page number as a String (starting from 0)
     * @param size   the number of records per page as a String
     * @param status the user status to filter by (e.g., "ACTIVE", "INACTIVE", "BLOCKED")
     * @return a {@link CollectionModel} containing a list of {@link UserDto} and pagination links
     */
    @GetMapping(value = PathPages.USER_FIND_BY_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UserDto> getUsersByStatus(@RequestParam(ControllerConstants.PAGE) String page,
                                                     @RequestParam(ControllerConstants.SIZE) String size,
                                                     @RequestParam(ControllerConstants.STATUS) String status) {
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.STATUS, status);

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                UserController.class,
                PathPages.USER_FIND_BY_STATUS,
                searchCriteria);
        Link nextLink = HateoasLinkHelper.createNextLink(
                UserController.class,
                PathPages.USER_FIND_BY_STATUS,
                searchCriteria);

        List<UserDto> userList = userService.findAllByStatus(
                status,
                Pagination.getOffset(page, size),
                Integer.parseInt(size));
        List<UserDto> nextDataList = userService.findAllByStatus(
                status,
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size));

        return createPaginatedModel(userList, nextDataList, previousLink, nextLink);
    }

    /**
     * Retrieves a paginated list of users belonging to the specified country.
     * <p>
     * The result is wrapped in a {@link CollectionModel} and includes HATEOAS
     * navigation links to the previous and next pages, if applicable.
     * </p>
     *
     * @param page      the current page number (1-based index)
     * @param size      the number of records to return per page
     * @param countryId the unique identifier of the country; must be a numeric value
     * @return a {@link CollectionModel} containing the list of {@link UserDto} objects
     *         for the requested page and country, along with pagination links
     * @throws AppRequestException if {@code countryId} is not a valid integer
     */
    @GetMapping(value = PathPages.USER_FIND_BY_COUNTRY, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UserDto> getUsersByCountry(@RequestParam(ControllerConstants.PAGE) String page,
                                                     @RequestParam(ControllerConstants.SIZE) String size,
                                                     @RequestParam(ControllerConstants.ID) String countryId) {
        ValidatorParam.isNumber(countryId);
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.ID, countryId);

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                UserController.class,
                PathPages.USER_FIND_BY_COUNTRY,
                searchCriteria);
        Link nextLink = HateoasLinkHelper.createNextLink(
                UserController.class,
                PathPages.USER_FIND_BY_COUNTRY,
                searchCriteria);

        List<UserDto> userList = userService.findAllByCountry(
                Integer.parseInt(countryId),
                Pagination.getOffset(page, size),
                Integer.parseInt(size));
        List<UserDto> nextDataList = userService.findAllByCountry(
                Integer.parseInt(countryId),
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size));

        return createPaginatedModel(userList, nextDataList, previousLink, nextLink);
    }

    /**
     * Retrieves a paginated list of users filtered by their role.
     * <p>
     * The method supports pagination using page and size parameters, and adds HATEOAS links
     * for navigating to previous and next pages.
     *
     * @param page the current page number as a String (starting from 0)
     * @param size the number of records per page as a String
     * @param role the role name to filter users by (e.g., "USER", "ADMIN", "CEO")
     * @return a {@link CollectionModel} containing a list of {@link UserDto} and pagination links
     */
    @GetMapping(value = PathPages.USER_FIND_BY_ROLE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UserDto> getUsersByRole(@RequestParam(ControllerConstants.PAGE) String page,
                                                     @RequestParam(ControllerConstants.SIZE) String size,
                                                     @RequestParam(ControllerConstants.ROLE) String role) {
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.ROLE, role);

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                UserController.class,
                PathPages.USER_FIND_BY_ROLE,
                searchCriteria);
        Link nextLink = HateoasLinkHelper.createNextLink(
                UserController.class,
                PathPages.USER_FIND_BY_ROLE,
                searchCriteria);

        List<UserDto> userList = userService.findAllByRole(
                role,
                Pagination.getOffset(page, size),
                Integer.parseInt(size));
        List<UserDto> nextDataList = userService.findAllByRole(
                role,
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size));

        return createPaginatedModel(userList, nextDataList, previousLink, nextLink);
    }
}
