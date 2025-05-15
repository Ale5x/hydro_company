package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.ProductTypeDto;
import org.study.hydro.service.ProductTypeService;
import org.study.hydro.utill.ValidatorParam;

import java.util.Collections;
import java.util.Map;

/**
 * This class {@link ProductTypeController} provides endpoints for accessing product type data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    private static final String PRODUCT_TYPE_NOT_FOUND_MESSAGE = "The specified product type was not found.";

    @Autowired
    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    /**
     * The method creates an access point for creating a product type in the database.
     * @param productTypeDto is the date of the new product type.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.PRODUCT_TYPE_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create (@RequestBody ProductTypeDto productTypeDto) {
        if (productTypeService.create(productTypeDto)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates the information of a product type based on the provided {@link ProductTypeDto}.
     * This method is typically called through a {@code POST} request.
     *
     * @param productTypeDto The {@link ProductTypeDto} containing the updated information of the product type.
     *                       The {@link ProductTypeDto} should contain all necessary data to update an existing product type.
     * @return A {@link ResponseEntity} containing the HTTP status code. In this case, a status of {@code OK} (200) is returned,
     *         indicating that the product type has been successfully updated.
     */
    @PostMapping(value = PathPages.PRODUCT_TYPE_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update (@RequestBody ProductTypeDto productTypeDto) {
        productTypeService.update(productTypeDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * The method creates an and point for removing the product type by product type's id.
     * @param id is the id of the product type.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @GetMapping(value = PathPages.PRODUCT_TYPE_REMOVE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> remove (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        if (productTypeService.remove(Integer.parseInt(id))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method returns all product types data.
     * @return The object of the CollectionModel includes list of the product types.
     */
    @GetMapping(value = PathPages.PRODUCT_TYPE_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductTypeDto> findAll () {
        return CollectionModel.of(productTypeService.findAll());
    }

    /**
     * The method creates an and point for getting the product type by the product type's id.
     * @param id is the id of the product type.
     * @return The object of the productTypeDTO.
     */
    @GetMapping(value = PathPages.PRODUCT_TYPE_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findById (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return productTypeService.findById(Integer.parseInt(id))
                .<ResponseEntity<?>>map(productTypeDto -> ResponseEntity.ok(productTypeDto))
                .orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, PRODUCT_TYPE_NOT_FOUND_MESSAGE);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                });
    }

    /**
     * The method creates an and point for getting the product type by the product type's name.
     * @param name is the name of the product type.
     * @return The object's list of the productTypeDTO.
     */
    @GetMapping(value = PathPages.PRODUCT_TYPE_BY_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CollectionModel<ProductTypeDto> findByName (@RequestParam(ControllerConstants.NAME) String name) {
        return CollectionModel.of(productTypeService.findByName(name));
    }
}
