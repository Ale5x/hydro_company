package org.study.hydro.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.ProductTypeDao;
import org.study.hydro.entity.ProductType;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class ProductTypeDaoImplTest {

    @Autowired
    private ProductTypeDao productTypeDao;

    private int productTypeId = 1;
    private ProductType productType = new ProductType(111, "Test type");

    @BeforeEach
    void setUp() {
    }

    @Test
    void create() {
        List<ProductType> productTypesListBefore = productTypeDao.getProductTypes();
        assertFalse(productTypesListBefore.isEmpty());

        boolean condition = productTypeDao.create(productType);
        assertTrue(condition);

        List<ProductType> productTypesListAfter = productTypeDao.getProductTypes();
        assertFalse(productTypesListAfter.isEmpty());

        assertTrue(productTypesListAfter.size() > productTypesListBefore.size());
    }

    @Test
    void getProductTypes() {
        List<ProductType> productTypeList = productTypeDao.getProductTypes();

        assertFalse(productTypeList.isEmpty());
        assertNotNull(productTypeList);
    }

    @Test
    void getProductTypeById() {
        Optional<ProductType> productTypeOptional = productTypeDao.getProductTypeById(productTypeId);

        assertTrue(productTypeOptional.isPresent());
        assertTrue(productTypeOptional.get().getProductTypeId() == productTypeId);
    }

    @Test
    void getProductTypeByName() {
        Optional<ProductType> productTypeOptional = productTypeDao.getProductTypeById(productTypeId);
        assertTrue(productTypeOptional.isPresent());

        Optional<ProductType> prTypeByName = productTypeDao.getProductTypeByName(productTypeOptional.get().getName());
        assertTrue(prTypeByName.isPresent());
    }

    @Test
    void update() {
        ProductType newProductType = new ProductType("new Product");
        List<ProductType> productTypesListBefore = productTypeDao.getProductTypes();
        assertFalse(productTypesListBefore.isEmpty());
        assertTrue(productTypesListBefore.size() > 0);

        productTypeDao.update(newProductType);

        List<ProductType> productTypesListAfter = productTypeDao.getProductTypes();
        assertFalse(productTypesListAfter.isEmpty());

        assertTrue(productTypesListAfter.size() > productTypesListBefore.size());
    }

    @Test
    void remove() {
        List<ProductType> productTypesListBefore = productTypeDao.getProductTypes();
        assertFalse(productTypesListBefore.isEmpty());
        assertTrue(productTypesListBefore.size() > 0);

        boolean condition = productTypeDao.remove(productTypeId);
        assertTrue(condition);

        List<ProductType> productTypesListAfter = productTypeDao.getProductTypes();
        assertFalse(productTypesListAfter.isEmpty());

        assertTrue(productTypesListBefore.size() > productTypesListAfter.size());
    }
}