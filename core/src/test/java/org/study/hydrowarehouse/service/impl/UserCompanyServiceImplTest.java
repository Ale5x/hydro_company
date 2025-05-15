package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydrowarehouse.dao.UserCompanyDao;
import org.study.hydrowarehouse.entity.Country;
import org.study.hydrowarehouse.entity.Dto.CountryDto;
import org.study.hydrowarehouse.entity.UserCompany;
import org.study.hydrowarehouse.entity.Dto.UserCompanyDto;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.service.CountryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCompanyServiceImplTest {

    @InjectMocks
    private UserCompanyServiceImpl userCompanyService;

    @Mock
    private UserCompanyDao userCompanyDao;
    @Mock
    private CountryService countryService;

    private UserCompanyDto userCompanyDto = new UserCompanyDto();
    private CountryDto countryDto = new CountryDto(1, "BOSH");
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
        when(countryService.findById(1)).thenReturn(Optional.of(countryDto));
        boolean expected = true;
        boolean actual = userCompanyService.create(userCompanyDto);
        assertEquals(expected, actual);
    }

    @Test
    void testUpdate_successful() throws CoreException {
        UserCompanyDto dto = new UserCompanyDto();
        dto.setCompanyDtoId(1);
        dto.setName("Updated Company");
        dto.setAddress("Updated Address");

        CountryDto countryDto = new CountryDto();
        countryDto.setCountryId(1);
        countryDto.setName("Country");

        dto.setCountryDto(countryDto);

        UserCompany existingCompany = new UserCompany();
        existingCompany.setUserCompanyId(1);
        existingCompany.setName("Old Company");
        existingCompany.setAddress("Old Address");
        existingCompany.setCountry(new Country(1, "Country"));

        Country newCountry = new Country(1, "Country");

        when(userCompanyDao.companyById(1)).thenReturn(Optional.of(existingCompany));
        when(countryService.findCountryById(1)).thenReturn(Optional.of(newCountry));
        when(userCompanyDao.update(existingCompany)).thenReturn(true);

        boolean result = userCompanyService.update(dto);

        assertTrue(result);
        assertEquals("Updated Company", existingCompany.getName());
        assertEquals("Updated Address", existingCompany.getAddress());
        assertEquals(newCountry, existingCompany.getCountry());
        verify(userCompanyDao).update(existingCompany);
    }

    @Test
    void testUpdate_userCompanyNotFound_throwsException() {
        UserCompanyDto dto = new UserCompanyDto();
        dto.setCompanyDtoId(404);
        dto.setName("Company");

        when(userCompanyDao.companyById(404)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> userCompanyService.update(dto));

        assertTrue(ex.getMessage().contains("User company not found"));
    }

    @Test
    void testUpdate_countryNotFound_throwsException() {
        UserCompanyDto dto = new UserCompanyDto();
        dto.setCompanyDtoId(1);
        dto.setName("Company");

        CountryDto countryDto = new CountryDto();
        countryDto.setCountryId(404);
        countryDto.setName("Unknown Country");

        dto.setCountryDto(countryDto);

        UserCompany existingCompany = new UserCompany();
        existingCompany.setUserCompanyId(1);
        existingCompany.setName("Existing Company");

        when(userCompanyDao.companyById(1)).thenReturn(Optional.of(existingCompany));
        when(countryService.findCountryById(404)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> userCompanyService.update(dto));

        assertTrue(ex.getMessage().contains("Country not found"));
    }

    @Test
    void createWrongTest() {
        when(userCompanyDao.save(any(UserCompany.class))).thenReturn(returnExpectedWrong);
        boolean expected = false;
        boolean actual = userCompanyService.create(userCompanyDto);
        assertEquals(expected, actual);
    }

    @Test
    void findByName() {
        String name = "Test name";
        when(userCompanyDao.companiesByName(name)).thenReturn(userCompanyList);
        List<UserCompanyDto> userCompanyDtoList = userCompanyService.findByName(name);
        assertFalse(userCompanyDtoList.isEmpty());
        assertTrue(userCompanyDtoList.size() > 0);
    }

    @Test
    void findAll() {
        when(userCompanyDao.companies(0, 0)).thenReturn(userCompanyList);
        List<UserCompanyDto> userCompanyDtoList = userCompanyService.findAll(0, 0);
        assertFalse(userCompanyDtoList.isEmpty());
        assertTrue(userCompanyDtoList.size() > 0);
    }

    @Test
    void findById() {
        int companyId = 1;
        when(userCompanyDao.companyById(companyId)).thenReturn(Optional.of(userCompanyList.get(companyId)));
        userCompanyList.stream().filter(x -> x.getUserCompanyId() == companyId).forEach(System.out::println);

        Optional<UserCompanyDto> company = userCompanyService.findById(companyId);
        assertTrue(company.isPresent());
    }

    @Test
    void findByIdWrongTest() {
        int companyId = 1;
        when(userCompanyDao.companyById(companyId)).thenReturn(Optional.empty());

        userCompanyList.stream().filter(x -> x.getUserCompanyId() == companyId).forEach(System.out::println);

        Optional<UserCompanyDto> company = userCompanyService.findById(companyId);
        assertFalse(company.isPresent());
    }
}