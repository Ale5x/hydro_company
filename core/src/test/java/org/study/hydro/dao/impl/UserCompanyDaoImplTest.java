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
import org.study.hydro.dao.UserCompanyDao;
import org.study.hydro.entity.UserCompany;
import org.study.hydro.entity.Country;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DevelopmentConfig.class)
@ActiveProfiles("development")
class UserCompanyDaoImplTest {

    @Autowired
    private UserCompanyDao userCompanyDao;

    @Autowired
    private SessionFactory sessionFactory;

    private int offset = 1;
    private int limit = 10;
    private Set<Country> countries = new HashSet<>();


    @BeforeEach
    void init () {
        countries.add(new Country("Norway"));
    }

    @Test
    void save() {
        UserCompany userCompany = new UserCompany("Company 77", "Street 11");
        int countBeforeOperation = userCompanyDao.companies(limit, offset).size();
        int companyId = userCompanyDao.save(userCompany);
        int countAfterOperation = userCompanyDao.companies(limit, offset).size();

        assertTrue(companyId >= 1);
        assertTrue(countAfterOperation > countBeforeOperation);
    }

    @Test
    @Transactional
    void update() {
        UserCompany company = new UserCompany();
        company.setName("OldName");

        Session session = sessionFactory.getCurrentSession();
        session.save(company);
        session.flush();

        Integer id = company.getUserCompanyId();

        company.setName("NewName");
        boolean result = userCompanyDao.update(company);

        assertTrue(result);

        UserCompany updated = session.get(UserCompany.class, id);
        assertEquals("NewName", updated.getName());
    }

    @Test
    void companies() {
        List<UserCompany> userCompanyList = userCompanyDao.companies(limit, offset);
        assertFalse(userCompanyList.isEmpty());
    }

    @Test
    void companyByName() {
        String name = "Company";
        List<UserCompany> userCompany = userCompanyDao.companiesByName(name);
        assertFalse(userCompany.isEmpty());
    }

    @Test
    void companyById() {
        int companyId = 1;
        Optional<UserCompany> company = userCompanyDao.companyById(companyId);
        assertTrue(company.isPresent());
    }
}