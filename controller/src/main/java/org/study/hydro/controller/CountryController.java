package org.study.hydro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.hydro.entity.Dto.CountryDto;
import org.study.hydro.service.CountryService;
import org.study.hydro.utill.ValidatorParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class {@link CountryController} provides endpoints for accessing country data.
 *
 * @author Aliaksandr Pishchala
 */
@RestController
public class CountryController {

    private final CountryService countryService;

    private static final String COUNTRY_NOT_FOUND_MESSAGE = "The specified country was not found.";

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    /**
     * The method returns all countries data.
     * @return The object of the CollectionModel includes list of countries.
     */
    @GetMapping(value = PathPages.COUNTRY_ALL, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<CountryDto> findAllCountries() {
        List<CountryDto> countryDtoList = countryService.findCountries();
        return CollectionModel.of(countryDtoList);
    }

    /**
     * The method creates an and point for getting the country by country's id.
     * @param id is the id of the country.
     * @return The object of the countryDTO.
     */
    @GetMapping(value = PathPages.COUNTRY_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findCountryById(@RequestParam(ControllerConstants.ID) String id) {
        ValidatorParam.isNumber(id);
        return countryService.findById(Integer.parseInt(id))
                .<ResponseEntity<?>>map(country -> ResponseEntity.ok(country))
                .orElseGet(() -> {
                    Map<String, String> body = Collections
                            .singletonMap(ControllerConstants.MESSAGE, COUNTRY_NOT_FOUND_MESSAGE);
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(body);
                    });
    }

    /**
     * The method creates an and point for getting the country by country's name.
     * @param name is the name of the country.
     * @return The list of objects includes countriesDTO.
     */
    @GetMapping(value = PathPages.COUNTRY_NAME, produces = MediaType.APPLICATION_JSON_VALUE)
    public CollectionModel<CountryDto> findCountriesByName(@RequestParam(ControllerConstants.NAME) String name) {
        return CollectionModel.of(countryService.findByName(name));
    }
}
