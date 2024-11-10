package org.study.hydro.dao.impl;

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
class ProductUserCompanyDaoImplTest {

    @Autowired
    private ProductCompanyDao productCompanyDao;
    private ProductCompany newProductCompany = new ProductCompany("AlG");
    private String searchName = "bosh";
    private String newName = "new name";
    private int productCompanyId = 1;
    private int wrongId = 111;

    @Test
    void create() {
        List<ProductCompany> productCompanyListBefore = productCompanyDao.getProductCompanies();
        assertFalse(productCompanyListBefore.isEmpty());
        assertTrue(productCompanyListBefore.size() > 0);

        boolean condition = productCompanyDao.create(newProductCompany);
        assertTrue(condition);

        List<ProductCompany> productCompanyListAfter = productCompanyDao.getProductCompanies();

        assertFalse(productCompanyListAfter.isEmpty());
        assertTrue(productCompanyListAfter.size() > 0);

        assertTrue(productCompanyListAfter.size() > productCompanyListBefore.size());
    }

    @Test
    void getProductCompanies() {
        List<ProductCompany> productCompanyList = productCompanyDao.getProductCompanies();

        assertFalse(productCompanyList.isEmpty());
        assertTrue(productCompanyList.size() > 0);
    }

    @Test
    void update() {
        Optional<ProductCompany> productCompanyBefore = productCompanyDao.getById(productCompanyId);
        assertTrue(productCompanyBefore.isPresent());
        ProductCompany productCompany = new ProductCompany(productCompanyBefore.get().getProductCompanyId(), newName);

        productCompanyDao.update(productCompany);
        Optional<ProductCompany> productCompanyAfter = productCompanyDao.getById(productCompanyId);

        assertTrue(productCompanyAfter.isPresent());
        assertEquals(newName, productCompanyAfter.get().getName());
    }

    @Test
    void getProductCompaniesByName() {
        Optional<ProductCompany> productCompanyOptional = productCompanyDao.getById(productCompanyId);
        assertTrue(productCompanyOptional.isPresent());

        List<ProductCompany> productCompanyList = productCompanyDao.getProductCompaniesByName(productCompanyOptional.get().getName());

        assertFalse(productCompanyList.isEmpty());
        assertNotNull(productCompanyList);
    }

    @Test
    void getProductCompaniesByIdRightTest() {
        Optional<ProductCompany> productCompanyOptional = productCompanyDao.getById(productCompanyId);
        assertTrue(productCompanyOptional.isPresent());
    }

    @Test
    void getProductCompaniesByIdWrongTest() {
        Optional<ProductCompany> productCompanyOptional = productCompanyDao.getById(wrongId);
        assertTrue(productCompanyOptional.isEmpty());
    }
}