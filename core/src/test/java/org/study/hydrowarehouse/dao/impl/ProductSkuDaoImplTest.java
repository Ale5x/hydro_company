package org.study.hydrowarehouse.dao.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.study.hydrowarehouse.configuration.DevelopmentConfig;
import org.study.hydrowarehouse.dao.ProductSkuDao;
import org.study.hydrowarehouse.entity.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DevelopmentConfig.class)
@ActiveProfiles("development")
@TestPropertySource(locations = "classpath:application-development.properties")
class ProductSkuDaoImplTest {

    @Autowired
    private ProductSkuDao productSkuDao;

    @Autowired
    private SessionFactory sessionFactory;
    private Product product = new Product();
    private Country country = new Country();
    private Shelf shelf = new Shelf();
    private ProductSkuStatus status = new ProductSkuStatus();

    private int limit = 5;
    private int offset = 0;


    @BeforeEach
    void setUp() {
        product.setProductId(1);
        country.setCountryId(1);
        shelf.setShelfId(1);
        status.setProductSkuStatusId(1);
        status.setStatus("In Stock");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        List<ProductSku> before = productSkuDao.findByCode("SKU-TEST-001");
        assertTrue(before.isEmpty());

        ProductSku sku = new ProductSku();
        sku.setCode("SKU-TEST-001");
        sku.setStatus(status);
        sku.setProduct(product);
        sku.setCountry(country);
        sku.setShelf(shelf);

        int id = productSkuDao.save(sku);
        assertTrue(id > 0);

        List<ProductSku> after = productSkuDao.findByCode("SKU-TEST-001");
        assertEquals(1, after.size());
        assertEquals("SKU-TEST-001", after.get(0).getCode());
    }

    @Test
    void update() {
        int id = 1;
        Optional<ProductSku> optionalSku = productSkuDao.findById(id);

        assertTrue(optionalSku.isPresent());

        ProductSku sku = optionalSku.get();

        sku.setCode("SKU-UPDATED");
        boolean updated = productSkuDao.update(sku);

        assertTrue(updated);

        Optional<ProductSku> found = productSkuDao.findById(id);
        assertTrue(found.isPresent());
        assertEquals("SKU-UPDATED", found.get().getCode());
    }

    @Test
    void remove() {
        int id = 1;
        boolean deleted = productSkuDao.remove(id);
        assertTrue(deleted);

        Optional<ProductSku> found = productSkuDao.findById(id);
        assertTrue(found.isEmpty());
    }

    @Test
    void findById() {
        int id = 1;

        Optional<ProductSku> found = productSkuDao.findById(id);
        assertTrue(found.isPresent());
    }

    @Test
    void findByCode() {
        int id = 1;
        Optional<ProductSku> optionalSku = productSkuDao.findById(id);

        assertTrue(optionalSku.isPresent());

        List<ProductSku> productSkuList = productSkuDao.findByCode(optionalSku.get().getCode());

        assertTrue(productSkuList.size() > 0);
        assertFalse(productSkuList.isEmpty());
    }

    @Test
    void findAllByStatus() {
        List<ProductSku> productSkuList = productSkuDao.findAllByStatus(limit, offset, status);

        assertTrue(productSkuList.size() > 0);
        assertFalse(productSkuList.isEmpty());
    }

    @Test
    void findAllByProduct() {
        List<ProductSku> productSkuList = productSkuDao.findAllByProduct(limit, offset, product);

        assertTrue(productSkuList.size() > 0);
        assertFalse(productSkuList.isEmpty());
    }
}