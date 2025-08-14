package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.study.hydrowarehouse.dao.ProductDao;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.*;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.mapping.DtoResolver;
import org.study.hydrowarehouse.mapping.EntityResolver;
import org.study.hydrowarehouse.utill.filestorage.ImageStorage;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductDao productDao;

    @Mock
    private EntityResolver entityResolver;

    @Mock
    private DtoResolver dtoResolver;

    @Mock
    private ImageStorage imageStorage;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto productDto = new ProductDto();

    private ProductConnectionDto productConDto =  new ProductConnectionDto();

    private ProductCompanyDto prCompanyDto = new ProductCompanyDto(1, "name");
    private Product product = new Product();

    private ProductSku productSku = new ProductSku();

    private Country country = new Country(1, "ARM");

    private Shelf shelf = new Shelf(1, "Shelf-5");
    private StorageRack storageRack = new StorageRack(1, "storage rack #1");
    private ProductType productType = new ProductType(1, "type");
    private ProductCompany productCompany = new ProductCompany(1, "company");
    private ProductConnection productConnection = new ProductConnection(1, "size");

    private String status = "Reserved";
    private String defaultStatus = "In active";

    private ProductSkuStatus skuStatus = new ProductSkuStatus(1, status);



    List<Product> productList = new ArrayList<>();
    private int productId = 1;
    private int limit = 0;
    private int offset = 0;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productService, "defaultStatus", defaultStatus);

        shelf.setStorageRack(storageRack);
        productCompany.setCompanyCountries(Set.of(country));

        productDto.setCount(100);
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
        product.setCount(100);
        product.setFlowRate(50);
        product.setPressure(320);
        product.setPressureMax(400);
        product.setWeight(1.5);
        product.setAdditionalInformation("ta-ta");
        product.setPathHydraulicScheme("path");
        product.setProductType(productType);

        productSku.setProductSkuId(11);
        productSku.setCountry(country);
        productSku.setCode("12345-F");
        productSku.setShelf(shelf);
        productSku.setStatus(skuStatus);

        product.setProductConnection(productConnection);
        product.setProductCompany(productCompany);
        product.setProductType(productType);
        product.setProductId(1);
        product.setPicturePath(List.of(new Picture("some path")));
        product.setProductSkus(List.of(productSku));

        productList.add(product);

        productConDto.setProductConnectionId(1);

        prCompanyDto.setCountries(List.of(new CountryDto(1, "name Country")));

    }

    @Test
    void create() {
        when(productDao.create(any(Product.class))).thenReturn(productId);

        boolean condition = productService.create(productDto);

        assertTrue(condition);
        verify(productDao, times(1)).create(any(Product.class));
    }

    @Test
    void testUpdate_successfulWithAllEntitiesChanged() throws CoreException {

        ProductDto dto = new ProductDto();
        dto.setProductDtoId(1);

        CountryDto countryDto =new CountryDto();
        countryDto.setCountryId(2);

        dto.setProductCompanyDto(new ProductCompanyDto(3, "NewCompany"));
        dto.setProductConnectionDto(new ProductConnectionDto(4, "1/2''"));
        dto.setProductTypeDto(new ProductTypeDto(5, "NewType"));

        Product existing = new Product();
        existing.setProductId(1);
        existing.setProductCompany(new ProductCompany(10, "OldCompany"));
        existing.setProductConnection(new ProductConnection(11, "3/4''"));
        existing.setProductType(new ProductType(12, "OldType"));

        when(productDao.getProductById(1)).thenReturn(Optional.of(existing));
        when(entityResolver.resolveProductCompany(any(ProductCompanyDto.class))).thenReturn(new ProductCompany(3, "NewCompany"));
        when(entityResolver.resolveProductType(any(ProductTypeDto.class))).thenReturn(new ProductType(5, "NewType"));
        when(productDao.update(any(Product.class))).thenReturn(true);

        boolean result = productService.update(dto);

        assertTrue(result);
        verify(productDao).update(any(Product.class));
    }

    @Test
    void testUpdate_shouldKeepSameEntitiesIfIdsEqual() throws CoreException {
        ProductDto dto = new ProductDto();
        dto.setProductDtoId(1);

        CountryDto countryDto = new CountryDto();
        countryDto.setCountryId(1);

        ProductCompanyDto companyDto = new ProductCompanyDto();
        companyDto.setProductCompanyDtoId(10);
        dto.setProductCompanyDto(companyDto);

        ProductConnectionDto connectionDto = new ProductConnectionDto();
        connectionDto.setProductConnectionId(11);
        dto.setProductConnectionDto(connectionDto);

        ProductTypeDto typeDto = new ProductTypeDto();
        typeDto.setProductTypeId(12);
        dto.setProductTypeDto(typeDto);

        Product existing = new Product();
        existing.setProductId(1);
        existing.setProductCompany(new ProductCompany(10, "OldCompany"));
        existing.setProductConnection(new ProductConnection(11, "3/4''"));
        existing.setProductType(new ProductType(12, "OldType"));

        when(productDao.getProductById(1)).thenReturn(Optional.of(existing));
        when(productDao.update(any(Product.class))).thenReturn(true);

        boolean result = productService.update(dto);

        assertTrue(result);

        verify(entityResolver, never()).resolveProductCompany(any(ProductCompanyDto.class));
        verify(entityResolver, never()).resolveProductConnection(any(ProductConnectionDto.class));
        verify(entityResolver, never()).resolveProductType(any(ProductTypeDto.class));

    }

    @Test
    void testUpdate_shouldKeepSameProductTypeIfIdsEqual() throws CoreException {
        ProductDto dto = new ProductDto();
        dto.setProductDtoId(1);

        ProductTypeDto typeDto = new ProductTypeDto();
        typeDto.setProductTypeId(12);
        typeDto.setName("Type");
        dto.setProductTypeDto(typeDto);

        ProductType existingType = new ProductType(12, "Type");

        Product existing = new Product();
        existing.setProductId(1);
        existing.setProductType(existingType);

        when(productDao.getProductById(1)).thenReturn(Optional.of(existing));
        when(productDao.update(any(Product.class))).thenReturn(true);

        boolean result = productService.update(dto);

        assertTrue(result);
        assertSame(existingType, existing.getProductType());

        verify(entityResolver, never()).resolveProductType(any(ProductTypeDto.class));
    }

    @Test
    void testUpdate_shouldKeepSameProductCompanyIfIdsEqual() throws CoreException {
        ProductDto dto = new ProductDto();
        dto.setProductDtoId(1);

        ProductCompanyDto companyDto = new ProductCompanyDto();
        companyDto.setProductCompanyDtoId(10);
        companyDto.setName("Company");
        dto.setProductCompanyDto(companyDto);

        ProductCompany existingCompany = new ProductCompany(10, "Company");

        Product existing = new Product();
        existing.setProductId(1);
        existing.setProductCompany(existingCompany);

        when(productDao.getProductById(1)).thenReturn(Optional.of(existing));
        when(productDao.update(any(Product.class))).thenReturn(true);

        boolean result = productService.update(dto);

        assertTrue(result);
        assertSame(existingCompany, existing.getProductCompany());

        verify(entityResolver, never()).resolveProductCompany(any(ProductCompanyDto.class));
    }

    @Test
    void testUpdate_shouldKeepSameProductConnectionIfIdsEqual() throws CoreException {
        ProductDto dto = new ProductDto();
        dto.setProductDtoId(1);

        ProductConnectionDto connectionDto = new ProductConnectionDto();
        connectionDto.setProductConnectionId(11);
        connectionDto.setSize("1/2\"");
        dto.setProductConnectionDto(connectionDto);

        ProductConnection existingConnection = new ProductConnection(11, "1/2\"");

        Product existing = new Product();
        existing.setProductId(1);
        existing.setProductConnection(existingConnection);

        when(productDao.getProductById(1)).thenReturn(Optional.of(existing));
        when(productDao.update(any(Product.class))).thenReturn(true);

        boolean result = productService.update(dto);

        assertTrue(result);
        assertSame(existingConnection, existing.getProductConnection());

        verify(entityResolver, never()).resolveProductConnection(any(ProductConnectionDto.class));
    }

    @Test
    void testUpdate_shouldThrowExceptionIfProductConnectionNotFound() throws CoreException {
        ProductDto dto = new ProductDto();
        dto.setProductDtoId(1);

        ProductConnectionDto connectionDto = new ProductConnectionDto();
        connectionDto.setProductConnectionId(11);
        connectionDto.setSize("1/2\"");
        dto.setProductConnectionDto(connectionDto);

        Product existing = new Product();
        existing.setProductId(1);

        when(productDao.getProductById(1)).thenReturn(Optional.of(existing));

        when(entityResolver.resolveProductConnection(any(ProductConnectionDto.class)))
                .thenThrow(new CoreException("Product Connection not found"));

        CoreException exception = assertThrows(CoreException.class, () -> {
            productService.update(dto);
        });

        assertTrue(exception.getMessage().contains("Product Connection not found"));
    }

    @Test
    void testUpdate_shouldThrowExceptionIfProductTypeNotFound() throws CoreException {
        ProductDto dto = new ProductDto();
        dto.setProductDtoId(1);

        ProductTypeDto typeDto = new ProductTypeDto();
        typeDto.setProductTypeId(5);
        typeDto.setName("Type");
        dto.setProductTypeDto(typeDto);

        Product existing = new Product();
        existing.setProductId(1);

        when(productDao.getProductById(1)).thenReturn(Optional.of(existing));

        when(entityResolver.resolveProductType(any(ProductTypeDto.class)))
                .thenThrow(new CoreException("Product Type not found"));

        CoreException exception = assertThrows(CoreException.class, () -> {
            productService.update(dto);
        });

        assertTrue(exception.getMessage().contains("Product Type not found"));
    }

    @Test
    void testUpdate_shouldThrowExceptionIfProductCompanyNotFound() throws CoreException {
        ProductDto dto = new ProductDto();
        dto.setProductDtoId(1);

        ProductCompanyDto companyDto = new ProductCompanyDto();
        companyDto.setProductCompanyDtoId(10);
        companyDto.setName("Company");
        dto.setProductCompanyDto(companyDto);

        Product existing = new Product();
        existing.setProductId(1);

        when(productDao.getProductById(1)).thenReturn(Optional.of(existing));

        when(entityResolver.resolveProductCompany(any(ProductCompanyDto.class)))
                .thenThrow(new CoreException("Product Company not found"));

        CoreException exception = assertThrows(CoreException.class, () -> {
            productService.update(dto);
        });

        assertTrue(exception.getMessage().contains("Product Company not found"));
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
        Product product = new Product();
        product.setProductId(productId);

        ProductDto mappedDto = new ProductDto();
        mappedDto.setProductDtoId(productId);

        when(productDao.getProductById(productId)).thenReturn(Optional.of(product));
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(mappedDto);
        when(dtoResolver.resolveProductTypeDto(any())).thenReturn(new ProductTypeDto());
        when(dtoResolver.resolveProductCompanyDto(any())).thenReturn(new ProductCompanyDto());
        when(dtoResolver.resolveProductConnectionDto(any())).thenReturn(new ProductConnectionDto());
        when(dtoResolver.resolveProductSkuDto(any())).thenReturn(new ProductSkuDto());

        Optional<ProductDto> result = productService.findById(productId);

        assertTrue(result.isPresent());
        assertEquals(productId, result.get().getProductDtoId());

        verify(productDao).getProductById(productId);
        verify(dtoResolver).resolveProductBasicFields(product);

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
        when(productDao.getProductsList(offset, limit, status)).thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> list = productService.findAll(limit, offset, status);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsList(limit, offset, status);
    }

    @Test
    void findAll_shouldUseDefaultStatus_whenStatusIsNull() throws CoreException {
        int offset = 0;
        int limit = 10;

        skuStatus.setStatus(defaultStatus);
        List<Product> mockProducts = List.of(product);
        when(productDao.getProductsList(limit, offset, defaultStatus)).thenReturn(mockProducts);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAll(offset, limit, null);

        assertNotNull(result);
        verify(productDao).getProductsList(limit, offset, defaultStatus);
    }

    @Test
    void findAll_shouldUseDefaultStatus_whenStatusIsEmpty() throws CoreException {
        int offset = 0;
        int limit = 10;
        skuStatus.setStatus(defaultStatus);

        List<Product> mockProducts = List.of(product);
        when(productDao.getProductsList(limit, offset, defaultStatus)).thenReturn(mockProducts);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAll(offset, limit, "   ");

        assertNotNull(result);
        verify(productDao).getProductsList(limit, offset, defaultStatus);
    }

    @Test
    void findAllByPressure() {
        when(productDao.getProductsByPressure(offset, limit, 1, status)).thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> list = productService.findAllByPressure(limit, offset, 1, status);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsByPressure(limit, offset, 1, status);
    }

    @Test
    void findAllByPressure_shouldUseDefaultStatus_whenStatusIsNull() throws CoreException {
        ReflectionTestUtils.setField(productService, "defaultStatus", "In active");

        int offset = 0;
        int limit = 10;
        int pressure = 320;
        String status = null;

        List<Product> products = List.of(product);
        when(productDao.getProductsByPressure(10, 0, 320, "In active")).thenReturn(products);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAllByPressure(offset, limit, pressure, status);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(productDao).getProductsByPressure(limit, offset, pressure, "In active");
    }

    @Test
    void findAllByPressure_shouldUseDefaultStatus_whenStatusIsEmpty() throws CoreException {
        ReflectionTestUtils.setField(productService, "defaultStatus", "In active");

        int offset = 0;
        int limit = 10;
        int pressure = 320;
        String status = "";

        List<Product> products = List.of(product);
        when(productDao.getProductsByPressure(limit, offset, pressure, "In active")).thenReturn(products);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAllByPressure(offset, limit, pressure, status);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(productDao).getProductsByPressure(limit, offset, pressure, "In active");
    }

    @Test
    void findAllByFlowRate() {
        when(productDao.getProductsByFlowRate(offset, limit, 150, status)).thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> list = productService.findAllByFlowRate(limit, offset, 150, status);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsByFlowRate(limit, offset, 150, status);
    }

    @Test
    void findAllByFlowRate_shouldUseDefaultStatus_whenStatusIsNull() throws CoreException {
        int offset = 0;
        int limit = 10;
        int flowRate = 150;

        when(productDao.getProductsByFlowRate(limit, offset, flowRate, defaultStatus)).thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAllByFlowRate(limit, offset, flowRate, null);

        assertNotNull(result);
        verify(productDao).getProductsByFlowRate(limit, offset, flowRate, defaultStatus);
    }

    @Test
    void findAllByFlowRate_shouldUseDefaultStatus_whenStatusIsEmpty() throws CoreException {
        int offset = 0;
        int limit = 10;
        int flowRate = 150;

        when(productDao.getProductsByFlowRate(limit, offset, flowRate, defaultStatus)).thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAllByFlowRate(limit, offset, flowRate, "");

        assertNotNull(result);
        verify(productDao).getProductsByFlowRate(limit, offset, flowRate, defaultStatus);
    }

    @Test
    void findAllByType() {
        when(productDao.getProductsByTypeId(offset, limit, 1, status)).thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        ProductTypeDto type = new ProductTypeDto();
        type.setProductTypeId(1);
        List<ProductDto> list = productService.findAllByType(limit, offset, type, status);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsByTypeId(limit, offset, 1, status);
    }
    @Test
    void findAllByType_shouldUseDefaultStatus_whenStatusIsNull() {
        int offset = 0;
        int limit = 10;
        ProductTypeDto typeDto = new ProductTypeDto(1, "Valve");

        when(productDao.getProductsByTypeId(limit, offset, typeDto.getProductTypeId(), defaultStatus))
                .thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAllByType(offset, limit, typeDto, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productDao).getProductsByTypeId(limit, offset, typeDto.getProductTypeId(), defaultStatus);
    }

    @Test
    void findAllByType_shouldUseDefaultStatus_whenStatusIsEmpty() {
        int offset = 0;
        int limit = 10;
        ProductTypeDto typeDto = new ProductTypeDto(1, "Valve");

        when(productDao.getProductsByTypeId(limit, offset, typeDto.getProductTypeId(), defaultStatus))
                .thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAllByType(offset, limit, typeDto, "");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productDao).getProductsByTypeId(limit, offset, typeDto.getProductTypeId(), defaultStatus);
    }

    @Test
    void findAllByType_shouldUseGivenStatus() {
        int offset = 0;
        int limit = 10;
        ProductTypeDto typeDto = new ProductTypeDto(1, "Valve");

        when(productDao.getProductsByTypeId(limit, offset, typeDto.getProductTypeId(), status))
                .thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAllByType(offset, limit, typeDto, status);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productDao).getProductsByTypeId(limit, offset, typeDto.getProductTypeId(), status);
    }

    @Test
    void findAllByCompany() {
        when(productDao.getProductsByCompanyId(offset, limit, 1, status)).thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> list = productService.findAllByCompany(limit, offset, prCompanyDto, status);

        assertNotNull(list);
        verify(productDao, times(1)).getProductsByCompanyId(limit, offset, 1, status);
    }

    @Test
    void findAllByCompany_shouldUseDefaultStatus_whenStatusIsNull() throws CoreException {
        int offset = 0;
        int limit = 10;

        when(productDao.getProductsByCompanyId(limit, offset, prCompanyDto.getProductCompanyDtoId(), defaultStatus))
                .thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAllByCompany(offset, limit, prCompanyDto, null);

        assertNotNull(result);
        verify(productDao).getProductsByCompanyId(limit, offset, prCompanyDto.getProductCompanyDtoId(), defaultStatus);
    }

    @Test
    void findAllByCompany_shouldUseDefaultStatus_whenStatusIsEmpty() throws CoreException {
        int offset = 0;
        int limit = 10;

        when(productDao.getProductsByCompanyId(limit, offset, prCompanyDto.getProductCompanyDtoId(), defaultStatus))
                .thenReturn(productList);
        when(dtoResolver.resolveProductBasicFields(product)).thenReturn(productDto);

        List<ProductDto> result = productService.findAllByCompany(offset, limit, prCompanyDto, "");

        assertNotNull(result);
        verify(productDao).getProductsByCompanyId(limit, offset, prCompanyDto.getProductCompanyDtoId(), defaultStatus);
    }
}