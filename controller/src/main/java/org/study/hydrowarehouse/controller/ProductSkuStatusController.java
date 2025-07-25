package org.study.hydrowarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydrowarehouse.entity.Dto.ProductSkuStatusDto;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.service.ProductSkuStatusService;

import java.util.Collections;
import java.util.List;

/**
 * REST controller for managing Product SKU Status entities.
 *
 * <p>This controller provides endpoints to create, update, delete, and retrieve
 * Product SKU Status data. It acts as a bridge between the client (UI or external API consumers)
 * and the {@link ProductSkuStatusService} service layer.</p>
 *
 * <p>All endpoints are mapped under the base URL <code>/api/sku-status</code>.</p>
 *
 * <p>Typical usage scenarios include:
 * <ul>
 *     <li>Creating a new SKU status</li>
 *     <li>Updating an existing SKU status</li>
 *     <li>Deleting a SKU status by ID</li>
 *     <li>Fetching all SKU statuses</li>
 *     <li>Searching SKU status by ID or by status name</li>
 * </ul>
 * </p>
 *
 * <p>Example base URL: <code>http://localhost:8080/api/sku-status</code></p>
 *
 * @author Aliaksandr Pishchala
 * @since 07.23.2025
 */
@RestController
public class ProductSkuStatusController {
    private final ProductSkuStatusService productSkuStatusService;

    private static final String STATUS_NOT_FOUND_MESSAGE = "We couldn't find the product status you were looking for.";
    private static final String STATUS_BY_ID_NOT_FOUND_MESSAGE =
            "The requested Product SKU status with the specified ID was not found. Please verify the ID and try again.";

    @Autowired
    public ProductSkuStatusController(ProductSkuStatusService productSkuStatusService) {
        this.productSkuStatusService = productSkuStatusService;
    }

    /**
     * Creates a new SKU status.
     *
     * @param dto the SKU status DTO to save
     * @return ResponseEntity with 200 OK if saved successfully
     */
    @PostMapping(value = PathPages.SKU_STATUS_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@RequestBody ProductSkuStatusDto dto) {
        try {
            boolean result = productSkuStatusService.save(dto);
            return result ? ResponseEntity.ok().build()
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (CoreException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing SKU status.
     *
     * @param dto the SKU status DTO with updated data
     * @return ResponseEntity with 200 OK if updated successfully
     */
    @PostMapping(value = PathPages.SKU_STATUS_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody ProductSkuStatusDto dto) {
        try {
            boolean result = productSkuStatusService.update(dto);
            return result ? ResponseEntity.ok().build()
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap(ControllerConstants.MESSAGE, STATUS_BY_ID_NOT_FOUND_MESSAGE));
        }
    }

    /**
     * Deletes a SKU status by ID.
     *
     * @param id the ID of the SKU status to delete
     * @return ResponseEntity with 200 OK if removed
     */
    @DeleteMapping(value = PathPages.SKU_STATUS_REMOVE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> remove(@RequestParam(ControllerConstants.ID) Integer id) {
        try {
            boolean result = productSkuStatusService.remove(id);
            return result ? ResponseEntity.ok().build()
                    : ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap(ControllerConstants.MESSAGE, STATUS_NOT_FOUND_MESSAGE));
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap(ControllerConstants.MESSAGE, STATUS_BY_ID_NOT_FOUND_MESSAGE));
        }
    }

    /**
     * Retrieves all SKU statuses.
     *
     * @return List of ProductSkuStatusDto
     */
    @GetMapping(value = PathPages.SKU_STATUS_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductSkuStatusDto>> findAll() {
        try {
            return ResponseEntity.ok(productSkuStatusService.findAll());
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves a SKU status by its ID.
     *
     * @param id the ID of the SKU status
     * @return ProductSkuStatusDto if found
     */
    @GetMapping(value = PathPages.SKU_STATUS_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@RequestParam("id") Integer id) {
        try {
            return productSkuStatusService.findById(id)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Collections.singletonMap(ControllerConstants.MESSAGE, STATUS_NOT_FOUND_MESSAGE)));
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap(ControllerConstants.MESSAGE, STATUS_BY_ID_NOT_FOUND_MESSAGE));
        }
    }

    /**
     * Retrieves a SKU status by its status string.
     *
     * @param status the status value
     * @return ProductSkuStatusDto if found
     */
    @GetMapping(value = PathPages.SKU_STATUS_BY_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByStatus(@RequestParam(ControllerConstants.STATUS) String status) {
        try {
            return productSkuStatusService.findByStatus(status)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Collections.singletonMap(ControllerConstants.MESSAGE, STATUS_NOT_FOUND_MESSAGE)));
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap(ControllerConstants.MESSAGE, e.getMessage()));
        }
    }
}