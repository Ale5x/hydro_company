package org.study.hydro.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.CompanyDao;
import org.study.hydro.entity.Company;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DevelopmentConfig.class)
@ActiveProfiles("development")
class CompanyDaoImplTest {

    @Autowired
    private CompanyDao companyDao;

    private int offset = 1;
    private int limit = 10;


    @Test
    void save() {
        Company company = new Company("Company 77", "CA");
        int countBeforeOperation = companyDao.companies(limit, offset).size();
        int companyId = companyDao.save(company);
        int countAfterOperation = companyDao.companies(limit, offset).size();
        assertTrue(companyId >= 1);
        assertTrue(countAfterOperation > countBeforeOperation);
    }

    @Test
    void companies() {
        List<Company> companyList = companyDao.companies(limit, offset);
        assertFalse(companyList.isEmpty());
    }

    @Test
    void companyByName() {
        String name = "Company";
        List<Company> company = companyDao.companiesByName(name);
        assertFalse(company.isEmpty());
    }

    @Test
    void companyById() {
        int companyId = 1;
        Optional<Company> company = companyDao.companyById(companyId);
        assertTrue(company.isPresent());
    }
}