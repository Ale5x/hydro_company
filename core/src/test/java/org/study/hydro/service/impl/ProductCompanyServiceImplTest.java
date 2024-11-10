package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.study.hydro.dao.ProductCompanyDao;
import org.study.hydro.entity.Dto.ProductCompanyDto;
import org.study.hydro.entity.ProductCompany;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductCompanyServiceImplTest {

    @Mock
    private ProductCompanyDao productCompanyDao;
    @InjectMocks
    private ProductCompanyServiceImpl productCompanyService;

    private int id = 1;
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
        when(productCompanyDao.create(any(ProductCompany.class))).thenReturn(true);

        boolean condition = productCompanyService.create(prDto);

        assertTrue(condition);

        verify(productCompanyDao, times(1)).create(any(ProductCompany.class));
    }

    @Test
    void update() {
        when(productCompanyDao.update(any(ProductCompany.class))).thenReturn(true);

        boolean condition = productCompanyService.update(prDto);

        assertTrue(condition);
        verify(productCompanyDao, times(1)).update(productCompany);
    }

    @Test
    void findAll() {
        when(productCompanyDao.getProductCompanies()).thenReturn(productCompanyList);

        List<ProductCompanyDto> list = productCompanyService.findAll();

        assertNotNull(list);
        verify(productCompanyDao, times(1)).getProductCompanies();
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