package org.study.hydro.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.ProductConnectionDao;
import org.study.hydro.entity.ProductConnection;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class ProductConnectionDaoImplTest {

    @Autowired
    private ProductConnectionDao productConnectionDao;

    private int productConnectionId = 1;
    private String newSize = "Test";

    @BeforeEach
    void setUp() {
    }

    @Test
    void save() {
        List<ProductConnection> connectionListBefore = productConnectionDao.getListProductsConnection();
        assertFalse(connectionListBefore.isEmpty());

        ProductConnection productConnection = new ProductConnection("new connection type");
        int idNewRecord = productConnectionDao.save(productConnection);

        assertTrue(idNewRecord > 0);

        List<ProductConnection> connectionListAfter = productConnectionDao.getListProductsConnection();
        assertFalse(connectionListAfter.isEmpty());

        assertTrue(connectionListAfter.size() > connectionListBefore.size());
    }

    @Test
    void getProductConnectionById() {
        Optional<ProductConnection> productConnection = productConnectionDao
                .getProductConnectionById(productConnectionId);

        assertTrue(productConnection.isPresent());
    }

    @Test
    void getListProductsConnection() {
        List<ProductConnection> connectionList = productConnectionDao.getListProductsConnection();
        System.out.println(connectionList);
        assertTrue(connectionList.size() > 0);
        assertFalse(connectionList.isEmpty());
    }

    @Test
    void updateProductConnection() {
        Optional<ProductConnection> productConnectionBefore =
                productConnectionDao.getProductConnectionById(productConnectionId);
        assertTrue(productConnectionBefore.isPresent());

        ProductConnection productConnection = new ProductConnection(
                productConnectionBefore.get().getProductConnectionId(),
                productConnectionBefore.get().getSize()
        );

        productConnection.setSize(newSize);
        productConnectionDao.updateProductConnection(productConnection);

        Optional<ProductConnection> productConnectionAfter =
                productConnectionDao.getProductConnectionById(productConnectionId);
        assertTrue(productConnectionAfter.isPresent());

        assertEquals(productConnectionBefore.get().getSize(), productConnectionBefore.get().getSize());
        assertFalse(productConnectionAfter.get().getSize().equals(productConnectionBefore.get().getSize()));

    }
}