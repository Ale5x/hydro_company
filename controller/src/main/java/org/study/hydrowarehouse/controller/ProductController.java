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
     * @return The object of the CollectionModel includes list of the products.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllProducts (@RequestParam(ControllerConstants.PAGE) String page,
                                                        @RequestParam(ControllerConstants.SIZE) String size) {

        List<ProductDto> nextProductList = productService.findAll(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size));

        List<ProductDto> productList = productService.findAll(Pagination.getOffset(page, size), Integer.parseInt(size));
        Link previousLink = HateoasLinkHelper.createPreviousLink(ProductController.class,
                PathPages.PRODUCT_ALL,
                page,
                size);
        Link nextLink = HateoasLinkHelper.createNextLink(ProductController.class,
                PathPages.PRODUCT_ALL,
                page,
                size);

        return createPaginatedModel(productList, nextProductList, previousLink, nextLink);
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
                                                          @RequestParam(ControllerConstants.PRODUCT_PRESSURE) String pressure) {
        ValidatorParam.isNumber(pressure);
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.PRODUCT_PRESSURE,
                pressure);

        List<ProductDto> productList = productService.findAllByPressure(
                Pagination.getOffset(page, size),
                Integer.parseInt(size),
                Integer.parseInt(pressure));
        List<ProductDto> nextDataList = productService.findAllByPressure(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                Integer.parseInt(pressure));

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
     * The method returns all products data by the flowRate and the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param flowRate is some the product's flowRate.
     * @return The object of the CollectionModel includes list of the products by some flowRate.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_FLOW_RATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByFlowRate (@RequestParam(ControllerConstants.PAGE) String page,
                                                          @RequestParam(ControllerConstants.SIZE) String size,
                                                          @RequestParam(ControllerConstants.PRODUCT_FLOW_RATE)String flowRate) {
        ValidatorParam.isNumber(flowRate);
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.PRODUCT_FLOW_RATE,
                flowRate);

        List<ProductDto> productList = productService.findAllByFlowRate(
                Pagination.getOffset(page, size),
                Integer.parseInt(size),
                Integer.parseInt(flowRate));
        List<ProductDto> nextDataList = productService.findAllByFlowRate(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                Integer.parseInt(flowRate));


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
     * The method returns all products data by the product type and the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param id is the id object of the productType.
     * @return The object of the CollectionModel includes list of the products by some product type.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_TYPE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByType (@RequestParam(ControllerConstants.PAGE) String page,
                                                      @RequestParam(ControllerConstants.SIZE) String size,
                                                      @RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.ID,
                id);
        List<ProductDto> productList = productService.findAllByType(
                Pagination.getOffset(page, size),
                Integer.parseInt(size),
                new ProductTypeDto(Integer.parseInt(id)));
        List<ProductDto> nextDataList = productService.findAllByType(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                new ProductTypeDto(Integer.parseInt(id)));

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
     * The method returns all products data by the companies and the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param id is the id of the productCompany.
     * @return The object of the CollectionModel includes list of the products by some product company.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_COMPANY, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByCompany (@RequestParam(ControllerConstants.PAGE) String page,
                                                         @RequestParam(ControllerConstants.SIZE) String size,
                                                         @RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.ID,
                id);

        List<ProductDto> productList = productService.findAllByCompany(
                Pagination.getOffset(page, size),
                Integer.parseInt(size),
                new ProductCompanyDto(Integer.parseInt(id)));
        List<ProductDto> nextDataList = productService.findAllByCompany(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                new ProductCompanyDto(Integer.parseInt(id)));

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
     * The method returns all products data by the storage rack and the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @param name is the name of the storageRack.
     * @return The object of the CollectionModel includes list of the products by some storage rack.
     */
    @GetMapping(value = PathPages.PRODUCT_ALL_BY_STORAGE_RACK, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ProductDto> findAllByStorageRack (@RequestParam(ControllerConstants.PAGE) String page,
                                                         @RequestParam(ControllerConstants.SIZE) String size,
                                                         @RequestParam(ControllerConstants.NAME) String name) {
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size,
                ControllerConstants.NAME,
                name);

        List<ProductDto> productList = productService.findAllByStorageRack(
                Pagination.getOffset(page, size),
                Integer.parseInt(size),
                new StorageRackDto(name));
        List<ProductDto> nextDataList = productService.findAllByStorageRack(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size),
                new StorageRackDto(name));

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