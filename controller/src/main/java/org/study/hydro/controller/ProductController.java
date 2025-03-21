package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.ProductCompanyDto;
import org.study.hydro.entity.Dto.ProductDto;
import org.study.hydro.entity.Dto.ProductTypeDto;
import org.study.hydro.entity.Dto.StorageRackDto;
import org.study.hydro.exception.ReportException;
import org.study.hydro.service.ProductService;
import org.study.hydro.utill.Pagination;
import org.study.hydro.utill.ValidatorParam;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class {@link ProductController} provides endpoints for accessing product data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class ProductController {

    private final ProductService productService;

    private static final String PRODUCT_NOT_FOUND = "Product not found";

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * The method creates an access point for creating a product in the database.
     * @param productDto is the date of the new product.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.PRODUCT_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create (@RequestBody ProductDto productDto) {
        if (productService.create(productDto)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method creates an access point for updating a product in the database.
     * @param productDto is the date of the updating product.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.PRODUCT_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update (@RequestBody ProductDto productDto) {
        if (productService.update(productDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method creates an and point for removing the product by product's id.
     * @param id is the id of the product.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @GetMapping(value = PathPages.PRODUCT_REMOVE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> remove (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        if (productService.remove(Integer.parseInt(id))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method creates an and point for getting the product by product's id.
     * @param id is the id of the product.
     * @return The object of the productDTO.
     */
    @GetMapping(value = PathPages.PRODUCT_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductDto findProductById (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return productService.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ReportException(HttpStatus.BAD_REQUEST, PRODUCT_NOT_FOUND));
    }

    /**
     * The method returns all products data by the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @return The object of the CollectionModel includes list of the products.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllProducts (@RequestParam(ControllerConstants.PAGE) String page,
                                                @RequestParam(ControllerConstants.SIZE) String size) {
        ValidatorParam.isNumber(size);
        ValidatorParam.isNumber(page);

        Link previousLink = linkTo(methodOn(ProductController.class)
                .findAllProducts(Pagination.getPreviousPage(page), size))
                .withRel(ControllerConstants.PREVIOUS).withType(ControllerConstants.METHOD_GET);

        Link nextLink = linkTo(methodOn(ProductController.class)
                .findAllProducts(Pagination.getNumberNextPage(page), size))
                .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);

        return CollectionModel.of(
                productService.findAll(Integer.parseInt(size), Integer.parseInt(page)),
                previousLink,
                nextLink);
    }

    /**
     * The method returns all products data by the pressure and the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param pressure is some the product's pressure.
     * @return The object of the CollectionModel includes list of the products by some pressure.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_PRESSURE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByPressure (@RequestParam(ControllerConstants.PAGE) String page,
                                                          @RequestParam(ControllerConstants.SIZE) String size,
                                                          String pressure) {
        ValidatorParam.isNumber(page);
        ValidatorParam.isNumber(size);
        ValidatorParam.isNumber(pressure);

        Link previousLink = linkTo(methodOn(ProductController.class)
                .findAllByPressure(Pagination.getPreviousPage(page), size, pressure))
                .withRel(ControllerConstants.PREVIOUS).withType(ControllerConstants.METHOD_GET);

        Link nextLink = linkTo(methodOn(ProductController.class)
                .findAllByPressure(Pagination.getNumberNextPage(page), size, pressure))
                .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);

        return CollectionModel.of(productService
                .findAllByPressure(Integer.parseInt(size), Integer.parseInt(page), Integer.parseInt(pressure)),
                previousLink,
                nextLink);
    }

    /**
     * The method returns all products data by the flowRate and the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param flowRate is some the product's flowRate.
     * @return The object of the CollectionModel includes list of the products by some flowRate.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_FLOW_RATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByFlowRate (@RequestParam(ControllerConstants.PAGE) String page,
                                                          @RequestParam(ControllerConstants.SIZE) String size,
                                                          String flowRate) {
        ValidatorParam.isNumber(page);
        ValidatorParam.isNumber(size);
        ValidatorParam.isNumber(flowRate);

        Link previousLink = linkTo(methodOn(ProductController.class)
                .findAllByFlowRate(Pagination.getPreviousPage(page), size, flowRate))
                .withRel(ControllerConstants.PREVIOUS).withType(ControllerConstants.METHOD_GET);

        Link nextLink = linkTo(methodOn(ProductController.class)
                .findAllByFlowRate(Pagination.getNumberNextPage(page), size, flowRate))
                .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);

        return CollectionModel.of(productService
                        .findAllByFlowRate(Integer.parseInt(size), Integer.parseInt(page), Integer.parseInt(flowRate)),
                previousLink,
                nextLink);
    }

    /**
     * The method returns all products data by the product type and the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param productTypeDto is the object of the productType.
     * @return The object of the CollectionModel includes list of the products by some product type.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByType (@RequestParam(ControllerConstants.PAGE) String page,
                                                      @RequestParam(ControllerConstants.SIZE) String size,
                                                      @RequestBody ProductTypeDto productTypeDto) {
        ValidatorParam.isNumber(page);
        ValidatorParam.isNumber(size);

        Link previousLink = linkTo(methodOn(ProductController.class)
                .findAllByType(Pagination.getPreviousPage(page), size, productTypeDto))
                .withRel(ControllerConstants.PREVIOUS).withType(ControllerConstants.METHOD_GET);

        Link nextLink = linkTo(methodOn(ProductController.class)
                .findAllByType(Pagination.getNumberNextPage(page), size, productTypeDto))
                .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);

        return CollectionModel.of(productService
                        .findAllByType(Integer.parseInt(size), Integer.parseInt(page), productTypeDto),
                previousLink,
                nextLink);
    }

    /**
     * The method returns all products data by the companies and the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param productCompanyDto is the object of the productCompany.
     * @return The object of the CollectionModel includes list of the products by some product company.
     */
    @GetMapping(value = PathPages.PRODUCT_COMPANY_BRANCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByCompany (@RequestParam(ControllerConstants.PAGE) String page,
                                                      @RequestParam(ControllerConstants.SIZE) String size,
                                                      @RequestBody ProductCompanyDto productCompanyDto) {
        ValidatorParam.isNumber(page);
        ValidatorParam.isNumber(size);

        Link previousLink = linkTo(methodOn(ProductController.class)
                .findAllByCompany(Pagination.getPreviousPage(page), size, productCompanyDto))
                .withRel(ControllerConstants.PREVIOUS).withType(ControllerConstants.METHOD_GET);

        Link nextLink = linkTo(methodOn(ProductController.class)
                .findAllByCompany(Pagination.getNumberNextPage(page), size, productCompanyDto))
                .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);

        return CollectionModel.of(productService
                        .findAllByCompany(Integer.parseInt(size), Integer.parseInt(page), productCompanyDto),
                previousLink,
                nextLink);
    }

    /**
     * The method returns all products data by the storage rack and the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param storageRackDto is the object of the storageRack.
     * @return The object of the CollectionModel includes list of the products by some storage rack.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_STORAGE_RACK, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByStorageRack (@RequestParam(ControllerConstants.PAGE) String page,
                                                         @RequestParam(ControllerConstants.SIZE) String size,
                                                         @RequestBody StorageRackDto storageRackDto) {
        ValidatorParam.isNumber(page);
        ValidatorParam.isNumber(size);

        Link previousLink = linkTo(methodOn(ProductController.class)
                .findAllByStorageRack(Pagination.getPreviousPage(page), size, storageRackDto))
                .withRel(ControllerConstants.PREVIOUS).withType(ControllerConstants.METHOD_GET);

        Link nextLink = linkTo(methodOn(ProductController.class)
                .findAllByStorageRack(Pagination.getNumberNextPage(page), size, storageRackDto))
                .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);

        return CollectionModel.of(productService
                        .findAllByStorageRack(Integer.parseInt(size), Integer.parseInt(page), storageRackDto),
                previousLink,
                nextLink);
    }
}
