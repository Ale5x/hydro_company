package org.study.hydro.service.impl;

//import com.sun.xml.bind.v2.runtime.output.SAXOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydro.dao.UserCompanyDao;
import org.study.hydro.entity.UserCompany;
import org.study.hydro.entity.Dto.UserCompanyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCompanyServiceImplTest {

    @InjectMocks
    private UserCompanyServiceImpl companyService;

    @Mock
    private UserCompanyDao userCompanyDao;

    private UserCompanyDto userCompanyDto = new UserCompanyDto();
    private List<UserCompany> userCompanyList = new ArrayList<>();
    private int returnExpected = 1;
    private int returnExpectedWrong = 0;


    @BeforeEach
    void setUp() {
        for (int i = 0; i < 10; i++) {
            UserCompany userCompany = new UserCompany();
            userCompany.setUserCompanyId(i);
            userCompany.setName("Name #" + i);
            userCompany.setAddress("Address street #" + i);

            userCompanyList.add(userCompany);
        }

    }

    @Test
    void create() {
        when(userCompanyDao.save(any(UserCompany.class))).thenReturn(returnExpected);
        boolean expected = true;
        boolean actual = companyService.create(userCompanyDto);
        assertEquals(expected, actual);
    }

    @Test
    void createWrongTest() {
        when(userCompanyDao.save(any(UserCompany.class))).thenReturn(returnExpectedWrong);
        boolean expected = false;
        boolean actual = companyService.create(userCompanyDto);
        assertEquals(expected, actual);
    }

    @Test
    void findByName() {
        String name = "Test name";
        when(userCompanyDao.companiesByName(name)).thenReturn(userCompanyList);
        List<UserCompanyDto> userCompanyDtoList = companyService.findByName(name);
        assertFalse(userCompanyDtoList.isEmpty());
        assertTrue(userCompanyDtoList.size() > 0);
    }

    @Test
    void findAll() {
        when(userCompanyDao.companies(0, 0)).thenReturn(userCompanyList);
        List<UserCompanyDto> userCompanyDtoList = companyService.findAll(0, 0);
        assertFalse(userCompanyDtoList.isEmpty());
        assertTrue(userCompanyDtoList.size() > 0);
    }

    @Test
    void findById() {
        int companyId = 1;
        when(userCompanyDao.companyById(companyId)).thenReturn(Optional.of(userCompanyList.get(companyId)));
        userCompanyList.stream().filter(x -> x.getUserCompanyId() == companyId).forEach(System.out::println);

        Optional<UserCompanyDto> company = companyService.findById(companyId);
        assertTrue(company.isPresent());
    }

    @Test
    void findByIdWrongTest() {
        int companyId = 1;
        when(userCompanyDao.companyById(companyId)).thenReturn(Optional.empty());

        userCompanyList.stream().filter(x -> x.getUserCompanyId() == companyId).forEach(System.out::println);

        Optional<UserCompanyDto> company = companyService.findById(companyId);
        assertFalse(company.isPresent());
    }
}