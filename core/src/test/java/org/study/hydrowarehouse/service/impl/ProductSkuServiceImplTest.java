package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydrowarehouse.dao.ProductSkuDao;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.*;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.service.ServiceMediator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSkuServiceImplTest {

    @Mock
    private ProductSkuDao skuDao;

    @Mock
    private ServiceMediator serviceMediator;

    @InjectMocks
    private ProductSkuServiceImpl skuService;

    private ProductSkuDto skuDto;
    private ProductSku productSku;
    private Product product;
    private Country country;
    private Shelf shelf;

    private StorageRack storageRack;
    private ProductSkuStatus status;

    @BeforeEach
    void setUp() {
        skuDto = new ProductSkuDto();
        skuDto.setProductSkuDtoId(1);
        skuDto.setCode("SKU-123");

        skuDto.setProductDto(new ProductDto(1));
        skuDto.setCountryDto(new CountryDto(2, "Germany"));
        skuDto.setShelfDto(new ShelfDto(3, "Top Shelf", new StorageRackDto(4, "Rack-01")));
        skuDto.setStatus(new ProductSkuStatusDto("In Stock"));


        productSku = new ProductSku();
        product = new Product(1);
        country = new Country(2, "Germany");
        storageRack = new StorageRack(1, "rack");
        shelf = new Shelf(3, "name");
        shelf.setStorageRack(storageRack);
        status = new ProductSkuStatus("In Stock");

        productSku.setProduct(product);
        productSku.setShelf(shelf);
        productSku.setStatus(status);
        productSku.setCountry(country);
    }

    @Test
    void save_ShouldReturnTrue_WhenSaveSuccessful() throws CoreException {
        when(serviceMediator.findCountryById(2)).thenReturn(Optional.of(country));
        when(serviceMediator.findProductById(1)).thenReturn(Optional.of(product));
        when(serviceMediator.findShelfById(3)).thenReturn(Optional.of(shelf));
        when(serviceMediator.findSkuStatusByStatus("In Stock")).thenReturn(Optional.of(status));
        when(skuDao.save(any(ProductSku.class))).thenReturn(1);

        boolean result = skuService.save(skuDto);
        assertTrue(result);
    }

    @Test
    void save_ShouldThrowCoreException_WhenStatusNotFound() {
        when(serviceMediator.findCountryById(2)).thenReturn(Optional.of(country));
        when(serviceMediator.findProductById(1)).thenReturn(Optional.of(product));
        when(serviceMediator.findShelfById(3)).thenReturn(Optional.of(shelf));
        when(serviceMediator.findSkuStatusByStatus("In Stock")).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> skuService.save(skuDto));
        assertTrue(ex.getMessage().contains("SKU status"));
    }

    @Test
    void update_ShouldReturnTrue_WhenUpdateSuccessful() throws CoreException {
        ProductSku existingSku = new ProductSku();
        existingSku.setProductSkuId(1);
        existingSku.setCode("OLD");

        when(skuDao.findById(1)).thenReturn(Optional.of(existingSku));
        when(serviceMediator.findCountryById(2)).thenReturn(Optional.of(country));
        when(serviceMediator.findProductById(1)).thenReturn(Optional.of(product));
        when(serviceMediator.findShelfById(3)).thenReturn(Optional.of(shelf));
        when(serviceMediator.findSkuStatusById(status.getProductSkuStatusId())).thenReturn(Optional.of(status));
        when(skuDao.update(any(ProductSku.class))).thenReturn(true);

        boolean updated = skuService.update(skuDto);
        assertTrue(updated);
    }

    @Test
    void update_ShouldThrowException_WhenSkuNotFound() {
        when(skuDao.findById(1)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class,
                () -> skuService.update(skuDto));

        assertTrue(ex.getMessage().contains("1"));
    }

    @Test
    void update_ShouldThrowCoreException_WhenSkuNotFound() {
        when(skuDao.findById(1)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> skuService.update(skuDto));
        assertTrue(ex.getMessage().contains("SKU not found"));
    }

    @Test
    void updateStatus_ShouldReturnTrue_WhenStatusFound() throws CoreException {
        ProductSkuStatus newStatus = new ProductSkuStatus();
        newStatus.setStatus("RESERVED");

        ProductSku existingSku = new ProductSku();
        existingSku.setProductSkuId(1);

        when(skuDao.findById(1)).thenReturn(Optional.of(existingSku));
        when(serviceMediator.findSkuStatusByStatus("RESERVED")).thenReturn(Optional.of(newStatus));
        when(skuDao.update(existingSku)).thenReturn(true);

        boolean result = skuService.updateStatus(skuDto, "RESERVED");

        assertTrue(result);
        verify(skuDao).update(existingSku);
    }

    @Test
    void update_ShouldThrowCoreException_WhenStatusNotFound() {
        when(skuDao.findById(1)).thenReturn(Optional.of(productSku));
        skuDto.setStatus(new ProductSkuStatusDto(5, "new status"));
        when(serviceMediator.findCountryById(anyInt())).thenReturn(Optional.of(country));
        when(serviceMediator.findProductById(anyInt())).thenReturn(Optional.of(product));
        when(serviceMediator.findShelfById(anyInt())).thenReturn(Optional.of(shelf));

        CoreException ex = assertThrows(CoreException.class, () -> skuService.update(skuDto));

        assertTrue(ex.getMessage().contains("Product SKU not found."));
    }


    @Test
    void updateStatus_ShouldThrowCoreException_WhenSkuNotFound() {
        when(skuDao.findById(1)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> skuService.updateStatus(skuDto, "In Stock"));
        assertTrue(ex.getMessage().contains("SKU not found"));
    }

    @Test
    void updateStatus_ShouldThrowCoreException_WhenStatusNotFound() {
        when(skuDao.findById(1)).thenReturn(Optional.of(productSku));
        when(serviceMediator.findSkuStatusByStatus("In Stock")).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> skuService.updateStatus(skuDto, "In Stock"));
        assertTrue(ex.getMessage().contains("SKU status not found"));
    }

    @Test
    void remove_ShouldReturnTrue_WhenDeleted() throws CoreException {
        when(skuDao.remove(1)).thenReturn(true);

        boolean result = skuService.remove(1);

        assertTrue(result);
        verify(skuDao).remove(1);
    }

    @Test
    void remove_ShouldThrowCoreException_WhenIdIsNull() {
        CoreException ex = assertThrows(CoreException.class, () -> skuService.remove(null));
        assertTrue(ex.getMessage().contains("Id is null"));
    }

    @Test
    void findById_ShouldReturnDto_WhenFound() throws CoreException {
        productSku.setProductSkuId(1);
        productSku.setCode("SKU-123");
        productSku.setProduct(product);
        productSku.setCountry(country);
        productSku.setShelf(shelf);
        productSku.setStatus(status);

        when(skuDao.findById(1)).thenReturn(Optional.of(productSku));

        Optional<ProductSkuDto> result = skuService.findById(1);
        assertTrue(result.isPresent());
        assertEquals("SKU-123", result.get().getCode());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotFound() throws CoreException {
        when(skuDao.findById(1)).thenReturn(Optional.empty());

        Optional<ProductSkuDto> result = skuService.findById(1);
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_ShouldReturnSku_WhenExists() throws CoreException {
        ProductSku sku = new ProductSku();
        sku.setProductSkuId(1);
        sku.setCode("SKU-001");
        sku.setProduct(product);
        sku.setCountry(country);
        sku.setStatus(status);
        sku.setShelf(shelf);

        when(skuDao.findById(1)).thenReturn(Optional.of(sku));

        Optional<ProductSkuDto> result = skuService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("SKU-001", result.get().getCode());
    }

    @Test
    void findByCode_ShouldReturnList_WhenFound() throws CoreException {
        when(skuDao.findByCode("SKU-123")).thenReturn(List.of(productSku));
        List<ProductSkuDto> result = skuService.findByCode("SKU-123");
        assertEquals(1, result.size());
    }

    @Test
    void findAllByStatus_ShouldReturnList_WhenStatusExists() throws CoreException {
        when(serviceMediator.findSkuStatusByStatus("In Stock")).thenReturn(Optional.of(status));
        when(skuDao.findAllByStatus(10, 0, status)).thenReturn(List.of(productSku));

        List<ProductSkuDto> result = skuService.findAllByStatus(10, 0, "In Stock");
        assertEquals(1, result.size());
    }

    @Test
    void findAllByStatus_ShouldThrowCoreException_WhenStatusNotFound() {
        when(serviceMediator.findSkuStatusByStatus("Unknown")).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> skuService.findAllByStatus(10, 0, "Unknown"));

        assertTrue(ex.getMessage().contains("Invalid SKU status:"));
    }

    @Test
    void findAllByProduct_ShouldReturnList_WhenProductExists() throws CoreException {
        ProductDto productDto = new ProductDto(1);
        when(skuDao.findAllByProduct(10, 0, new Product(1))).thenReturn(List.of(productSku));

        List<ProductSkuDto> result = skuService.findAllByProduct(10, 0, productDto);
        assertEquals(1, result.size());
    }
}