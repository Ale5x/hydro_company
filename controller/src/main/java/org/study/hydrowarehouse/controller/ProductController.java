package org.study.hydrowarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.study.hydrowarehouse.entity.Dto.ProductCompanyDto;
import org.study.hydrowarehouse.entity.Dto.ProductDto;
import org.study.hydrowarehouse.entity.Dto.ProductTypeDto;
import org.study.hydrowarehouse.entity.Dto.StorageRackDto;
import org.study.hydrowarehouse.exception.AppRequestException;
import org.study.hydrowarehouse.hateoas.HateoasLinkHelper;
import org.study.hydrowarehouse.hateoas.HypermediaListAssembler;
import org.study.hydrowarehouse.service.ProductService;
import org.study.hydrowarehouse.utill.*;
import org.study.hydrowarehouse.utill.filestorage.ImageStorage;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class {@link ProductController} provides endpoints for accessing product data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class ProductController implements HypermediaListAssembler<ProductDto> {

    private final ProductService productService;

    private final ImageStorage localImageStorage;

    private static final String PRODUCT_NOT_FOUND_MESSAGE = "The specified product was not found.";
    private static final String MANY_FILES_FOR_THIS_PRODUCT_MESSAGE =
            "A product can have no more than 10 files. Please remove excess files before proceeding.";

    @Value("${file.upload-product-scheme-dir}")
    private String schemeDir;

    @Value("${file.limit_pictures}")
    private int limitFiles;

    @Value("${file.upload-product-images-dir}")
    private String imagesDir;

    @Autowired
    public ProductController(ProductService productService, ImageStorage localImageStorage) {
        this.productService = productService;
        this.localImageStorage = localImageStorage;
    }

    /**
     * The method creates an access point for creating a product in the database.
     * @param productDto is the date of the new product.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.PRODUCT_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create (
            @RequestPart(ControllerConstants.DATE) ProductDto productDto,
            @RequestPart(ControllerConstants.FILE_PRODUCT_SCHEME) MultipartFile fileScheme,
            @RequestPart(ControllerConstants.FILES) List<MultipartFile> files) {
        productDto.setPathHydraulicScheme(localImageStorage.save(fileScheme, schemeDir));
        productDto.setImagesPaths(localImageStorage.saveAll(files, imagesDir));
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
    public ResponseEntity<HttpStatus> update (@RequestPart(ControllerConstants.DATE) ProductDto productDto,
                                              @RequestPart(ControllerConstants.FILE_PRODUCT_SCHEME) MultipartFile fileScheme) {
        if (fileScheme != null && !fileScheme.isEmpty()) {
            productDto.setPathHydraulicScheme(localImageStorage.save(fileScheme, imagesDir));
        }
        if (productService.update(productDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates the images associated with a product.
     * <p>
     * This endpoint accepts a product DTO and a list of image files, and attempts to update
     * the product's image paths. If the total number of images exceeds the allowed limit,
     * the update will be rejected.
     * </p>
     *
     * @param productDto the product data containing the product ID to update
     * @param files      the list of image files to add (can be null or empty)
     * @return {@link ResponseEntity}:
     * <ul>
     *     <li>{@code 200 OK} if the update was successful</li>
     *     <li>{@code 404 NOT FOUND} if the product already has the maximum number of images</li>
     *     <li>{@code 400 BAD REQUEST} if the product is not found or the update fails</li>
     * </ul>
     *
     * @throws AppRequestException if the product is not found
     */
    @PostMapping(value = PathPages.PRODUCT_UPDATE_IMAGES, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateImages (@RequestPart(ControllerConstants.DATE) ProductDto productDto,
                                           @RequestPart(ControllerConstants.FILES) List<MultipartFile> files) {
        ProductDto product = productService.findById(productDto.getProductDtoId()).orElseThrow(() -> {
            // logger
            throw new AppRequestException(PRODUCT_NOT_FOUND_MESSAGE, HttpStatus.BAD_REQUEST);
        });
        if (product.getImagesPaths().size() >= limitFiles) {
            Map<String, String> body = Collections
                    .singletonMap(ControllerConstants.MESSAGE, MANY_FILES_FOR_THIS_PRODUCT_MESSAGE);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }
        if (files != null && !files.isEmpty() && (product.getImagesPaths().size() + files.size()) <= limitFiles) {
            productDto.setImagesPaths(localImageStorage.saveAll(files, imagesDir));
        }
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
    public ResponseEntity<?> findProductById (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return productService.findById(Integer.parseInt(id))
                .<ResponseEntity<?>>map(productDto -> ResponseEntity.ok(productDto))
                .orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, PRODUCT_NOT_FOUND_MESSAGE);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                });
    }

    /**
     * The method returns all products data by the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param status   the status of associated product SKUs to filter by;
     *                 only products having at least one SKU with the given status will be included.
     * @return a {@link CollectionModel} containing the paginated list of {@link ProductDto} objects
     *         matching the specified status criteria, along with pagination links.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllProducts (@RequestParam(ControllerConstants.PAGE) String page,
                                                        @RequestParam(ControllerConstants.SIZE) String size,
                                                        @RequestParam(ControllerConstants.STATUS) String status) {

        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.STATUS, status);

        List<ProductDto> nextProductList = productService.findAll(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                status);

        List<ProductDto> productList = productService.findAll(Pagination.getOffset(page, size), Integer.parseInt(size), status);
        Link previousLink = HateoasLinkHelper.createPreviousLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_PRESSURE,
                searchCriteria);
        Link nextLink = HateoasLinkHelper.createNextLink(
                ProductController.class,
                PathPages.PRODUCT_ALL,
                searchCriteria);

        return createPaginatedModel(productList, nextProductList, previousLink, nextLink);
    }

    /**
     * Retrieves a paginated collection of {@link ProductDto} filtered by a specific pressure value
     * and product SKU status.
     *
     * @param page     the current page number (as a String).
     * @param size     the number of items per page (as a String).
     * @param pressure the pressure value used to filter the products (as a String).
     * @param status   the status of associated product SKUs to filter by;
     *                 only products having at least one SKU with the given status will be included.
     * @return a {@link CollectionModel} containing the paginated list of {@link ProductDto} objects
     *         matching the specified pressure and status criteria, along with pagination links.
     *
     * @throws IllegalArgumentException if the pressure parameter is not a valid number.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_PRESSURE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByPressure (@RequestParam(ControllerConstants.PAGE) String page,
                                                          @RequestParam(ControllerConstants.SIZE) String size,
                                                          @RequestParam(ControllerConstants.PRODUCT_PRESSURE) String pressure,
                                                          @RequestParam(ControllerConstants.STATUS) String status) {
        ValidatorParam.isNumber(pressure);
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.PRODUCT_PRESSURE, pressure,
                ControllerConstants.STATUS, status);

        List<ProductDto> productList = productService.findAllByPressure(
                Pagination.getOffset(page, size),
                Integer.parseInt(size),
                Integer.parseInt(pressure),
                status);
        List<ProductDto> nextDataList = productService.findAllByPressure(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                Integer.parseInt(pressure),
                status);

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_PRESSURE,
                searchCriteria);

        Link nextLink = HateoasLinkHelper.createNextLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_PRESSURE,
                searchCriteria);

        return createPaginatedModel(productList, nextDataList, previousLink, nextLink);
    }

    /**
     * Retrieves a paginated collection of {@link ProductDto} filtered by a specific flowRate value
     * and product SKU status.
     *
     * @param page     the current page number (as a String).
     * @param size     the number of items per page (as a String).
     * @param flowRate the flowRate value used to filter the products (as a String).
     * @param status   the status of associated product SKUs to filter by;
     *                 only products having at least one SKU with the given status will be included.
     * @return a {@link CollectionModel} containing the paginated list of {@link ProductDto} objects
     *         matching the specified flowRate and status criteria, along with pagination links.
     *
     * @throws IllegalArgumentException if the flowRate parameter is not a valid number.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_FLOW_RATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByFlowRate (@RequestParam(ControllerConstants.PAGE) String page,
                                                          @RequestParam(ControllerConstants.SIZE) String size,
                                                          @RequestParam(ControllerConstants.PRODUCT_FLOW_RATE)String flowRate,
                                                          @RequestParam(ControllerConstants.STATUS) String status) {
        ValidatorParam.isNumber(flowRate);
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.PRODUCT_FLOW_RATE, flowRate,
                ControllerConstants.STATUS, status);

        List<ProductDto> productList = productService.findAllByFlowRate(
                Integer.parseInt(size),
                Pagination.getOffset(page, size),
                Integer.parseInt(flowRate),
                status);
        List<ProductDto> nextDataList = productService.findAllByFlowRate(
                Integer.parseInt(size),
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(flowRate),
                status);


        Link previousLink = HateoasLinkHelper.createPreviousLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_FLOW_RATE,
                searchCriteria);

        Link nextLink = HateoasLinkHelper.createNextLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_FLOW_RATE,
                searchCriteria);

        return createPaginatedModel(productList, nextDataList, previousLink, nextLink);
    }

    /**
     * Retrieves a paginated collection of {@link ProductDto} filtered by a specific product type
     * and product SKU status.
     *
     * @param page   the current page number (as a String).
     * @param size   the number of items per page (as a String).
     * @param id     the unique identifier of the product type (as a String).
     * @param status the status of associated product SKUs to filter by;
     *               only products having at least one SKU with the given status will be included.
     * @return a {@link CollectionModel} containing the paginated list of {@link ProductDto} objects
     *         matching the specified product type and status criteria, along with pagination links.
     *
     * @throws IllegalArgumentException if the id parameter is not a valid number.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByType (@RequestParam(ControllerConstants.PAGE) String page,
                                                      @RequestParam(ControllerConstants.SIZE) String size,
                                                      @RequestParam(ControllerConstants.ID) String id,
                                                      @RequestParam(ControllerConstants.STATUS) String status) {
        ValidatorParam.isNumber(id);
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.ID, id,
                ControllerConstants.STATUS, status);
        List<ProductDto> productList = productService.findAllByType(
                Pagination.getOffset(page, size),
                Integer.parseInt(size),
                new ProductTypeDto(Integer.parseInt(id)),
                status);
        List<ProductDto> nextDataList = productService.findAllByType(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                new ProductTypeDto(Integer.parseInt(id)),
                status);

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_TYPE,
                searchCriteria);

        Link nextLink = HateoasLinkHelper.createNextLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_TYPE,
                searchCriteria);

        return createPaginatedModel(productList, nextDataList, previousLink, nextLink);
    }

    /**
     * Retrieves a paginated collection of {@link ProductDto} filtered by a specific product company
     * and product SKU status.
     *
     * @param page   the current page number (as a String).
     * @param size   the number of items per page (as a String).
     * @param id     the unique identifier of the product company (as a String).
     * @param status the status of associated product SKUs to filter by;
     *               only products having at least one SKU with the given status will be included.
     * @return a {@link CollectionModel} containing the paginated list of {@link ProductDto} objects
     *         matching the specified product company and status criteria, along with pagination links.
     *
     * @throws IllegalArgumentException if the id parameter is not a valid number.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_COMPANY, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByCompany (@RequestParam(ControllerConstants.PAGE) String page,
                                                         @RequestParam(ControllerConstants.SIZE) String size,
                                                         @RequestParam(ControllerConstants.ID) String id,
                                                         @RequestParam(ControllerConstants.STATUS) String status) {
        ValidatorParam.isNumber(id);
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.ID, id,
                ControllerConstants.STATUS, status);

        List<ProductDto> productList = productService.findAllByCompany(
                Pagination.getOffset(page, size),
                Integer.parseInt(size),
                new ProductCompanyDto(Integer.parseInt(id)),
                status);
        List<ProductDto> nextDataList = productService.findAllByCompany(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                new ProductCompanyDto(Integer.parseInt(id)),
                status);

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_COMPANY,
                searchCriteria);

        Link nextLink = HateoasLinkHelper.createNextLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_COMPANY,
                searchCriteria);

        return createPaginatedModel(productList, nextDataList, previousLink, nextLink);
    }

    /**
     * Retrieves a paginated collection of {@link ProductDto} filtered by a specific storage rack name
     * and product SKU status.
     *
     * @param page   the current page number (as a String).
     * @param size   the number of items per page (as a String).
     * @param name   the unique name of the storage rack (as a String).
     * @param status the status of associated product SKUs to filter by;
     *               only products having at least one SKU with the given status will be included.
     * @return a {@link CollectionModel} containing the paginated list of {@link ProductDto} objects
     *         matching the specified storage rack and status criteria, along with pagination links.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_STORAGE_RACK, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByStorageRack (@RequestParam(ControllerConstants.PAGE) String page,
                                                         @RequestParam(ControllerConstants.SIZE) String size,
                                                         @RequestParam(ControllerConstants.NAME) String name,
                                                             @RequestParam(ControllerConstants.STATUS) String status) {
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.NAME, name,
                ControllerConstants.STATUS, status);

        List<ProductDto> productList = productService.findAllByStorageRack(
                Pagination.getOffset(page, size),
                Integer.parseInt(size),
                new StorageRackDto(name),
                status);
        List<ProductDto> nextDataList = productService.findAllByStorageRack(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                new StorageRackDto(name),
                status);

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_STORAGE_RACK,
                searchCriteria);

        Link nextLink = HateoasLinkHelper.createNextLink(
                ProductController.class,
                PathPages.PRODUCT_ALL_BY_STORAGE_RACK,
                searchCriteria);

        return createPaginatedModel(productList, nextDataList, previousLink, nextLink);
    }
}