package org.study.hydrowarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydrowarehouse.entity.Dto.UserCompanyDto;
import org.study.hydrowarehouse.hateoas.HateoasLinkHelper;
import org.study.hydrowarehouse.hateoas.HypermediaListAssembler;
import org.study.hydrowarehouse.service.UserCompanyService;
import org.study.hydrowarehouse.utill.Pagination;
import org.study.hydrowarehouse.utill.ValidatorParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class {@link UserCompanyController} provides endpoints for accessing user company data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class UserCompanyController implements HypermediaListAssembler<UserCompanyDto> {

    private final UserCompanyService userCompanyService;

    private static final String USER_COMPANY_NOT_FOUND_MESSAGE = "The specified user company was not found.";

    @Autowired
    public UserCompanyController(UserCompanyService userCompanyService) {
        this.userCompanyService = userCompanyService;
    }

    /**
     * The method creates an access point for creating a user company in the database.
     * @param userCompanyDto is the date of the new user company.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.USER_COMPANY_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create (@RequestBody UserCompanyDto userCompanyDto) {
        if (userCompanyService.create(userCompanyDto)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates the user company information.
     *
     * <p>This endpoint accepts a {@link UserCompanyDto} JSON object in the request body
     * and attempts to update the corresponding user company data.</p>
     *
     * @param userCompanyDto the data transfer object containing updated company details
     * @return {@link ResponseEntity} with {@link HttpStatus#OK} if the update was successful,
     *         or {@link HttpStatus#BAD_REQUEST} if the update failed
     */
    @PostMapping(value = PathPages.USER_COMPANY_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update (@RequestBody UserCompanyDto userCompanyDto) {
        userCompanyService.update(userCompanyDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * The method creates an and point for getting the user companies by user company's name.
     * @param name is the name of some the user company.
     * @return The object of the CollectionModel includes list of the user companies by some name.
     */
    @GetMapping(value = PathPages.USER_COMPANY_BY_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UserCompanyDto> findByName (@RequestParam(ControllerConstants.NAME) String name) {

        return CollectionModel.of(userCompanyService.findByName(name));
    }

    /**
     * The method creates an and point for getting the all user companies.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @return The object of the CollectionModel includes list of the user companies by some name.
     */
    @GetMapping(value = PathPages.USER_COMPANY_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UserCompanyDto> findAll (@RequestParam(ControllerConstants.PAGE) String page,
                                                    @RequestParam(ControllerConstants.SIZE) String size) {
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size);

        List<UserCompanyDto> companyList = userCompanyService.findAll(
                Pagination.getOffset(page, size),
                Integer.parseInt(size));
        List<UserCompanyDto> nextDataList = userCompanyService.findAll(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size));

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                UserCompanyController.class,
                PathPages.USER_COMPANY_ALL,
                searchCriteria);

        Link nextLink = HateoasLinkHelper.createNextLink(
                UserCompanyController.class,
                PathPages.USER_COMPANY_ALL,
                searchCriteria);

        return createPaginatedModel(companyList, nextDataList, previousLink, nextLink);
    }

    /**
     * The method creates an and point for getting the user company by the user company's id.
     * @param id is the id of the user company.
     * @return The object of the UserCompanyDto.
     */
    @GetMapping(value = PathPages.USER_COMPANY_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findById (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return userCompanyService.findById(Integer.parseInt(id)).
                <ResponseEntity<?>>map(userCompanyDto -> ResponseEntity.ok(userCompanyDto))
                .orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, USER_COMPANY_NOT_FOUND_MESSAGE);
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(body);
                });
    }
}
