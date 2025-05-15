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
import org.study.hydrowarehouse.entity.Dto.PictureDto;
import org.study.hydrowarehouse.hateoas.HateoasLinkHelper;
import org.study.hydrowarehouse.hateoas.HypermediaListAssembler;
import org.study.hydrowarehouse.service.PictureService;
import org.study.hydrowarehouse.utill.ImageStorage;
import org.study.hydrowarehouse.utill.Pagination;
import org.study.hydrowarehouse.utill.ValidatorParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class {@link PictureController} provides endpoints for accessing pictures data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class PictureController implements HypermediaListAssembler<PictureDto> {

    private static final String PICTURE_NOT_FOUND_MESSAGE = "The requested picture was not found.";

    @Value("${file.upload-product-images-dir}")
    private String productImagesDir;
    private final PictureService pictureService;
    private final ImageStorage imageStorage;

    @Autowired
    public PictureController(PictureService pictureService, ImageStorage imageStorage) {
        this.pictureService = pictureService;
        this.imageStorage = imageStorage;
    }

    /**
     * Handles the creation of a new picture entity by saving the uploaded image file and associating it with
     * the provided picture data.
     * @param file the uploaded image file to be saved, expected as a multipart/form-data request part;
     * identified by {@code ControllerConstants.FILE}
     * @param pictureDto  the data transfer object containing metadata and details for the picture
     * @return {@link ResponseEntity} with status {@code 201 Created} if the picture was successfully created,
     * or {@code 400 Bad Request} if creation failed
     */
    @PostMapping(value = PathPages.PICTURE_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<HttpStatus> create(@RequestPart(ControllerConstants.FILE) MultipartFile file,
                                              @RequestPart(ControllerConstants.PICTURE) PictureDto pictureDto) {
        pictureDto.setPath(imageStorage.save(file, productImagesDir));
        if (pictureService.create(pictureDto)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        imageStorage.removeFile(pictureDto.getPath());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = PathPages.PICTURE_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update ( @RequestPart(ControllerConstants.DATE) PictureDto pictureDto,
                                               @RequestPart(ControllerConstants.FILE) MultipartFile file) {
        pictureDto.setPath(imageStorage.save(file, productImagesDir));
        pictureService.update(pictureDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Handles the removal of a picture entity based on the provided identifier.
     * Validates that the provided ID is a valid numeric string using {@link ValidatorParam#isNumber(String)}.
     * Parses the ID to an integer and attempts to remove the corresponding picture entity via {@code pictureService}.
     *
     * @param id  the string representation of the picture ID to be removed; must be a valid number
     * @return {@link ResponseEntity} with status {@code 200 OK} if the picture was successfully removed,
     * or {@code 400 Bad Request} if removal failed or the ID was invalid
     */
    @GetMapping(value = PathPages.PICTURE_REMOVE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<HttpStatus> remove (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        if (pictureService.remove(Integer.parseInt(id))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Retrieves all pictures associated with a specific product by its ID.
     * Validates that the provided ID is a valid numeric string using {@link ValidatorParam#isNumber(String)}.
     * @param id  the string representation of the product ID; must be a valid number
     * @return a {@link CollectionModel} containing the list of {@link PictureDto} objects associated with the product
     */
    @GetMapping(value = PathPages.PICTURE_BY_PRODUCT, produces = MediaType.APPLICATION_JSON_VALUE)
    private CollectionModel<PictureDto> findByProduct(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return CollectionModel.of(pictureService.findPicturesByProductId(Integer.parseInt(id)));
    }

    /**
     * Retrieves a picture associated with a specific own by its ID.
     * Validates that the provided ID is a valid numeric string using {@link ValidatorParam#isNumber(String)}.
     * @param id  the string representation of the pictures ID; must be a valid number
     * @return a {@link CollectionModel} containing the list of {@link PictureDto} objects associated with the product
     */
    @GetMapping(value = PathPages.PICTURE_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> findById(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return pictureService.findPictureById(Integer.parseInt(id)).
                <ResponseEntity<?>>map(picture -> ResponseEntity.ok(picture)).
                orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, PICTURE_NOT_FOUND_MESSAGE);
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(body);
                });
    }

    /**
     * Retrieves a paginated list of all pictures and adds HATEOAS navigation links.
     * Validates that the {@code size} parameter is a valid number using {@link ValidatorParam#isNumber(String)}.
     * Validates that the {@code page} parameter is a valid page index using {@link ValidatorParam#validPage(String)}.
     * Retrieves a paginated list of pictures from {@code pictureService}.
     * Creates HATEOAS links for navigating to the previous and next pages.
     * Wraps the result and navigation links in a {@link CollectionModel}.
     * @param size  the number of items per page; must be a valid positive integer
     * @param page  the current page number; must be a valid non-negative integer
     * @return  a {@link CollectionModel} containing the list of {@link PictureDto} for the requested page,
     * along with HATEOAS links for pagination ({@code previous}, {@code next})
     */
    @GetMapping(value = PathPages.PICTURE_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    private CollectionModel<PictureDto> findAllPictures(@RequestParam(ControllerConstants.SIZE) String size,
                                                        @RequestParam(ControllerConstants.PAGE) String page) {
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size);

        List<PictureDto> pictureList = pictureService.findAll(
                Pagination.getOffset(page, size),
                Integer.parseInt(size));
        List<PictureDto> nextDataList = pictureService.findAll(
                Pagination.getOffset(
                        Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size));

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                PictureController.class,
                PathPages.PICTURE_ALL,
                searchCriteria);

        Link nextLink = HateoasLinkHelper.createNextLink(
                PictureController.class,
                PathPages.PICTURE_ALL,
                searchCriteria);

        return createPaginatedModel(pictureList, nextDataList, previousLink, nextLink);
    }
}

