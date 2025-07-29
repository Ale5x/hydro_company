package org.study.hydrowarehouse.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydrowarehouse.configuration.DevelopmentConfig;
import org.study.hydrowarehouse.dao.CountryDao;
import org.study.hydrowarehouse.entity.Country;

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
    private int productId = 1;

    @Test
    void getCountries() {
        Set<Country> countries = countryDao.countries();
        assertTrue(countries.size() > 0);
        assertFalse(countries.isEmpty());

    }

    @Test
    void getByName() {
        Set<Country> countrySetByName = countryDao.countriesByName(countryName);
        assertTrue(countrySetByName.size() > 0);
        assertFalse(countrySetByName.isEmpty());
    }

    @Test
    void getById() {
        Optional<Country> countryById = countryDao.countryById(countryId);

        assertTrue(countryById.isPresent());
    }

    @Test
    void countriesByProduct () {
        Set<Country> countrySetByProduct = countryDao.countriesByProduct(productId);
        assertTrue(countrySetByProduct.size() > 0);
        assertFalse(countrySetByProduct.isEmpty());
    }
}
