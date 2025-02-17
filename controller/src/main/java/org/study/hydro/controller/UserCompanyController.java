package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.UserCompanyDto;
import org.study.hydro.exception.ReportException;
import org.study.hydro.service.UserCompanyService;
import org.study.hydro.utill.Pagination;
import org.study.hydro.utill.ValidatorParam;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class {@link UserCompanyController} provides endpoints for accessing user company data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class UserCompanyController {

    private final UserCompanyService userCompanyService;

    private static final String USER_COMPANY_NOT_FOUND = "User company not found";

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
        ValidatorParam.isNumber(size);
        ValidatorParam.isNumber(page);

        Link previousLink = linkTo(methodOn(UserCompanyController.class)
                .findAll(Pagination.getPreviousPage(page), size))
                .withRel(ControllerConstants.PREVIOUS).withType(ControllerConstants.METHOD_GET);

        Link nextLink = linkTo(methodOn(UserCompanyController.class)
                .findAll(Pagination.getNumberNextPage(page), size))
                .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);

        return CollectionModel.of(
                userCompanyService.findAll(Integer.parseInt(size), Integer.parseInt(page)),
                previousLink,
                nextLink);
    }

    /**
     * The method creates an and point for getting the user company by the user company's id.
     * @param id is the id of the user company.
     * @return The object of the UserCompanyDto.
     */
    @GetMapping(value = PathPages.USER_COMPANY_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserCompanyDto findById (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return userCompanyService.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ReportException(HttpStatus.BAD_REQUEST, USER_COMPANY_NOT_FOUND));
    }
}
