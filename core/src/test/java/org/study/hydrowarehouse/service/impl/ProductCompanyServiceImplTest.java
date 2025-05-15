package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydrowarehouse.dao.ProductCompanyDao;
import org.study.hydrowarehouse.entity.Country;
import org.study.hydrowarehouse.entity.Dto.CountryDto;
import org.study.hydrowarehouse.entity.Dto.ProductCompanyDto;
import org.study.hydrowarehouse.entity.ProductCompany;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.service.ServiceMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCompanyServiceImplTest {

    @Mock
    private ProductCompanyDao productCompanyDao;
    @InjectMocks
    private ProductCompanyServiceImpl productCompanyService;

    @Mock
    private ServiceMediator serviceMediator;

    private int id = 1;
    private int limit = 10;
    private int offset = 1;
    private String name = "name";

    private List<ProductCompany> productCompanyList = new ArrayList<>();
    private List<ProductCompanyDto> dtoList = new ArrayList<>();
    private ProductCompanyDto prDto = new ProductCompanyDto();
    private ProductCompany productCompany = new ProductCompany(1, "name");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productCompanyList.add(productCompany);

        prDto.setProductCompanyDtoId(1);
        prDto.setName("name");

        dtoList.add(prDto);
    }

    @Test
    void create() {
        when(productCompanyDao.create(any(ProductCompany.class))).thenReturn(3);

        boolean condition = productCompanyService.create(prDto);

        assertTrue(condition);

        verify(productCompanyDao, times(1)).create(any(ProductCompany.class));
    }

    @Test
    void testUpdate_successful() throws CoreException {
        ProductCompanyDto dto = new ProductCompanyDto();
        dto.setProductCompanyDtoId(1);
        dto.setName("Updated Company");

        CountryDto countryDto = new CountryDto();
        countryDto.setCountryId(1);
        countryDto.setName("USA");
        dto.setCountries(List.of(countryDto));

        ProductCompany existingCompany = new ProductCompany();
        existingCompany.setProductCompanyId(1);
        existingCompany.setName("Old Company");

        Country country = new Country();
        country.setCountryId(1);
        country.setName("USA");

        when(productCompanyDao.getById(1)).thenReturn(Optional.of(existingCompany));
        when(serviceMediator.countryById(1)).thenReturn(Optional.of(country));
        when(productCompanyDao.update(any(ProductCompany.class))).thenReturn(true);

        boolean result = productCompanyService.update(dto);

        assertTrue(result);
        assertEquals("Updated Company", existingCompany.getName());
        assertEquals(Set.of(country), existingCompany.getCompanyCountries());

        verify(productCompanyDao).update(existingCompany);
    }

    @Test
    void testUpdate_companyNotFound_throwsException() {
        ProductCompanyDto dto = new ProductCompanyDto();
        dto.setProductCompanyDtoId(99);

        when(productCompanyDao.getById(99)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> productCompanyService.update(dto));

        assertTrue(ex.getMessage().contains("ProductCompany not found"));
    }

    @Test
    void testUpdate_countryNotFound_throwsException() {
        ProductCompanyDto dto = new ProductCompanyDto();
        dto.setProductCompanyDtoId(1);
        dto.setName("Valid Name");

        CountryDto countryDto = new CountryDto();
        countryDto.setCountryId(2);
        countryDto.setName("Unknown");
        dto.setCountries(List.of(countryDto));

        ProductCompany existingCompany = new ProductCompany();
        existingCompany.setProductCompanyId(1);
        existingCompany.setName("Old Name");

        when(productCompanyDao.getById(1)).thenReturn(Optional.of(existingCompany));
        when(serviceMediator.countryById(2)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> productCompanyService.update(dto));

        assertTrue(ex.getMessage().contains("Country not found"));
    }

    @Test
    void findAll() {
        when(productCompanyDao.getProductCompanies(offset, limit)).thenReturn(productCompanyList);

        List<ProductCompanyDto> list = productCompanyService.findAll(offset, limit);

        assertNotNull(list);
        verify(productCompanyDao, times(1)).getProductCompanies(offset, limit);
    }

    @Test
    void findAllByName() {
        when(productCompanyDao.getProductCompaniesByName(name)).thenReturn(productCompanyList);

        List<ProductCompanyDto> list = productCompanyService.findAllByName(name);

        assertNotNull(list);
        verify(productCompanyDao, times(1)).getProductCompaniesByName(name);
    }

    @Test
    void findById() {
        when(productCompanyDao.getById(id)).thenReturn(Optional.of(productCompany));

        Optional<ProductCompanyDto> productCompanyDto = productCompanyService.findById(id);
        assertTrue(productCompanyDto.isPresent());

        verify(productCompanyDao, times(1)).getById(id);
    }
}