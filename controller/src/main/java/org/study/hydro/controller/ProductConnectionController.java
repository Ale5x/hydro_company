package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.ProductConnectionDto;
import org.study.hydro.service.ProductConnectionService;
import org.study.hydro.utill.ValidatorParam;

import java.util.Collections;
import java.util.Map;

/**
 * This class {@link ProductConnectionController} provides endpoints for accessing product connection data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class ProductConnectionController {

    private final ProductConnectionService productConnectionService;

    private static final String PRODUCT_CONNECTION_NOT_FOUND_MESSAGE = "The specified product connection was not found.";

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
     * Updates the product connection information.
     * <p>
     * This endpoint updates the product connection details based on the provided
     * {@link ProductConnectionDto}. It performs validation and updates the product
     * connection through the service layer.
     * </p>
     *
     * @param connectionDto the {@link ProductConnectionDto} containing the updated
     *                      information for the product connection.
     * @return a {@link ResponseEntity} with HTTP status {@link HttpStatus#OK} if the update
     *         is successful, otherwise {@link HttpStatus#BAD_REQUEST} is returned.
     */
    @PostMapping(value = PathPages.PRODUCT_CONNECTION_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update (@RequestBody ProductConnectionDto connectionDto) {
        productConnectionService.update(connectionDto);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<?> findById(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return productConnectionService.findById(Integer.parseInt(id))
                .<ResponseEntity<?>>map(connectionDto -> ResponseEntity.ok(connectionDto))
                .orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, PRODUCT_CONNECTION_NOT_FOUND_MESSAGE);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                });
    }
}
