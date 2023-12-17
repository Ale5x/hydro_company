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

@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping(value = "/company/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create (@RequestBody CompanyDto company) {

        if (companyService.create(company)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/company/get_all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<CompanyDto> getCompanies(@RequestParam("page") String page,
                                                    @RequestParam("size") String size) {
        ValidatorParam.isNumber(size);
        ValidatorParam.validPage(page);
        List<CompanyDto> companyDtoList = companyService.findAll(Integer.parseInt(page), Integer.parseInt(size));
        return CollectionModel.of(companyDtoList);
    }

    @GetMapping(value = "/company/id", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CompanyDto findCompanyById(@RequestParam("id") String id) {
        ValidatorParam.isNumber(id);
        return companyService.findById(Integer.parseInt(id)).orElseThrow(() ->
                new AppRequestException("Company doesn't exist", HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "company/find_company", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CompanyDto> findCompanyByName(@RequestParam("name") String name) {
        return companyService.findByName(name);
    }

}
