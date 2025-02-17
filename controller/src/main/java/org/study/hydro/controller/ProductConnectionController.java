package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.ProductConnectionDto;
import org.study.hydro.exception.ReportException;
import org.study.hydro.service.ProductConnectionService;
import org.study.hydro.utill.ValidatorParam;

/**
 * This class {@link ProductConnectionController} provides endpoints for accessing product connection data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class ProductConnectionController {

    private final ProductConnectionService productConnectionService;

    private static final String PRODUCT_CONNECTION_NOT_FOUND = "Product connection not found";

    @Autowired
    public ProductConnectionController(ProductConnectionService productConnectionService) {
        this.productConnectionService = productConnectionService;
    }

    /**
     * The method creates an access point for creating a product connection in the database.
     * @param connectionDto is the date of the new product connection.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.PRODUCT_CONNECTION_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create (@RequestBody ProductConnectionDto connectionDto) {
        if (productConnectionService.create(connectionDto)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method creates an access point for creating a product connection in the database.
     * @param connectionDto is the date of the updating product connection.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.PRODUCT_CONNECTION_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update (@RequestBody ProductConnectionDto connectionDto) {
        if (productConnectionService.update(connectionDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method returns all product connections data.
     * @return The object of the CollectionModel includes list of the product connections.
     */
    @GetMapping(value = PathPages.PRODUCT_CONNECTION_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductConnectionDto> findAllProductConnection () {
        return CollectionModel.of(productConnectionService.findAll());
    }

    /**
     * The method creates an and point for getting the product connection by product connection's id.
     * @param id is the id of the product connection.
     * @return The object of the productConnectionDTO.
     */
    @GetMapping(value = PathPages.PRODUCT_CONNECTION_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ProductConnectionDto findById(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return productConnectionService.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ReportException(HttpStatus.BAD_REQUEST, PRODUCT_CONNECTION_NOT_FOUND));
    }
}
