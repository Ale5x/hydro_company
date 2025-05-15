package org.study.hydro.dao.impl;

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
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.ProductDao;
import org.study.hydro.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class ProductDaoImplTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private SessionFactory sessionFactory;

    private int productId = 1;
    private int limit = 3;
    private int maxLimit = 100;
    private int offset = 0;

    private List<Picture> pictures = new ArrayList<>();

    @BeforeEach
    void init() {
        pictures.add(new Picture("path_product-1"));
        pictures.add(new Picture("path_product-1"));
        pictures.add(new Picture("path_product-1"));
    }

    @Test
    void create() {
        Product product = new Product(35, 320, 350, 3.5, "path scheme",
                52, "stock #3", "description", pictures);

        product.setCountryProduct(new Country(1, "SW"));

        product.setProductType(new ProductType(1, "name"));
        product.setProductCompany(new ProductCompany(1, "name"));
        product.setProductConnection(new ProductConnection(1, "size"));

        List<Product> productListBefore = productDao.getProductsList(maxLimit, offset);

        assertFalse(productListBefore.isEmpty());

        int id = productDao.create(product);

        assertTrue(id > 0);

        List<Product> productListAfter = productDao.getProductsList(maxLimit, offset);

        assertFalse(productListAfter.isEmpty());
        assertTrue(productListAfter.size() > productListBefore.size());
    }

    @Test
    @Transactional
    void update() {
        Product product = new Product();
        product.setStockKeepingUnit("OldProduct");
        product.setCount(100);
        product.setAdditionalInformation("Category1");

        Session session = sessionFactory.getCurrentSession();
        session.save(product);
        session.flush();

        Integer id = product.getProductId();

        product.setStockKeepingUnit("NewProduct");
        product.setCount(150);
        boolean result = productDao.update(product);

        assertTrue(result);

        Product updatedProduct = session.get(Product.class, id);
        assertEquals("NewProduct", updatedProduct.getStockKeepingUnit());
        assertEquals(150, updatedProduct.getCount());
    }

    @Test
    void remove() {
        List<Product> productListBefore = productDao.getProductsList(maxLimit, offset);

        assertFalse(productListBefore.isEmpty());
        boolean condition = productDao.remove(productId);
        assertTrue(condition);

        List<Product> productListAfter = productDao.getProductsList(maxLimit, offset);
        assertFalse(productListAfter.isEmpty());
        assertTrue(productListBefore.size() > productListAfter.size());

        Optional<Product> isProduct = productDao.getProductById(productId);
        assertTrue(isProduct.isEmpty());
    }

    @Test
    void getProductById() {
        Optional<Product> product = productDao.getProductById(productId);

        assertTrue(product.isPresent());
        assertEquals(product.get().getProductId(), productId);
    }

    @Test
    void getProductsList() {
        List<Product> productList = productDao.getProductsList(limit, offset);

        assertFalse(productList.isEmpty());
        assertTrue(productList.size() == limit);
    }

    @Test
    void getProductsByCompany() {
        List<Product> productList = productDao.getProductsByCompanyId(maxLimit, offset, 1);

        assertNotNull(productList);
    }

    @Test
    void getProductsByFlowRate() {
        int flowRate = 200;
        List<Product> productList = productDao.getProductsByFlowRate(maxLimit, offset, flowRate);

        assertNotNull(productList);
    }


    @Test
    void getProductsByPressure() {
        int pressure = 185;
        List<Product> productList = productDao.getProductsByPressure(maxLimit, offset, pressure);
        assertNotNull(productList);
    }

    @Test
    void getProductsByType() {
        List<Product> productList = productDao.getProductsByTypeId(maxLimit, offset, 2);

        assertFalse(productList.isEmpty());
    }

    @Test
    void getProductsByStorageName() {
        String storageName = "storage_racks-name-1";

        List<Product> productList = productDao.getProductsByStorageRackName(maxLimit, offset, storageName);

        assertFalse(productList.isEmpty());
    }
}