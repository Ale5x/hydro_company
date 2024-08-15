package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.CompanyDto;
import org.study.hydro.exception.AppRequestException;
import org.study.hydro.service.CompanyService;
import org.study.hydro.utill.ValidatorParam;

import java.util.List;

/**
 * This class {@link CompanyController} provides access endpoints for user data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * The method creates an access point for creating a company in the database.
     * @param company is the date of the new company.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.COMPANY_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create (@RequestBody CompanyDto company) {
        if (companyService.create(company)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * The method returns all companies data by page.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @return The object of the CollectionModel includes list of companies.
     */
    @GetMapping(value = PathPages.COMPANY_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<CompanyDto> getCompanies(@RequestParam(ControllerConstants.PAGE) String page,
                                                    @RequestParam(ControllerConstants.SIZE) String size) {
        ValidatorParam.isNumber(size);
        ValidatorParam.validPage(page);
        List<CompanyDto> companyDtoList = companyService.findAll(Integer.parseInt(page), Integer.parseInt(size));
        return CollectionModel.of(companyDtoList);
    }

    /**
     * The method creates an and point for getting the company by company's id.
     * @param id is the id of the company.
     * @return The object of the companyDTO.
     */
    @GetMapping(value = PathPages.COMPANY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CompanyDto findCompanyById(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return companyService.findById(Integer.parseInt(id)).orElseThrow(() ->
                new AppRequestException("Company doesn't exist", HttpStatus.BAD_REQUEST));
    }

    /**
     * The method creates an and point for getting the companies by company's name.
     * @param name is the name of some company.
     * @return The list of the companiesDTO.
     */
    @GetMapping(value = PathPages.COMPANY_BY_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CompanyDto> findCompanyByName(@RequestParam(ControllerConstants.NAME) String name) {
        return companyService.findByName(name);
    }
}
