package org.study.hydro.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.ProductCompanyDao;
import org.study.hydro.entity.ProductCompany;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class ProductCompanyDaoImplTest {

    @Autowired
    private ProductCompanyDao productCompanyDao;

    private int productCompanyId = 1;
    private String newName = "BOSH - 2";
    private String createNewProductCompanyName = "AlG link";
    private int offset = 1;
    private int limit = 10;

    @BeforeEach
    void setUp() {
    }

    @Test
    void create() {
        List<ProductCompany> productCompanyListBefore = productCompanyDao.getProductCompanies(offset, limit);

        assertFalse(productCompanyListBefore.isEmpty());

        ProductCompany productCompany = new ProductCompany(createNewProductCompanyName);
        boolean condition = productCompanyDao.create(productCompany);

        assertTrue(condition);

        List<ProductCompany> productCompanyListAfter = productCompanyDao.getProductCompanies(offset, limit);

        assertFalse(productCompanyListAfter.isEmpty());
        assertTrue(productCompanyListAfter.size() > productCompanyListBefore.size());

    }

    @Test
    void getById() {
        Optional<ProductCompany> productCompany = productCompanyDao.getById(productCompanyId);

        assertTrue(productCompany.isPresent());
    }

    @Test
    void getProductCompanies() {
        List<ProductCompany> productCompanyList = productCompanyDao.getProductCompanies(offset, limit);
        assertFalse(productCompanyList.isEmpty());
        assertTrue(productCompanyList.size() > 0);
    }

    @Test
    void update() {
        Optional<ProductCompany> productCompanyBefore = productCompanyDao.getById(productCompanyId);
        assertTrue(productCompanyBefore.isPresent());

        ProductCompany productCompany = productCompanyBefore.get();
        productCompany.setName(newName);

        productCompanyDao.update(productCompany);

        Optional<ProductCompany> productCompanyAfter = productCompanyDao.getById(productCompanyId);
        assertTrue(productCompanyAfter.isPresent());

        assertEquals(newName, productCompanyAfter.get().getName());

    }

    @Test
    void getProductCompaniesByName() {
        Optional<ProductCompany> productCompany = productCompanyDao.getById(productCompanyId);
        assertTrue(productCompany.isPresent());

        List<ProductCompany> productCompanyList = productCompanyDao.getProductCompaniesByName(productCompany.get().getName());
        assertFalse(productCompanyList.isEmpty());
        assertTrue(productCompanyList.size() > 0);
    }
}