package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.entity.Country;
import org.study.hydrowarehouse.entity.Dto.CountryDto;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The ProductService interface {@link CountryService} provides methods for managing countries.
 * It defines basic operations for getting, getting by name etc. countries.
 *
 * @author Aliaksandr Pishchala
 */
public interface CountryService {

    /**
     * Retrieves a list of all countriesDto.
     * @return a list of all countriesDto as {@link CountryDto} objects.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<CountryDto> findCountries();

    /**
     * Retrieves a list of countriesDto filtered by a specific name value.
     * @param name the name value used to filter the countries.
     * @return a list of {@link CountryDto} objects representing the countriesDto that match the specified name value.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<CountryDto> findByName(String name);

    /**
     * Retrieves a countryDto by its unique identifier.
     * @param id the unique identifier of the country.
     * @return an Optional object of {@link CountryDto} object representing the country with the specified id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<CountryDto> findById(int id);

    /**
     * Retrieves a country by its unique identifier.
     * @param id the unique identifier of the country.
     * @return an Optional object of {@link Country} object representing the country with the specified id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<Country> findCountryById(int id);
}
