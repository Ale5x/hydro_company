package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.study.hydro.dao.ProductConnectionDao;
import org.study.hydro.entity.Dto.ProductConnectionDto;
import org.study.hydro.entity.ProductConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductConnectionServiceImplTest {

    @Mock
    private ProductConnectionDao productConnectionDao;

    @InjectMocks
    private ProductConnectionServiceImpl productConnectionService;

    private ProductConnection prCon = new ProductConnection(1, "size");
    private ProductConnectionDto prConDto = new ProductConnectionDto();
    private List<ProductConnection> prConList = new ArrayList<>();
    private List<ProductConnectionDto> prConDtoList = new ArrayList<>();
    private int limit = 10;
    private int offset = 0;
    private int id = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        prConList.add(prCon);
    }

    @Test
    void create() {
        when(productConnectionDao.save(any(ProductConnection.class))).thenReturn(1);

        boolean condition = productConnectionService.create(prConDto);

        assertTrue(condition);
        verify(productConnectionDao, times(1)).save(any(ProductConnection.class));
    }

    @Test
    void update() {
        when(productConnectionDao.updateProductConnection(any(ProductConnection.class))).thenReturn(true);

        boolean condition = productConnectionService.update(prConDto);

        assertTrue(condition);
        verify(productConnectionDao, times(1)).updateProductConnection(any(ProductConnection.class));
    }

    @Test
    void findAll() {
        when(productConnectionDao.getListProductsConnection()).thenReturn(prConList);

        List<ProductConnectionDto> list = productConnectionService.findAll();

        assertNotNull(list);
        verify(productConnectionDao, times(1)).getListProductsConnection();
    }

    @Test
    void findById() {
        when(productConnectionDao.getProductConnectionById(id)).thenReturn(Optional.of(prCon));

        Optional<ProductConnectionDto> productConnection = productConnectionService.findById(id);

        assertTrue(productConnection.isPresent());
        verify(productConnectionDao, times(1)).getProductConnectionById(id);
    }
}