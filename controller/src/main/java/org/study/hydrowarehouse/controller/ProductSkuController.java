package org.study.hydrowarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydrowarehouse.entity.Dto.PictureDto;
import org.study.hydrowarehouse.entity.Dto.ProductDto;
import org.study.hydrowarehouse.entity.Dto.ProductSkuDto;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.hateoas.HateoasLinkHelper;
import org.study.hydrowarehouse.hateoas.HypermediaListAssembler;
import org.study.hydrowarehouse.service.ProductSkuService;
import org.study.hydrowarehouse.utill.Pagination;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProductSkuController implements HypermediaListAssembler<ProductSkuDto> {

    private final ProductSkuService productSkuService;

    private static final String SKU_BY_ID_NOT_FOUND_MESSAGE =
            "The requested Product SKU with the specified ID was not found. Please verify the ID and try again.";

    private static final String SKU_BY_NAME_NOT_FOUND_MESSAGE =
            "The requested Product SKU with the specified name was not found. Please verify the ID and try again.";


    @Autowired
    public ProductSkuController(ProductSkuService productSkuService) {
        this.productSkuService = productSkuService;
    }

    /**
     * Creates a new Product SKU.
     *
     * @param skuDto the ProductSkuDto containing data to create.
     * @return ResponseEntity with HTTP status CREATED if successful,
     *         BAD_REQUEST otherwise.
     */
    @PostMapping(value = PathPages.SKU_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> save(@RequestBody ProductSkuDto skuDto) {
        try {
            boolean result = productSkuService.save(skuDto);
            return result ? ResponseEntity.ok().build()
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (CoreException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates an existing Product SKU.
     *
     * @param skuDto the ProductSkuDto containing updated data.
     * @return ResponseEntity with HTTP status OK if successful,
     *         BAD_REQUEST otherwise.
     */
    @PostMapping(value = PathPages.SKU_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody ProductSkuDto skuDto) {
        try {
            boolean result = productSkuService.update(skuDto);
            return result ? ResponseEntity.ok().build()
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap(ControllerConstants.MESSAGE, SKU_BY_ID_NOT_FOUND_MESSAGE));
        }
    }

    /**
     * Updates the status of an existing Product SKU.
     *
     * @param skuDto the ProductSkuDto containing updated data.
     * @param status the new status string.
     * @return ResponseEntity with HTTP status OK if successful,
     *         BAD_REQUEST otherwise.
     */
    @PostMapping("/update-status")
    public ResponseEntity<?> updateStatus(@RequestBody ProductSkuDto skuDto,
                                          @RequestParam(ControllerConstants.STATUS) String status) {
        try {
            boolean result = productSkuService.updateStatus(skuDto, status);
            return result ? ResponseEntity.ok().build()
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap(ControllerConstants.MESSAGE, SKU_BY_NAME_NOT_FOUND_MESSAGE));
        }
    }

    /**
     * Deletes a Product SKU by its ID.
     *
     * @param id the ID of the ProductSku to delete.
     * @return ResponseEntity with HTTP status OK if deleted,
     *         BAD_REQUEST otherwise.
     */
    @DeleteMapping(value = PathPages.SKU_REMOVE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> remove(@RequestParam(ControllerConstants.ID) Integer id) {
        try {
            boolean result = productSkuService.remove(id);
            return result ? ResponseEntity.ok().build()
                    : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap(ControllerConstants.MESSAGE, SKU_BY_ID_NOT_FOUND_MESSAGE));
        }
    }

    /**
     * Finds a Product SKU by its ID.
     *
     * @param id the ID of the ProductSku to find.
     * @return ResponseEntity containing the found ProductSkuDto with HTTP status OK,
     *         or a NOT_FOUND status with message if not found,
     *         or BAD_REQUEST if an error occurs.
     */
    @GetMapping(value = PathPages.SKU_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@RequestParam(ControllerConstants.ID) Integer id) {
        try {
            Optional<ProductSkuDto> dto = productSkuService.findById(id);
            return dto.<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of(ControllerConstants.MESSAGE, SKU_BY_ID_NOT_FOUND_MESSAGE)));
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ControllerConstants.MESSAGE, e.getMessage()));
        }
    }

    /**
     * Finds Product SKUs by code.
     *
     * @param code the code to search by.
     * @return ResponseEntity with a list of matching ProductSkuDto objects and HTTP status OK,
     *         or BAD_REQUEST if an error occurs.
     */
    @GetMapping(value = PathPages.SKU_BY_CODE)
    public ResponseEntity<List<ProductSkuDto>> findByCode(@RequestParam(ControllerConstants.CODE) String code) {
        try {
            List<ProductSkuDto> dtos = productSkuService.findByCode(code);
            return ResponseEntity.ok(dtos);
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Finds Product SKUs by status with pagination.
     *
     * @param status the status to filter by.
     * @param size  max number of results.
     * @param page pagination offset.
     * @return ResponseEntity with a list of matching ProductSkuDto objects and HTTP status OK,
     *         or BAD_REQUEST if an error occurs.
     */
    @GetMapping(value = PathPages.SKU_ALL_BY_STATUS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<ProductSkuDto>> findAllByStatus(@RequestParam(ControllerConstants.STATUS) String status,
                                                           @RequestParam(ControllerConstants.SIZE) String size,
                                                           @RequestParam(ControllerConstants.PAGE) String page) {
        try {
            Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                    page,
                    size,
                    ControllerConstants.STATUS, status);

            List<ProductSkuDto> skuDtoList = productSkuService.findAllByStatus(
                    Integer.parseInt(size),
                    Pagination.getOffset(page, size),
                    status);
            List<ProductSkuDto> nextDataList = productSkuService.findAllByStatus(
                    Integer.parseInt(size),
                    Pagination.getOffset(
                            Pagination.getNumberNextPage(page), size),
                    status);

            Link previousLink = HateoasLinkHelper.createPreviousLink(
                    ProductSkuController.class,
                    PathPages.SKU_ALL_BY_STATUS,
                    searchCriteria);

            Link nextLink = HateoasLinkHelper.createNextLink(
                    ProductSkuController.class,
                    PathPages.SKU_ALL_BY_STATUS,
                    searchCriteria);

            CollectionModel<ProductSkuDto> model = createPaginatedModel(skuDtoList, nextDataList, previousLink, nextLink);
            return ResponseEntity.ok(model);
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Finds Product SKUs by product with pagination.
     *
     * @param productDto the ProductDto to filter by.
     * @param size      max number of results.
     * @param page     pagination offset.
     * @return ResponseEntity with a list of matching ProductSkuDto objects and HTTP status OK,
     *         or BAD_REQUEST if an error occurs.
     */
    @PostMapping(value = PathPages.SKU_ALL_BY_PRODUCT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<ProductSkuDto>> findAllByProduct(@RequestBody ProductDto productDto,
                                                                @RequestParam(ControllerConstants.SIZE) String size,
                                                                @RequestParam(ControllerConstants.PAGE) String page) {
        try {

            Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                    page,
                    size,
                    ControllerConstants.PRODUCT_ID, productDto.getProductDtoId().toString());

            List<ProductSkuDto> skuDtoList = productSkuService.findAllByProduct(
                    Integer.parseInt(size),
                    Pagination.getOffset(page, size),
                    new ProductDto(productDto.getProductDtoId()));
            List<ProductSkuDto> nextDataList = productSkuService.findAllByProduct(
                    Integer.parseInt(size),
                    Pagination.getOffset(
                            Pagination.getNumberNextPage(page), size),
                    new ProductDto(productDto.getProductDtoId()));

            Link previousLink = HateoasLinkHelper.createPreviousLink(
                    ProductSkuController.class,
                    PathPages.SKU_ALL_BY_PRODUCT,
                    searchCriteria);

            Link nextLink = HateoasLinkHelper.createNextLink(
                    ProductSkuController.class,
                    PathPages.SKU_ALL_BY_PRODUCT,
                    searchCriteria);

            CollectionModel<ProductSkuDto> model = createPaginatedModel(skuDtoList, nextDataList, previousLink, nextLink);
            return ResponseEntity.ok(model);
        } catch (CoreException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
