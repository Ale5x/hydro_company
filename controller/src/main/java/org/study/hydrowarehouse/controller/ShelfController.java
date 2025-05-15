package org.study.hydrowarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.study.hydrowarehouse.entity.Dto.ShelfDto;
import org.study.hydrowarehouse.service.ShelfService;
import org.study.hydrowarehouse.utill.ValidatorParam;

import java.util.Collections;
import java.util.Map;

/**
 * This class {@link ShelfController} provides endpoints for accessing shelf data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class ShelfController {

    private final ShelfService shelfService;

    private static final String SHELF_NOT_FOUND_MESSAGE = "The specified shelf was not found.";

    @Autowired
    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    /**
     * The method creates an access point for creating a shelf in the database.
     * @param shelfDto is the date of the shelf company.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @PostMapping(value = PathPages.SHELF_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> create(@RequestBody ShelfDto shelfDto) {
        shelfService.create(shelfDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Updates the information of a shelf based on the provided {@link ShelfDto}.
     * This method is invoked through a {@code POST} request to update the shelf details.
     *
     * @param shelfDto The {@link ShelfDto} containing the updated information of the shelf.
     *                 This DTO should include all necessary fields to perform the update.
     * @return A {@link ResponseEntity} with an HTTP status code. In this case, it returns {@code OK} (200),
     *         indicating that the shelf has been successfully updated.
     */
    @PostMapping(value = PathPages.SHELF_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> update (@RequestBody ShelfDto shelfDto) {
        shelfService.update(shelfDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * The method creates an and point for getting the shelf by the shelf's id.
     * @param id is the id of the shelf.
     * @return The object of the shelfDTO.
     */
    @GetMapping(value = PathPages.SHELF_BY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findById (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return shelfService.findById(Integer.parseInt(id))
                .<ResponseEntity<?>>map(shelfDto -> ResponseEntity.ok(shelfDto))
                .orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, SHELF_NOT_FOUND_MESSAGE);
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(body);
                });
    }

    /**
     * The method creates an and point for getting the shelf by the shelf's name.
     * @param name is the name of the shelf.
     * @return The object of the shelfDTO.
     */
    @GetMapping(value = PathPages.SHELF_BY_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> findByName (@RequestParam(ControllerConstants.NAME) String name) {
        return shelfService.findByName(name)
                .<ResponseEntity<?>>map(shelfDto -> ResponseEntity.ok(shelfDto))
                .orElseGet(() -> {
                    Map<String, String> body = Collections.singletonMap(ControllerConstants.MESSAGE, SHELF_NOT_FOUND_MESSAGE);
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(body);
                });
    }

    /**
     * The method creates an access point for removing a shelf in the database by the shelf's id.
     * @param id is the id of the shelf.
     * @return The instance of ResponseEntity with the HttpStatus.
     */
    @GetMapping(value = PathPages.SHELF_REMOVE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> remove (@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        if (shelfService.remove(Integer.parseInt(id))) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * The method creates an and point for getting the list of the shelf.
     * @return The object's list of the ShelfDTO.
     */
    @GetMapping(value = PathPages.SHELF_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<ShelfDto> findAll() {
        return CollectionModel.of(shelfService.findAll());
    }
}
