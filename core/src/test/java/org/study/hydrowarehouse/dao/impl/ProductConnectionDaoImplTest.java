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
import org.study.hydrowarehouse.dao.ProductConnectionDao;
import org.study.hydrowarehouse.entity.ProductConnection;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class ProductConnectionDaoImplTest {

    @Autowired
    private ProductConnectionDao productConnectionDao;

    @Autowired
    private SessionFactory sessionFactory;

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
    @Transactional
    void updateProductConnection () {
        ProductConnection connection = new ProductConnection();
        connection.setSize("1/2 inch");

        Session session = sessionFactory.getCurrentSession();
        session.save(connection);
        session.flush();

        Integer id = connection.getProductConnectionId();

        connection.setSize("3/4 inch");
        boolean updated = productConnectionDao.updateProductConnection(connection);
        assertTrue(updated);

        ProductConnection updatedConnection = session.get(ProductConnection.class, id);
        assertEquals("3/4 inch", updatedConnection.getSize());
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
        assertTrue(connectionList.size() > 0);
        assertFalse(connectionList.isEmpty());
    }
}