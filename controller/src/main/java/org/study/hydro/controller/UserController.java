package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.exception.AppRequestException;
import org.study.hydro.service.UserService;
import org.study.hydro.utill.Pagination;
import org.study.hydro.utill.ValidatorParam;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "api/user/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> createUser(@RequestBody UserDto user) {
        if (userService.create(user)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "api/user/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getUser(@RequestParam("id") String id) {
        ValidatorParam.isNumber(id);
        System.out.println(userService.findUserById(Integer.parseInt(id)));
        return userService.findUserById(Integer.parseInt(id))
                .orElseThrow(() -> new AppRequestException("User is not found", HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "api/user/get_all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UserDto> getUsers(@RequestParam("page") String page,
                                             @RequestParam("size") String size) {
        ValidatorParam.isNumber(size);
        ValidatorParam.validPage(page);

        List<UserDto> users = userService.findAll(Integer.parseInt(size), Integer.parseInt(page));
        return CollectionModel.of(users,
                getPreviousLinkForGetUsers(page, size),
                getNextLinkForGetUsers(page, size));
    }

    private Link getPreviousLinkForGetUsers(String page, String size) {
        return linkTo(methodOn(UserController.class)
                .getUsers(Pagination.getPreviousPage(page), size))
                .withRel("Previous").withType("GET");
    }

    private Link getNextLinkForGetUsers(String page, String size) {
        if(Pagination.isNextListEmpty(
                Collections.singletonList(userService.findAll(Integer.parseInt(size),
                Integer.parseInt(Pagination.getNumberNextPage(page)))))) {
           return linkTo(methodOn(UserController.class)
                    .getUsers(page, size))
                    .withRel("Next").withType("GET");
        } else {
            return linkTo(methodOn(UserController.class)
                    .getUsers(Pagination.getNumberNextPage(page), size))
                    .withRel("Next").withType("GET");
        }
    }
}
