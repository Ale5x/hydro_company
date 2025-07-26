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
import org.study.hydrowarehouse.dao.ProductDao;
import org.study.hydrowarehouse.entity.*;

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

    private String inStockStatus = "In Stock";
    private String outOfStockStatus = "Out of Stock";

    @BeforeEach
    void init() {
        pictures.add(new Picture("path_product-1"));
        pictures.add(new Picture("path_product-1"));
        pictures.add(new Picture("path_product-1"));
    }

    @Test
    void create() {
        Product product = new Product(35, 320, 350, 35,
                5.2, 550, "stock #3", "description", pictures);

        product.setProductType(new ProductType(1, "name"));
        product.setProductCompany(new ProductCompany(1, "name"));
        product.setProductConnection(new ProductConnection(1, "size"));

        List<Product> productListBefore = productDao.getProductsList(maxLimit, offset, inStockStatus);

        assertFalse(productListBefore.isEmpty());

        int id = productDao.create(product);

        assertTrue(id > 0);

        List<Product> productListAfter = productDao.getProductsList(maxLimit, offset, inStockStatus);

        assertFalse(productListAfter.isEmpty());
        assertTrue(productListAfter.size() > productListBefore.size());
    }

    @Test
    @Transactional
    void update() {
        Product product = new Product();

        product.setCount(20);
        product.setFlowRate(50);
        product.setPressure(100);
        product.setPressureMax(150);
        product.setWeight(20.2);
        product.setPathHydraulicScheme("/schemes/");
        product.setAdditionalInformation("Test info: ");

        ProductType productType = new ProductType();
        productType.setProductTypeId(1);
        product.setProductType(productType);

        ProductCompany company = new ProductCompany();
        company.setProductCompanyId(1);
        product.setProductCompany(company);

        ProductConnection connection = new ProductConnection();
        connection.setProductConnectionId(1);
        product.setProductConnection(connection);

        product.setPicturePath(new ArrayList<>());

        product.setProductSkus(new ArrayList<>());



        Session session = sessionFactory.getCurrentSession();
        session.save(product);
        session.flush();

        Integer id = product.getProductId();

        product.setCount(150);
        boolean result = productDao.update(product);

        assertTrue(result);

        Product updatedProduct = session.get(Product.class, id);
        assertEquals(150, updatedProduct.getCount());
    }

    @Test
    void remove() {
        Optional<Product> isProductExist = productDao.getProductById(productId);
        assertTrue(isProductExist.isPresent());

        boolean condition = productDao.remove(productId);
        assertTrue(condition);

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
        List<Product> productListInStock = productDao.getProductsList(limit, offset, inStockStatus);
        assertFalse(productListInStock.isEmpty());
    }

    @Test
    void getProductsByCompany() {
        List<Product> productList = productDao.getProductsByCompanyId(maxLimit, offset, 1, inStockStatus);

        assertNotNull(productList);
    }

    @Test
    void getProductsByFlowRate() {
        int flowRate = 200;
        List<Product> productList = productDao.getProductsByFlowRate(maxLimit, offset, flowRate, inStockStatus);

        assertNotNull(productList);
    }


    @Test
    void getProductsByPressure() {
        int pressure = 300;
        List<Product> productList = productDao.getProductsByPressure(maxLimit, offset, pressure, inStockStatus);
        assertNotNull(productList);
    }

    @Test
    void getProductsByType() {
        List<Product> productListInStock = productDao.getProductsByTypeId(maxLimit, offset, 2, inStockStatus);
        List<Product> productListOutStock = productDao.getProductsByTypeId(maxLimit, offset, 2, outOfStockStatus);

        if (productListInStock.isEmpty()) {
            assertFalse(productListOutStock.isEmpty());
        } else {
            assertFalse(productListInStock.isEmpty());
        }
    }

    @Test
    void getProductsByStorageName() {
        String storageName = "Rack B";

        List<Product> productListInStock = productDao.getProductsByStorageRackName(maxLimit, offset, storageName, inStockStatus);
        List<Product> productListOutStock = productDao.getProductsByStorageRackName(maxLimit, offset, storageName, outOfStockStatus);

        if (productListInStock.isEmpty()) {
            assertFalse(productListOutStock.isEmpty());
        } else {
            assertFalse(productListInStock.isEmpty());
        }
    }
}