package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.study.hydro.dao.ProductTypeDao;
import org.study.hydro.entity.Dto.ProductTypeDto;
import org.study.hydro.entity.ProductType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductTypeServiceImplTest {

    @Mock
    private ProductTypeDao productTypeDao;

    @InjectMocks
    private ProductTypeServiceImpl productTypeService;

    private ProductType productType = new ProductType(1, "size");
    private ProductTypeDto productTypeDto = new ProductTypeDto();
    private List<ProductType> productTypeList = new ArrayList<>();
    private int id = 1;
    private String name = "size";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productTypeList.add(productType);

    }

    @Test
    void create() {
        when(productTypeDao.create(any(ProductType.class))).thenReturn(true);

        boolean condition = productTypeDao.create(productType);

        assertTrue(condition);
        verify(productTypeDao, times(1)).create(any(ProductType.class));
    }

    @Test
    void update() {
        when(productTypeDao.update(productType)).thenReturn(true);

        boolean condition = productTypeDao.update(productType);

        assertTrue(condition);
        verify(productTypeDao, times(1)).update(productType);
    }

    @Test
    void remove() {
        when(productTypeDao.getProductTypeById(id)).thenReturn(Optional.of(productType));
        when(productTypeDao.remove(id)).thenReturn(true);

        boolean condition = productTypeService.remove(id);

        assertTrue(condition);
        verify(productTypeDao, times(1)).remove(id);
    }

    @Test
    void findAll() {
        when(productTypeDao.getProductTypes()).thenReturn(productTypeList);

        List<ProductTypeDto> list = productTypeService.findAll();
        System.out.println(" ---> " + list);
        assertNotNull(list);
        verify(productTypeDao, times(1)).getProductTypes();
    }

    @Test
    void findById() {
        when(productTypeDao.getProductTypeById(id)).thenReturn(Optional.of(productType));

        Optional<ProductTypeDto> productType = productTypeService.findById(id);

        assertTrue(productType.isPresent());
        verify(productTypeDao, times(1)).getProductTypeById(id);
    }

    @Test
    void findByName() {
        when(productTypeDao.getProductTypeByName(name)).thenReturn(Optional.of(productType));

        Optional<ProductTypeDto> productType = productTypeService.findByName(name);

        assertTrue(productType.isPresent());
        verify(productTypeDao, times(1)).getProductTypeByName(name);
    }
}