package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ProductSkuDao;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.*;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.ProductSkuService;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing {@link ProductSku} entities.
 * <p>
 * This service provides functionality to save, update, retrieve and remove Product SKU records,
 * as well as to resolve and map related entities such as {@link Country}, {@link Product}, {@link Shelf},
 * and {@link ProductSkuStatus}. It also handles the transformation between DTOs and entities via {@link EntityMapper}.
 * </p>
 *
 * <p>
 * The class is marked as a Spring {@link org.springframework.stereotype.Service @Service} and operates
 * within a transactional context provided by {@link org.springframework.transaction.annotation.Transactional @Transactional}.
 * </p>
 *
 * @author Aliaksandr Pishchala
 * @see ProductSkuService
 * @see ProductSku
 * @see ProductSkuDto
 * @see EntityMapper
 */
@Service
@Transactional
public class ProductSkuServiceImpl extends EntityMapper<ProductSkuDto, ProductSku> implements ProductSkuService {
    private final static String IN_STOCK = "In Stock";

    private final ProductSkuDao skuDao;
    private final ServiceMediator serviceMediator;

    @Autowired
    public ProductSkuServiceImpl(ProductSkuDao skuDao, ServiceMediator serviceMediator) {
        this.skuDao = skuDao;
        this.serviceMediator = serviceMediator;
    }

