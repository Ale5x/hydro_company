package org.study.hydrowarehouse.dao.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.study.hydrowarehouse.configuration.DevelopmentConfig;
import org.study.hydrowarehouse.dao.ProductSkuStatusDao;
import org.study.hydrowarehouse.entity.ProductSkuStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DevelopmentConfig.class)
@ActiveProfiles("development")
@TestPropertySource(locations = "classpath:application-development.properties")
class ProductSkuStatusDaoImplTest {

    @Autowired
    private ProductSkuStatusDao skuStatusDao;
    private Integer id = 1;

    @Test
    void save() {
        ProductSkuStatus newStatus = new ProductSkuStatus();
        newStatus.setStatus("INACTIVE");
        int id = skuStatusDao.save(newStatus);

        Optional<ProductSkuStatus> found = skuStatusDao.findById(id);
        assertTrue(found.isPresent());
        assertEquals("INACTIVE", found.get().getStatus());
    }

    @Test
    void update() {
        Optional<ProductSkuStatus> optionalTestStatus = skuStatusDao.findById(id);
        assertTrue(optionalTestStatus.isPresent());

        ProductSkuStatus testStatus = optionalTestStatus.get();
        testStatus.setStatus("UPDATED");

        boolean updated = skuStatusDao.update(testStatus);

        assertTrue(updated);
        Optional<ProductSkuStatus> result = skuStatusDao.findById(testStatus.getProductSkuStatusId());
        assertTrue(result.isPresent());
        assertEquals("UPDATED", result.get().getStatus());
    }

    @Test
    void findAll() {
        List<ProductSkuStatus> all = skuStatusDao.findAll();
        assertFalse(all.isEmpty());
    }

    @Test
    void findByStatus() {
        Optional<ProductSkuStatus> testStatus = skuStatusDao.findById(1);
        assertTrue(testStatus.isPresent());
        String status = testStatus.get().getStatus();

        Optional<ProductSkuStatus> result = skuStatusDao.findByStatus(status);
        assertTrue(result.isPresent());
        assertTrue(result.get().getStatus().contentEquals(status));
    }

    @Test
    void findById() {
        Optional<ProductSkuStatus> result = skuStatusDao.findById(id);
        assertTrue(result.isPresent());
    }

    @Test
    void remove() {
        int saved = skuStatusDao.save(new ProductSkuStatus("test saving"));
        assertTrue(saved > 0);

        boolean removed = skuStatusDao.remove(saved);
        assertTrue(removed);

        Optional<ProductSkuStatus> result = skuStatusDao.findById(saved);
        assertFalse(result.isPresent());
    }
}