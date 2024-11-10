package org.study.hydro.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.CountryDao;
import org.study.hydro.entity.Country;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DevelopmentConfig.class)
@ActiveProfiles("development")
public class CountyDaoImplTest {

    @Autowired
    private CountryDao countryDao;

    private String countryName = "B";
    private int countryId = 1;

    @Test
    void getCountries() {
        Set<Country> countries = countryDao.countries();
        System.out.println(" Count countries --->> "  + countries.size());
        for(Country country : countries) {
            System.out.println("ID --> " + country.getCountryId() + " | name --> " + country.getName());
        }
        assertTrue(countries.size() > 0);
        assertFalse(countries.isEmpty());

    }

    @Test
    void getByName() {
        Set<Country> countrySetByName = countryDao.countriesByName(countryName);
        System.out.println(" Count countries --->> "  + countrySetByName.size());
        for(Country country : countrySetByName) {
            System.out.println("ID --> " + country.getCountryId() + " | name --> " + country.getName());
        }
        assertTrue(countrySetByName.size() > 0);
        assertFalse(countrySetByName.isEmpty());
    }

    @Test
    void getById() {
        Optional<Country> countryById = countryDao.countryById(countryId);
        System.out.println(" Count countries --->> "  + countryById.get().getCountryId() + " | name -> " + countryById.get().getName());

        assertTrue(countryById.isPresent());
    }
}
