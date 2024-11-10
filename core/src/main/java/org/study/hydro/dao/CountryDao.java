package org.study.hydro.dao;


import org.study.hydro.entity.Country;

import java.util.Optional;
import java.util.Set;

/**
 * Interface {@link CountryDao} provides operation with data of database table 'countries'.
 *
 * @author Aliaksandr Pishchala
 */
public interface CountryDao {

    /**
     * The method will return list of the countries.
     *
     * @return the specified list of the countries.
     */
    Set<Country> countries();

    /**
     * The method returns the specified country by id.
     *
     * @param id is the country's id.
     *
     * @return the specified Optional country by id.
     */
    Optional<Country> countryById(int id);

    /**
     * The method returns the specified list of the countries by the name or the same name.
     *
     * @param name is the country's name.
     *
     * @return the specified list of countries by the name or the same name.
     */
    Set<Country> countriesByName(String name);
}
