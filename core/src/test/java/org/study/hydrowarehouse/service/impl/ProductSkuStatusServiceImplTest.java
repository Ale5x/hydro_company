package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydrowarehouse.dao.ProductSkuStatusDao;
import org.study.hydrowarehouse.entity.Dto.ProductSkuStatusDto;
import org.study.hydrowarehouse.entity.ProductSkuStatus;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductSkuStatusServiceImplTest {

    @Mock
    private ProductSkuStatusDao skuStatusDao;

    @InjectMocks
    private ProductSkuStatusServiceImpl skuStatusService;

    private ProductSkuStatusDto dto;
    private ProductSkuStatus entity;

    @BeforeEach
    void setUp() {
        dto = new ProductSkuStatusDto();
        dto.setProductSkuStatusDtoId(1);
        dto.setStatus("ACTIVE");

        entity = new ProductSkuStatus();
        entity.setProductSkuStatusId(1);
        entity.setStatus("ACTIVE");
    }

    @Test
    void save_ShouldReturnTrue_WhenSavedSuccessfully() throws CoreException {
        when(skuStatusDao.save(any())).thenReturn(1);

        boolean result = skuStatusService.save(dto);

        assertTrue(result);
        verify(skuStatusDao).save(any(ProductSkuStatus.class));
    }

    @Test
    void update_ShouldReturnTrue_WhenStatusExists() throws CoreException {
        when(skuStatusDao.findById(1)).thenReturn(Optional.of(entity));
        when(skuStatusDao.update(entity)).thenReturn(true);

        boolean result = skuStatusService.update(dto);

        assertTrue(result);
        assertEquals("ACTIVE", entity.getStatus());
        verify(skuStatusDao).update(entity);
    }

    @Test
    void update_ShouldThrowCoreException_WhenStatusNotFound() {
        when(skuStatusDao.findById(1)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> skuStatusService.update(dto));
        assertTrue(ex.getMessage().contains("Sku status not found"));
    }

    @Test
    void remove_ShouldReturnTrue_WhenDeletedSuccessfully() throws CoreException {
        when(skuStatusDao.remove(1)).thenReturn(true);

        boolean result = skuStatusService.remove(1);

        assertTrue(result);
        verify(skuStatusDao).remove(1);
    }

    @Test
    void remove_ShouldThrowCoreException_WhenIdIsNull() {
        CoreException ex = assertThrows(CoreException.class, () -> skuStatusService.remove(null));
        assertTrue(ex.getMessage().contains("Id is null"));
    }

    @Test
    void findAll_ShouldReturnListOfDtos() throws CoreException {
        List<ProductSkuStatus> entities = List.of(entity);
        when(skuStatusDao.findAll()).thenReturn(entities);

        List<ProductSkuStatusDto> result = skuStatusService.findAll();

        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
    }

    @Test
    void findByStatus_ShouldReturnDto_WhenFound() throws CoreException {
        when(skuStatusDao.findByStatus("ACTIVE")).thenReturn(Optional.of(entity));

        Optional<ProductSkuStatusDto> result = skuStatusService.findByStatus("ACTIVE");

        assertTrue(result.isPresent());
        assertEquals("ACTIVE", result.get().getStatus());
    }

    @Test
    void findByStatus_ShouldReturnEmpty_WhenNotFound() throws CoreException {
        when(skuStatusDao.findByStatus("INACTIVE")).thenReturn(Optional.empty());

        Optional<ProductSkuStatusDto> result = skuStatusService.findByStatus("INACTIVE");

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_ShouldReturnDto_WhenFound() throws CoreException {
        when(skuStatusDao.findById(1)).thenReturn(Optional.of(entity));

        Optional<ProductSkuStatusDto> result = skuStatusService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("ACTIVE", result.get().getStatus());
    }

    @Test
    void findById_ShouldThrowCoreException_WhenIdIsNull() {
        CoreException ex = assertThrows(CoreException.class, () -> skuStatusService.findById(null));
        assertTrue(ex.getMessage().contains("Id is null"));
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotFound() throws CoreException {
        when(skuStatusDao.findById(1)).thenReturn(Optional.empty());

        Optional<ProductSkuStatusDto> result = skuStatusService.findById(1);

        assertTrue(result.isEmpty());
    }
}