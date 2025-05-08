package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.study.hydro.dao.ProductDao;
import org.study.hydro.entity.*;
import org.study.hydro.entity.Dto.*;
import org.study.hydro.service.ServiceMediator;
import org.study.hydro.utill.ImageStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private ServiceMediator serviceMediator;

    @Mock
    private ImageStorage imageStorage;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto productDto = new ProductDto();
    private Product product = new Product();
    private ProductType productType = new ProductType(1, "type");
    private ProductCompany productCompany = new ProductCompany(1, "company");
    private ProductConnection productConnection = new ProductConnection(1, "size");

    private Country country = new Country(1, "SW");
    private StorageRackDto storageRackDto = new StorageRackDto();
    private StorageRack storageRack = new StorageRack(1, "storage", new Shelf(1, "#39-1"));

    List<Product> productList = new ArrayList<>();
    private int productId = 1;
    private int limit = 0;
    private int offset = 0;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        storageRackDto.setName("name");
        storageRackDto.setStorageRackDtoId(1);
        storageRackDto.setShelfName("name");

        productDto.setCount(100);
        productDto.setStockKeepingUnit("#12-3");
        productDto.setFlowRate(50);
        productDto.setPressure(320);
        productDto.setPressureMax(400);
        productDto.setWeight(1.5);
        productDto.setAdditionalInformation("ta-ta");
        productDto.setProductTypeDto(new ProductTypeDto(1, "product type"));
        productDto.setPathHydraulicScheme("path");
        productDto.setProductCompanyDto(new ProductCompanyDto(1, "product company #1"));
        productDto.setProductConnectionDto(new ProductConnectionDto(1, "size type #3"));
        productDto.setImagesPaths(Arrays.asList("path1", "path 2"));
        productDto.setStorageRackDtoList(Arrays.asList(storageRackDto));
        productDto.setCountryDto(new CountryDto(1, "Country"));


        product.setProductConnection(productConnection);
        product.setProductCompany(productCompany);
        product.setProductType(productType);
        product.setStorageRackList(Arrays.asList(storageRack));
        product.setCountryProduct(country);
        product.setProductId(1);
        product.setPicturePath(List.of(new Picture("some path")));

        productList.add(product);
    }

    @Test
    void create() {
        when(productDao.create(any(Product.class))).thenReturn(productId);

        boolean condition = productService.create(productDto);

        assertTrue(condition);
        verify(productDao, times(1)).create(any(Product.class));
    }

    @Test
    void update() {
        when(productDao.update(any(Product.class))).thenReturn(true);
        productDto.setProductDtoId(1);
        boolean condition = productService.update(productDto);

        assertTrue(condition);
        verify(productDao, times(1)).update(any(Product.class));
    }

    @Test
    void remove() {
        when(productDao.getProductById(productId)).thenReturn(Optional.of(product));
        when(productDao.remove(productId)).thenReturn(true);

        when(imageStorage.removeFile(anyString())).thenReturn(true);  // Мокируем все вызовы removeFile

        ProductServiceImpl spyProductService = spy(productService);

        ReflectionTestUtils.invokeMethod(spyProductService, "removeAllProductFiles", product);

        boolean condition = spyProductService.remove(productId);

        assertTrue(condition);
        verify(productDao, times(1)).remove(productId);
    }

    @Test
    void findById() {
        when(productDao.getProductById(productId)).thenReturn(Optional.of(product));

        Optional<ProductDto> isProduct = productService.findById(productId);

        assertTrue(isProduct.isPresent());
        verify(productDao, times(1)).getProductById(productId);
    }

    @Test
    void findProductById() {
        when(productDao.getProductById(productId)).thenReturn(Optional.of(product));

        Optional<Product> isProduct = productService.findProductById(productId);

        assertTrue(isProduct.isPresent());
        verify(productDao, times(1)).getProductById(productId);
    }

    @Test
    void findAll() {
        when(productDao.getProductsList(offset, limit)).thenReturn(productList);

        List<ProductDto> list = productService.findAll(limit, offset);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsList(limit, offset);
    }

    @Test
    void findAllByPressure() {
        when(productDao.getProductsByPressure(offset, limit, 1)).thenReturn(productList);

        List<ProductDto> list = productService.findAllByPressure(limit, offset, 1);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsByPressure(limit, offset, 1);
    }

    @Test
    void findAllByFlowRate() {
        when(productDao.getProductsByFlowRate(offset, limit, 150)).thenReturn(productList);

        List<ProductDto> list = productService.findAllByFlowRate(limit, offset, 150);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsByFlowRate(limit, offset, 150);
    }

    @Test
    void findAllByType() {
        when(productDao.getProductsByTypeId(offset, limit, 1)).thenReturn(productList);
        ProductTypeDto type = new ProductTypeDto();
        type.setProductTypeId(1);
        List<ProductDto> list = productService.findAllByType(limit, offset, type);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsByTypeId(limit, offset, 1);

    }

    @Test
    void findAllByCompany() {
        when(productDao.getProductsByCompanyId(offset, limit, 1)).thenReturn(productList);
        ProductCompanyDto company = new ProductCompanyDto();
        company.setProductCompanyDtoId(1);
        List<ProductDto> list = productService.findAllByCompany(limit, offset, company);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsByCompanyId(limit, offset, 1);
    }

    @Test
    void findAllByStorageRack() {
        String storageName = "name";
        when(productDao.getProductsByStorageRackName(offset, limit, storageName)).thenReturn(productList);

        List<ProductDto> list = productService.findAllByStorageRack(limit, offset, storageRackDto);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsByStorageRackName(limit, offset, storageName);
    }
}