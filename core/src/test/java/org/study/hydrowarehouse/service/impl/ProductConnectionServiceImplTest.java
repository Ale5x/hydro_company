package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydrowarehouse.dao.ProductConnectionDao;
import org.study.hydrowarehouse.entity.Dto.ProductConnectionDto;
import org.study.hydrowarehouse.entity.ProductConnection;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    void testUpdate_successful() throws CoreException {
        ProductConnectionDto dto = new ProductConnectionDto();
        dto.setProductConnectionId(1);
        dto.setSize("New Size");

        ProductConnection existing = new ProductConnection();
        existing.setProductConnectionId(1);
        existing.setSize("Old Size");

        when(productConnectionDao.getProductConnectionById(1)).thenReturn(Optional.of(existing));
        when(productConnectionDao.updateProductConnection(existing)).thenReturn(true);

        boolean result = productConnectionService.update(dto);

        assertTrue(result);
        assertEquals("New Size", existing.getSize());
        verify(productConnectionDao).updateProductConnection(existing);
    }

    @Test
    void testUpdate_productConnectionNotFound_throwsException() {
        ProductConnectionDto dto = new ProductConnectionDto();
        dto.setProductConnectionId(999);

        when(productConnectionDao.getProductConnectionById(999)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> productConnectionService.update(dto));

        assertTrue(ex.getMessage().contains("Product Connection not found"));
    }

    @Test
    void testUpdate_blankSize_shouldKeepOriginal() throws CoreException {
        ProductConnectionDto dto = new ProductConnectionDto();
        dto.setProductConnectionId(1);
        dto.setSize("  ");

        ProductConnection existing = new ProductConnection();
        existing.setProductConnectionId(1);
        existing.setSize("Original Size");

        when(productConnectionDao.getProductConnectionById(1)).thenReturn(Optional.of(existing));
        when(productConnectionDao.updateProductConnection(existing)).thenReturn(true);

        boolean result = productConnectionService.update(dto);

        assertTrue(result);
        assertEquals("Original Size", existing.getSize());
        verify(productConnectionDao).updateProductConnection(existing);
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