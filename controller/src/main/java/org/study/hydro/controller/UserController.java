package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.exception.ReportException;
import org.study.hydro.service.UserService;
import org.study.hydro.utill.Pagination;
import org.study.hydro.utill.ValidatorParam;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class {@link UserController} provides endpoints for accessing user data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class UserController {

    private UserService userService;

    private final static String USER_NOT_FOUND = "User not found";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * The method gives user data by user's id.
     * @param id is the id of the user.
     * @return The object of the userDTO.
     */
    @GetMapping(value = PathPages.USER_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserDto getUser(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return userService.findUserById(Integer.parseInt(id))
                .orElseThrow(() -> new ReportException(HttpStatus.BAD_REQUEST, USER_NOT_FOUND));
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
        ValidatorParam.isNumber(size);
        ValidatorParam.validPage(page);

        List<UserDto> users = userService.findAll(Integer.parseInt(size), Integer.parseInt(page));
        return CollectionModel.of(users,
                getPreviousLinkForGetUsers(page, size),
                getNextLinkForGetUsers(page, size));
    }

    /**
     * The method creates the previous link.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @return The instance of link.
     */
    private Link getPreviousLinkForGetUsers(String page, String size) {
        return linkTo(methodOn(UserController.class)
                .getUsers(Pagination.getPreviousPage(page), size))
                .withRel(ControllerConstants.PREVIOUS).withType(ControllerConstants.METHOD_GET);
    }

    /**
     * The method creates the next link.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @return The instance of link.
     */
    private Link getNextLinkForGetUsers(String page, String size) {
        if(Pagination.isNextListEmpty(
                Collections.singletonList(userService.findAll(Integer.parseInt(size),
                Integer.parseInt(Pagination.getNumberNextPage(page)))))) {
           return linkTo(methodOn(UserController.class)
                    .getUsers(page, size))
                    .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);
        } else {
            return linkTo(methodOn(UserController.class)
                    .getUsers(Pagination.getNumberNextPage(page), size))
                    .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);
        }
    }
}
