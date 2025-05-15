package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.StorageRackDto;
import org.study.hydro.hateoas.HateoasLinkHelper;
import org.study.hydro.hateoas.HypermediaListAssembler;
import org.study.hydro.service.StorageRackService;
import org.study.hydro.utill.Pagination;
import org.study.hydro.utill.ValidatorParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class {@link StorageRackController} provides endpoints for accessing storage rack data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class StorageRackController implements HypermediaListAssembler<StorageRackDto> {

    private final StorageRackService storageRackService;

    private static final String STORAGE_RACK_NOT_FOUND_MESSAGE = "The specified storage rack was not found.";

    @Autowired
    public StorageRackController(StorageRackService storageRackService) {
        this.storageRackService = storageRackService;
    }

    /**
     * The method creates an access point for creating a storage rack in the database.
     * @param storageRackDto is the date of the new storage rack.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.STORAGE_RACK_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create (@RequestBody StorageRackDto storageRackDto) {
        if (storageRackService.create(storageRackDto)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Updates the information of a storage rack based on the provided {@link StorageRackDto}.
     * This method is invoked through a {@code POST} request to update the storage rack details.
     *
     * @param storageRackDto The {@link StorageRackDto} containing the updated information of the storage rack.
     *                       This DTO should include all necessary fields to perform the update.
     * @return A {@link ResponseEntity} with an HTTP status code. In this case, it returns {@code OK} (200),
     *         indicating that the storage rack has been successfully updated.
     */
    @PostMapping(value = PathPages.STORAGE_RACK_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update (@RequestBody StorageRackDto storageRackDto) {
        storageRackService.update(storageRackDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * The method creates an and point for getting the storage rack by the storage rack's id.
     * @param id is the id of the storage rack.
     * @return The object of the companyDTO.
     */
    @GetMapping(value = PathPages.STORAGE_RACK_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findById (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return storageRackService.findById(Integer.parseInt(id))
                .<ResponseEntity<?>>map(storageRackDto -> ResponseEntity.ok(storageRackDto))
                .orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, STORAGE_RACK_NOT_FOUND_MESSAGE);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
                });
    }

    /**
     * The method returns all storage racks data by the page and the size.
     * @param page is the current page.
     * @param size is the count items on this page.
     * @return The object of the CollectionModel includes list of the storage racks.
     */
    @GetMapping(value = PathPages.STORAGE_RACK_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<StorageRackDto> findAll (@RequestParam(ControllerConstants.PAGE) String page,
                                                    @RequestParam(ControllerConstants.SIZE) String size) {
        Map<String, String> searchCriteria = HateoasLinkHelper.buildSearchCriteria(
                page,
                size);
        List<StorageRackDto> storageRackList = storageRackService.storageRackList(
                Pagination.getOffset(page, size),
                Integer.parseInt(size));
        List<StorageRackDto> nextDataList = storageRackService.storageRackList(
                Pagination.getOffset(Pagination.getNumberNextPage(page), size),
                Integer.parseInt(size));

        Link previousLink = HateoasLinkHelper.createPreviousLink(
                StorageRackController.class,
                PathPages.STORAGE_RACK_ALL,
                searchCriteria);

        Link nextLink = HateoasLinkHelper.createNextLink(
                StorageRackController.class,
                PathPages.STORAGE_RACK_ALL,
                searchCriteria);

        return createPaginatedModel(storageRackList, nextDataList, previousLink, nextLink);
    }
}
