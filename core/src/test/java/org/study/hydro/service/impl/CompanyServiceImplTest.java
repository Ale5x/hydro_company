package org.study.hydro.service.impl;

import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydro.dao.CompanyDao;
import org.study.hydro.entity.Company;
import org.study.hydro.entity.Dto.CompanyDto;
import org.study.hydro.service.CompanyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @InjectMocks
    private CompanyServiceImpl companyService;

    @Mock
    private CompanyDao companyDao;

    private CompanyDto companyDto = new CompanyDto();
    private List<Company> companyList = new ArrayList<>();
    private int returnExpected = 1;
    private int returnExpectedWrong = 0;


    @BeforeEach
    void setUp() {
        for (int i = 0; i < 10; i++) {
            Company company = new Company();
            company.setCompanyId(i);
            company.setName("Name #" + i);
            company.setAddress("Address street #" + i);

            companyList.add(company);
        }

    }

    @Test
    void create() {
        when(companyDao.save(any(Company.class))).thenReturn(returnExpected);
        boolean expected = true;
        boolean actual = companyService.create(companyDto);
        System.out.println("Test result -> " + actual);
        assertEquals(expected, actual);
    }

    @Test
    void createWrongTest() {
        when(companyDao.save(any(Company.class))).thenReturn(returnExpectedWrong);
        boolean expected = false;
        boolean actual = companyService.create(companyDto);
        System.out.println("Test result -> " + actual);
        assertEquals(expected, actual);
    }

    @Test
    void findByName() {
        String name = "Test name";
        when(companyDao.companiesByName(name)).thenReturn(companyList);
        List<CompanyDto> companyDtoList = companyService.findByName(name);
        System.out.println(companyDtoList);
        assertFalse(companyDtoList.isEmpty());
        assertTrue(companyDtoList.size() > 0);
    }

    @Test
    void findAll() {
        when(companyDao.companies(0, 0)).thenReturn(companyList);
        List<CompanyDto> companyDtoList = companyService.findAll(0, 0);
        System.out.println(companyDtoList);
        assertFalse(companyDtoList.isEmpty());
        assertTrue(companyDtoList.size() > 0);
    }

    @Test
    void findById() {
        int companyId = 1;
        when(companyDao.companyById(companyId)).thenReturn(Optional.of(companyList.get(companyId)));
        System.out.println("Resalt --- > ");
        companyList.stream().filter(x -> x.getCompanyId() == companyId).forEach(System.out::println);

        Optional<CompanyDto> company = companyService.findById(companyId);
        assertTrue(company.isPresent());
    }

    @Test
    void findByIdWrongTest() {
        int companyId = 1;
        when(companyDao.companyById(companyId)).thenReturn(Optional.empty());

        companyList.stream().filter(x -> x.getCompanyId() == companyId).forEach(System.out::println);

        Optional<CompanyDto> company = companyService.findById(companyId);
        System.out.println("Resalt --- > " + company.isPresent());
        assertFalse(company.isPresent());
    }
}