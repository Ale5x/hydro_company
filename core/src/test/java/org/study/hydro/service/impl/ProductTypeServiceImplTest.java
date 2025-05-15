package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydro.dao.ProductTypeDao;
import org.study.hydro.entity.Dto.ProductTypeDto;
import org.study.hydro.entity.ProductType;
import org.study.hydro.exception.CoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        when(productTypeDao.create(any(ProductType.class))).thenReturn(3);

        int id = productTypeDao.create(productType);

        assertTrue(id > 0);
        verify(productTypeDao, times(1)).create(any(ProductType.class));
    }

    @Test
    void testUpdate_successful() throws CoreException {
        ProductTypeDto dto = new ProductTypeDto();
        dto.setProductTypeId(1);
        dto.setName("Updated Type");

        ProductType existing = new ProductType();
        existing.setProductTypeId(1);
        existing.setName("Old Type");

        when(productTypeDao.getProductTypeById(1)).thenReturn(Optional.of(existing));
        when(productTypeDao.update(existing)).thenReturn(true);

        boolean result = productTypeService.update(dto);

        assertTrue(result);
        assertEquals("Updated Type", existing.getName());
        verify(productTypeDao).update(existing);
    }

    @Test
    void testUpdate_productTypeNotFound_throwsException() {
        ProductTypeDto dto = new ProductTypeDto();
        dto.setProductTypeId(404);

        when(productTypeDao.getProductTypeById(404)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> productTypeService.update(dto));

        assertTrue(ex.getMessage().contains("Product type not found"));
    }

    @Test
    void testUpdate_blankName_shouldKeepOriginal() throws CoreException {
        ProductTypeDto dto = new ProductTypeDto();
        dto.setProductTypeId(1);
        dto.setName(" ");

        ProductType existing = new ProductType();
        existing.setProductTypeId(1);
        existing.setName("Existing Name");

        when(productTypeDao.getProductTypeById(1)).thenReturn(Optional.of(existing));
        when(productTypeDao.update(existing)).thenReturn(true);

        boolean result = productTypeService.update(dto);

        assertTrue(result);
        assertEquals("Existing Name", existing.getName());
        verify(productTypeDao).update(existing);
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
        when(productTypeDao.getProductTypeByName(name)).thenReturn(List.of(productType));

        List<ProductTypeDto> productType = productTypeService.findByName(name);

        assertFalse(productType.isEmpty());
        verify(productTypeDao, times(1)).getProductTypeByName(name);
    }
}