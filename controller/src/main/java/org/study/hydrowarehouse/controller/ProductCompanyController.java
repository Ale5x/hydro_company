package org.study.hydrowarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydrowarehouse.entity.Dto.ProductCompanyDto;
import org.study.hydrowarehouse.hateoas.HateoasLinkHelper;
import org.study.hydrowarehouse.hateoas.HypermediaListAssembler;
import org.study.hydrowarehouse.service.ProductCompanyService;
import org.study.hydrowarehouse.utill.Pagination;
import org.study.hydrowarehouse.utill.ValidatorParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class {@link ProductCompanyController} provides endpoints for accessing product company data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class ProductCompanyController implements HypermediaListAssembler<ProductCompanyDto> {

    private final ProductCompanyService productCompanyService;

    private static final String PRODUCT_COMPANY_NOT_FOUND_MESSAGE = "The specified product company was not found.";

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
     * Updates the information of a product company based on the provided {@link ProductCompanyDto}.
     * This method is typically called through a {@code POST} request.
     *
     * @param companyDto The {@link ProductCompanyDto} containing the updated information of the product company.
     *                   The {@link ProductCompanyDto} should contain all necessary data to update an existing product company.
     * @return A {@link ResponseEntity} containing the HTTP status code. A status of {@code OK} (200) is returned if the update is successful.
     *         In case of failure, a different HTTP status can be returned (e.g., {@code BAD_REQUEST} or {@code INTERNAL_SERVER_ERROR}).
     */
    @PostMapping(value = PathPages.PRODUCT_COMPANY_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update ( @RequestBody ProductCompanyDto companyDto) {
       productCompanyService.update(companyDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * The method returns all product companies data by page.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @return The object of the CollectionModel includes list of product companies.
     */
    @GetMapping(value = PathPages.PRODUCT_COMPANY_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductCompanyDto> findAllProductCompanies(@RequestParam(ControllerConstants.PAGE) String page,
                                                                      @RequestParam(ControllerConstants.SIZE) String size) {
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size);

        List<ProductCompanyDto> productCompanyList = productCompanyService.findAll(
                Pagination.getOffset(page, size),
                Integer.parseInt(size));
        List<ProductCompanyDto> nextDataList = productCompanyService.findAll(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size));

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                ProductCompanyController.class,
                PathPages.PRODUCT_COMPANY_ALL,
                searchCriteria);

        Link nextLink = HateoasLinkHelper.createNextLink(
                ProductCompanyController.class,
                PathPages.PRODUCT_COMPANY_ALL,
                searchCriteria);

        return createPaginatedModel(productCompanyList, nextDataList, previousLink, nextLink);
    }

    /**
     * The method creates an and point for getting the product company by product company's id.
     * @param id is the id of the product company.
     * @return The object of the productCompanyDTO.
     */
    @GetMapping(value = PathPages.PRODUCT_COMPANY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findProductCompanyById(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return productCompanyService.findById(Integer.parseInt(id)).
                <ResponseEntity<?>>map(productCompanyDto -> ResponseEntity.ok(productCompanyDto))
                .orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, PRODUCT_COMPANY_NOT_FOUND_MESSAGE);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                });
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
