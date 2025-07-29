package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.configuration.DevelopmentConfig;
import org.study.hydrowarehouse.dao.ProductTypeDao;
import org.study.hydrowarehouse.entity.ProductType;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class ProductTypeDaoImplTest {

    @Autowired
    private ProductTypeDao productTypeDao;

    @Autowired
    private SessionFactory sessionFactory;


    private int productTypeId = 1;
    private ProductType productType = new ProductType(111, "Test type");

    @BeforeEach
    void setUp() {
    }

    @Test
    void create() {
        List<ProductType> productTypesListBefore = productTypeDao.getProductTypes();
        assertFalse(productTypesListBefore.isEmpty());

        int id = productTypeDao.create(productType);
        assertTrue(id > 0);

        List<ProductType> productTypesListAfter = productTypeDao.getProductTypes();
        assertFalse(productTypesListAfter.isEmpty());

        assertTrue(productTypesListAfter.size() > productTypesListBefore.size());
    }

    @Test
    @Transactional
    void updateProductType() {
        ProductType type = new ProductType();
        type.setName("Original");

        Session session = sessionFactory.getCurrentSession();
        session.save(type);
        session.flush();

        Integer id = type.getProductTypeId();

        type.setName("Updated");
        boolean updated = productTypeDao.update(type);

        assertTrue(updated);

        ProductType updatedType = session.get(ProductType.class, id);
        assertEquals("Updated", updatedType.getName());
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

        List<ProductType> prTypeByName = productTypeDao.getProductTypeByName(productTypeOptional.get().getName());
        assertFalse(prTypeByName.isEmpty());
    }

    @Test
    void getProductTypeByPartName() {
        Optional<ProductType> productTypeOptional = productTypeDao.getProductTypeById(productTypeId);
        assertTrue(productTypeOptional.isPresent());

        String fullName = productTypeOptional.get().getName();
        assertFalse(fullName.isBlank());

        String partName = fullName.substring(0, Math.min(3, fullName.length()));

        List<ProductType> prTypeByName = productTypeDao.getProductTypeByName(partName);
        assertFalse(prTypeByName.isEmpty());
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