package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.UserCompanyDto;
import org.study.hydro.exception.ReportException;
import org.study.hydro.service.UserCompanyService;
import org.study.hydro.utill.ValidatorParam;

import java.util.List;

/**
 * This class {@link CompanyController} provides endpoints for accessing company data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class CompanyController {

    private final UserCompanyService userCompanyService;

    private static final String COMPANY_NOT_FOUND_MESSAGE = "The specified company was not found.";

    @Autowired
    public CompanyController(UserCompanyService userCompanyService) {
        this.userCompanyService = userCompanyService;
    }

    /**
     * The method creates an access point for creating a company in the database.
     * @param company is the date of the new company.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.COMPANY_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create (@RequestBody UserCompanyDto company) {
        if (userCompanyService.create(company)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * The method returns all companies data by the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @return The object of the CollectionModel includes list of the companies.
     */
    @GetMapping(value = PathPages.COMPANY_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<UserCompanyDto> findAllCompanies(@RequestParam(ControllerConstants.PAGE) String page,
                                                        @RequestParam(ControllerConstants.SIZE) String size) {
        ValidatorParam.isNumber(size);
        ValidatorParam.validPage(page);
        List<UserCompanyDto> userCompanyDtoList = userCompanyService.findAll(Integer.parseInt(page), Integer.parseInt(size));
        return CollectionModel.of(userCompanyDtoList);
    }

    /**
     * The method creates an and point for getting the company by the company's id.
     * @param id is the id of the company.
     * @return The object of the companyDTO.
     */
    @GetMapping(value = PathPages.COMPANY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserCompanyDto findCompanyById(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return userCompanyService.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ReportException(HttpStatus.BAD_REQUEST, COMPANY_NOT_FOUND_MESSAGE));
    }

    /**
     * The method creates an and point for getting the companies by company's name.
     * @param name is the name of some the company.
     * @return The object of the CollectionModel includes list of the companies by some name.
     */
    @GetMapping(value = PathPages.COMPANY_BY_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserCompanyDto> findCompanyByName(@RequestParam(ControllerConstants.NAME) String name) {
        return userCompanyService.findByName(name);
    }
}
