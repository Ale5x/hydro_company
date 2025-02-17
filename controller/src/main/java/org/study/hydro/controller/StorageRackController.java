package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydro.entity.Dto.StorageRackDto;
import org.study.hydro.exception.ReportException;
import org.study.hydro.service.StorageRackService;
import org.study.hydro.utill.Pagination;
import org.study.hydro.utill.ValidatorParam;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * This class {@link StorageRackController} provides endpoints for accessing storage rack data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class StorageRackController {

    private final StorageRackService storageRackService;

    private static final String STORAGE_RACK_NOT_FOUND = "Storage rack not found";

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
     * The method creates an access point for updating a storage rack in the database.
     * @param storageRackDto is the date of the updating storage rack.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.STORAGE_RACK_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update (@RequestBody StorageRackDto storageRackDto) {
        if (storageRackService.update(storageRackDto)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method creates an and point for getting the storage rack by the storage rack's id.
     * @param id is the id of the storage rack.
     * @return The object of the companyDTO.
     */
    @GetMapping(value = PathPages.STORAGE_RACK_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public StorageRackDto findById (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return storageRackService.findById(Integer.parseInt(id))
                .orElseThrow(() -> new ReportException(HttpStatus.BAD_REQUEST, STORAGE_RACK_NOT_FOUND));
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
        ValidatorParam.isNumber(size);
        ValidatorParam.isNumber(page);

        Link previousLink = linkTo(methodOn(StorageRackController.class)
                .findAll(Pagination.getPreviousPage(page), size))
                .withRel(ControllerConstants.PREVIOUS).withType(ControllerConstants.METHOD_GET);

        Link nextLink = linkTo(methodOn(StorageRackController.class)
                .findAll(Pagination.getNumberNextPage(page), size))
                .withRel(ControllerConstants.NEXT).withType(ControllerConstants.METHOD_GET);

        return CollectionModel.of(
                storageRackService.storageRackList(Integer.parseInt(size), Integer.parseInt(page)),
                previousLink,
                nextLink);
    }
}
