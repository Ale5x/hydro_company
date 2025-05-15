package org.study.hydrowarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydrowarehouse.entity.Dto.UserDto;
import org.study.hydrowarehouse.hateoas.HateoasLinkHelper;
import org.study.hydrowarehouse.hateoas.HypermediaListAssembler;
import org.study.hydrowarehouse.service.UserService;
import org.study.hydrowarehouse.utill.Pagination;
import org.study.hydrowarehouse.utill.ValidatorParam;

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

    @PostMapping(value = PathPages.USER_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update ( @RequestBody UserDto userDto) {
        userService.update(userDto);
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
}
