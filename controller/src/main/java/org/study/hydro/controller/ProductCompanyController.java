package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.ProductCompanyDto;
import org.study.hydro.exception.ReportException;
import org.study.hydro.service.ProductCompanyService;
import org.study.hydro.utill.ValidatorParam;

/**
 * This class {@link ProductCompanyController} provides endpoints for accessing product company data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class ProductCompanyController {

    private final ProductCompanyService productCompanyService;

    private static final String PRODUCT_COMPANY_NOT_FOUND = "Product Company not found";

    @Autowired
    public ProductCompanyController(ProductCompanyService productCompanyService) {
        this.productCompanyService = productCompanyService;
    }

    /**
     * The method creates an access point for creating a product company in the database.
     * @param productCompanyDto is the date of the new product company.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.PRODUCT_COMPANY_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create(@RequestBody ProductCompanyDto productCompanyDto) {
        if (productCompanyService.create(productCompanyDto)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method creates an access point for updating a product company in the database.
     * @param productCompanyDto is the date of the updating product company.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.PRODUCT_COMPANY_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update(@RequestBody ProductCompanyDto productCompanyDto) {
        if (productCompanyService.update(productCompanyDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method returns all product companies data by page.
     * @return The object of the CollectionModel includes list of product companies.
     */
    @GetMapping(value = PathPages.PRODUCT_COMPANY_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductCompanyDto> findAllProductCompanies() {
        return CollectionModel.of(productCompanyService.findAll());
    }

    /**
     * The method creates an and point for getting the product company by product company's id.
     * @param id is the id of the product company.
     * @return The object of the productCompanyDTO.
     */
    @GetMapping(value = PathPages.PRODUCT_COMPANY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductCompanyDto findProductCompanyById(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return productCompanyService.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ReportException(HttpStatus.BAD_REQUEST, PRODUCT_COMPANY_NOT_FOUND));
    }

    /**
     * The method creates an and point for getting the product companies by product company's name.
     * @param name is the name of some the product company.
     * @return The object of the CollectionModel includes list of product companies by some name.
     */
    @GetMapping(value = PathPages.PRODUCT_COMPANY_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductCompanyDto> findProductCompanyByName(@RequestParam(ControllerConstants.NAME) String name) {
        return CollectionModel.of(productCompanyService.findAllByName(name));
    }
}
