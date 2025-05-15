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
import org.study.hydrowarehouse.dao.ProductCompanyDao;
import org.study.hydrowarehouse.entity.ProductCompany;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class ProductCompanyDaoImplTest {

    @Autowired
    private ProductCompanyDao productCompanyDao;

    @Autowired
    private SessionFactory sessionFactory;

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
        int id = productCompanyDao.create(productCompany);

        assertTrue(id > 0);

        List<ProductCompany> productCompanyListAfter = productCompanyDao.getProductCompanies(offset, limit);

        assertFalse(productCompanyListAfter.isEmpty());
        assertTrue(productCompanyListAfter.size() > productCompanyListBefore.size());

    }

    @Test
    @Transactional
    public void update () {
        ProductCompany company = new ProductCompany();
        company.setName("Original Name");

        Session session = sessionFactory.getCurrentSession();
        session.save(company);
        session.flush();

        Integer id = company.getProductCompanyId();

        company.setName("Updated Name");

        boolean result = productCompanyDao.update(company);
        assertTrue(result);

        ProductCompany updatedCompany = session.get(ProductCompany.class, id);
        assertEquals("Updated Name", updatedCompany.getName());
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
    void getProductCompaniesByName() {
        Optional<ProductCompany> productCompany = productCompanyDao.getById(productCompanyId);
        assertTrue(productCompany.isPresent());

        List<ProductCompany> productCompanyList = productCompanyDao.getProductCompaniesByName(productCompany.get().getName());
        assertFalse(productCompanyList.isEmpty());
        assertTrue(productCompanyList.size() > 0);
    }
}