    @Override
    public boolean save(ProductSkuDto skuDto) throws CoreException {
        ProductSku productSku = new ProductSku();
        productSku.setCode(skuDto.getCode());
        productSku.setCountry(resolveCountry(skuDto.getCountryDto()));
        productSku.setProduct(resolveProduct(skuDto.getProductDto()));
        productSku.setShelf(resolveShelf(skuDto.getShelfDto()));
        productSku.setStatus(serviceMediator.findSkuStatusByStatus(IN_STOCK).orElseThrow(() -> {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.SKU_STATUS_BY_NAME_NOT_FOUND_MESSAGE,
                                        IN_STOCK));
        }));
        return skuDao.save(productSku) > 0;
    }

    @Override
    public boolean update(ProductSkuDto skuDto) throws CoreException {
        Integer skuDtoId = skuDto.getProductSkuDtoId();
        if (StringUtils.isNullNumericObject(skuDtoId)) {
            // logger
            throw new CoreException(String.format(
                    ExceptionMessages.ID_IS_NULL_MESSAGE,
                    ExceptionMessages.PRODUCT_SKU_TYPE));
        }
        ProductSku existingSku = skuDao.findById(skuDtoId)
                .orElseThrow(() -> {
                   // logger.warn("ProductSku not found, ID = {}", skuDto.getProductSkuId());
                    throw new CoreException(String.format(
                                                ExceptionMessages.PRODUCT_SKU_BY_ID_NOT_FOUND_MESSAGE,
                                                skuDtoId));
                });

        if (skuDto.getCode() != null && !skuDto.getCode().isBlank()) {
            existingSku.setCode(skuDto.getCode());
        }

        if (skuDto.getCountryDto() != null) {
            existingSku.setCountry(resolveCountry(skuDto.getCountryDto()));
        }

        if (skuDto.getProductDto() != null) {
            existingSku.setProduct(resolveProduct(skuDto.getProductDto()));
        }

        if (skuDto.getShelfDto() != null) {
            existingSku.setShelf(resolveShelf(skuDto.getShelfDto()));
        }

        if (skuDto.getStatus() != null) {
            ProductSkuStatus status = serviceMediator.findSkuStatusById(skuDto.getStatus().getProductSkuStatusDtoId())
                    .orElseThrow(() -> {
                        //logger.warn("ProductSkuStatus not found: {}", skuDto.getStatus());
                        throw new CoreException(String.format(
                                                    ExceptionMessages.PRODUCT_SKU_BY_ID_NOT_FOUND_MESSAGE,
                                                    skuDto.getStatus().getProductSkuStatusDtoId()));
                    });
            existingSku.setStatus(status);
        }

        return skuDao.update(existingSku);
    }

    @Override
    public boolean updateStatus(ProductSkuDto skuDto, String status) throws CoreException {
        if (StringUtils.isNullNumericObject(skuDto.getProductSkuDtoId())) {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.ID_IS_NULL_MESSAGE,
                                        ExceptionMessages.PRODUCT_SKU_TYPE));
        }
        if (StringUtils.isNullNumericObject(status)) {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.ID_IS_NULL_MESSAGE,
                                        ExceptionMessages.PRODUCT_SKU_STATUS_TYPE));
        }
        ProductSku productSku = skuDao.findById(skuDto.getProductSkuDtoId()).orElseThrow(() -> {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.PRODUCT_SKU_BY_ID_NOT_FOUND_MESSAGE,
                                        skuDto.getProductSkuDtoId()));
        });
        ProductSkuStatus newStatus = serviceMediator.findSkuStatusByStatus(status).orElseThrow(() -> {
            // logger
            throw new CoreException(String.format(
                                        ExceptionMessages.SKU_STATUS_BY_NAME_NOT_FOUND_MESSAGE,
                                        status));
        });
        productSku.setStatus(newStatus);
        return skuDao.update(productSku);
    }

    @Override
    public boolean remove(Integer id) throws CoreException {
        if (StringUtils.isNullNumericObject(id)) {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.ID_IS_NULL_MESSAGE,
                                        ExceptionMessages.PRODUCT_SKU_TYPE));
        }
        return skuDao.remove(id);
    }

    @Override
    public Optional<ProductSkuDto> findById(Integer id) throws CoreException {
        if (StringUtils.isNullNumericObject(id)) {
            //logger
            throw new CoreException(String.format(
                                    ExceptionMessages.ID_IS_NULL_MESSAGE,
                                    ExceptionMessages.PRODUCT_SKU_TYPE));
        }
        Optional<ProductSku> productSku = skuDao.findById(id);
        if (productSku.isEmpty()) {
            return Optional.empty();
        }
        return productSku.map(this::mapToObjectDto);
    }

    @Override
    public List<ProductSkuDto> findByCode(String code) throws CoreException {
        return mapToListObjectsDto(skuDao.findByCode(code));
    }

    @Override
    public List<ProductSkuDto> findAllByStatus(int limit, int offset, String status) throws CoreException {
        ProductSkuStatus productSkuStatus = serviceMediator.findSkuStatusByStatus(status)
                .orElseThrow(() -> new CoreException(String.format(
                                                        ExceptionMessages.INVALID_SKU_STATUS_MESSAGE,
                                                        status)));
        return mapToListObjectsDto(skuDao.findAllByStatus(limit, offset, productSkuStatus));
    }

    @Override
    public List<ProductSkuDto> findAllByProduct(int limit, int offset, ProductDto productDto) throws CoreException {
        Integer productId = productDto.getProductDtoId();
        if (StringUtils.isNullNumericObject(productId)) {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.ID_IS_NULL_MESSAGE,
                                        ExceptionMessages.PRODUCT_TYPE));
        }
        return mapToListObjectsDto(skuDao.findAllByProduct(limit, offset, new Product(productId)));
    }

    @Override
    public List<ProductSkuDto> mapToListObjectsDto(List<ProductSku> objectsList) throws CoreException {
        List<ProductSkuDto> skuDtoList = new ArrayList<>();
        for (ProductSku productSku : objectsList) {
            skuDtoList.add(mapToObjectDto(productSku));
        }
        return skuDtoList;
    }

    @Override
    public ProductSkuDto mapToObjectDto(ProductSku object) throws CoreException {
        if (object == null) {
            throw new CoreException(String.format(
                                        ExceptionMessages.OBJECT_IS_NULL_MESSAGE,
                                        ExceptionMessages.PRODUCT_SKU_TYPE));
        }

        ProductSkuDto skuDto = new ProductSkuDto();
        skuDto.setProductSkuDtoId(object.getProductSkuId());
        skuDto.setCode(object.getCode());

        skuDto.setProductDto(mapProduct(object.getProduct()));
        skuDto.setCountryDto(mapCountry(object.getCountry()));
        skuDto.setShelfDto(mapShelf(object.getShelf()));
        skuDto.setStatus(mapStatus(object.getStatus()));

        return skuDto;
    }

    /**
     * Maps a Product entity to its corresponding ProductDto.
     *
     * @param product the Product entity to be mapped
     * @return the mapped ProductDto or null if the input is null
     */
    private ProductDto mapProduct(Product product) {
        if (product == null) return null;

        ProductDto dto = new ProductDto();
        dto.setProductDtoId(product.getProductId());
        dto.setProductCompanyDto(mapCompany(product.getProductCompany()));
        return dto;
    }

    /**
     * Maps a ProductCompany entity to its corresponding ProductCompanyDto.
     *
     * @param company the ProductCompany entity to be mapped
     * @return the mapped ProductCompanyDto or null if the input is null
     */
    private ProductCompanyDto mapCompany(ProductCompany company) {
        if (company == null) return null;

        return new ProductCompanyDto(company.getProductCompanyId(), company.getName());
    }

    /**
     * Maps a Country entity to its corresponding CountryDto.
     *
     * @param country the Country entity to be mapped
     * @return the mapped CountryDto or null if the input is null
     */
    private CountryDto mapCountry(Country country) {
        if (country == null) return null;

        return new CountryDto(country.getCountryId(), country.getName());
    }

    /**
     * Maps a Shelf entity to its corresponding ShelfDto, including nested StorageRack.
     *
     * @param shelf the Shelf entity to be mapped
     * @return the mapped ShelfDto or null if the input is null
     */
    private ShelfDto mapShelf(Shelf shelf) {
        if (shelf == null) return null;

        StorageRackDto rackDto = null;
        if (shelf.getStorageRack() != null) {
            rackDto = new StorageRackDto(
                    shelf.getStorageRack().getRackId(),
                    shelf.getStorageRack().getName()
            );
        }

        return new ShelfDto(shelf.getShelfId(), shelf.getName(), rackDto);
    }

    /**
     * Maps a ProductSkuStatus entity to its corresponding ProductSkuStatusDto.
     *
     * @param status the ProductSkuStatus entity to be mapped
     * @return the mapped ProductSkuStatusDto or null if the input is null
     */
    private ProductSkuStatusDto mapStatus(ProductSkuStatus status) {
        if (status == null) return null;

        return new ProductSkuStatusDto(status.getProductSkuStatusId(), status.getStatus());
    }

    /**
     * Resolves a Country entity from the given CountryDto.
     *
     * @param dto the CountryDto containing the ID of the country to resolve
     * @return the resolved Country entity
     * @throws CoreException if the ID is null or the Country is not found
     */
    private Country resolveCountry(CountryDto dto) {
        if (StringUtils.isNullNumericObject(dto.getCountryId())) {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.OBJECT_IS_NULL_MESSAGE,
                                        ExceptionMessages.COUNTRY_TYPE));
        }
        return serviceMediator.findCountryById(dto.getCountryId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                                                ExceptionMessages.COUNTRY_BY_ID_NOT_FOUND_MESSAGE,
                                                dto.getCountryId()));
                });
    }

    /**
     * Resolves a Product entity from the given ProductDto.
     *
     * @param dto the ProductDto containing the ID of the product to resolve
     * @return the resolved Product entity
     * @throws CoreException if the ID is null or the Product is not found
     */
    private Product resolveProduct(ProductDto dto) {
        if (StringUtils.isNullNumericObject(dto.getProductDtoId())) {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.OBJECT_IS_NULL_MESSAGE,
                                        ExceptionMessages.PRODUCT_TYPE));
        }
        return serviceMediator.findProductById(dto.getProductDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                                                ExceptionMessages.PRODUCT_BY_ID_NOT_FOUND_MESSAGE,
                                                dto.getProductDtoId()));
                });
    }

    /**
     * Resolves a Shelf entity from the given ShelfDto.
     *
     * @param dto the ShelfDto containing the ID of the shelf to resolve
     * @return the resolved Shelf entity
     * @throws CoreException if the ID is null or the Shelf is not found
     */
    private Shelf resolveShelf(ShelfDto dto) {
        if (StringUtils.isNullNumericObject(dto.getShelfDtoId())) {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.OBJECT_IS_NULL_MESSAGE,
                                        ExceptionMessages.SHELF_TYPE));
        }
        return serviceMediator.findShelfById(dto.getShelfDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                                                ExceptionMessages.SHELF_BY_ID_NOT_FOUND_MESSAGE,
                                                dto.getShelfDtoId(),
                                                dto.getName()));
                });
    }
}
