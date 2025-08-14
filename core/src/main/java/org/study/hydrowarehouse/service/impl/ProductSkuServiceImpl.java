package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ProductSkuDao;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.*;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.mapping.DtoResolver;
import org.study.hydrowarehouse.mapping.EntityMapper;
import org.study.hydrowarehouse.mapping.EntityResolver;
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
 * @see EntityMapper
 * @see ProductSkuService
 * @see ProductSku
 * @see ProductSkuDto
 * @see EntityResolver
 * @see DtoResolver
 * @see ServiceMediator
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class ProductSkuServiceImpl extends EntityMapper<ProductSkuDto, ProductSku> implements ProductSkuService {
    private final static String IN_STOCK = "In Stock";

    private final ProductSkuDao skuDao;
    private final ServiceMediator serviceMediator;

    private final DtoResolver dtoResolver;

    private final EntityResolver entityResolver;

    @Autowired
    public ProductSkuServiceImpl(ProductSkuDao skuDao, ServiceMediator serviceMediator, DtoResolver dtoResolver, EntityResolver entityResolver) {
        this.skuDao = skuDao;
        this.serviceMediator = serviceMediator;
        this.dtoResolver = dtoResolver;
        this.entityResolver = entityResolver;
    }

    @Override
    public boolean save(ProductSkuDto skuDto) throws CoreException {
        ProductSku productSku = new ProductSku();
        productSku.setCode(skuDto.getCode());
        productSku.setCountry(entityResolver.resolveCountry(skuDto.getCountryDto()));
        productSku.setProduct(entityResolver.resolveProduct(skuDto.getProductDto()));
        productSku.setShelf(entityResolver.resolveShelf(skuDto.getShelfDto()));
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
            existingSku.setCountry(entityResolver.resolveCountry(skuDto.getCountryDto()));
        }

        if (skuDto.getProductDto() != null) {
            existingSku.setProduct(entityResolver.resolveProduct(skuDto.getProductDto()));
        }

        if (skuDto.getShelfDto() != null) {
            existingSku.setShelf(entityResolver.resolveShelf(skuDto.getShelfDto()));
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
        if (objectsList == null) return null;
        List<ProductSkuDto> skuDtoList = new ArrayList<>();
        for (ProductSku productSku : objectsList) {
            skuDtoList.add(mapToObjectDto(productSku));
        }
        return skuDtoList;
    }

    @Override
    public ProductSkuDto mapToObjectDto(ProductSku object) throws CoreException {
        ProductSkuDto skuDto = dtoResolver.resolveProductSkuDto(object);
        skuDto.setProductDto(mapProduct(object.getProduct()));
        skuDto.setCountryDto(mapCountry(object.getCountry()));
        skuDto.setShelfDto(mapShelf(object.getShelf()));
        skuDto.setStatus(mapStatus(object.getStatus()));

        return skuDto;
    }

    /**
     * Maps a {@link Product} entity to its corresponding {@link ProductDto}.
     * <p>
     * Resolves the basic product fields and sets the related company, connection, and type DTOs.
     * </p>
     *
     * @param product the {@link Product} entity to be mapped; must not be {@code null}
     * @return a fully populated {@link ProductDto}
     * @throws CoreException if an error occurs during the DTO resolution process
     */
    private ProductDto mapProduct(Product product) throws CoreException {
        ProductDto dto = dtoResolver.resolveProductBasicFields(product);
        dto.setProductCompanyDto(dtoResolver.resolveProductCompanyDto(product.getProductCompany()));
        dto.setProductConnectionDto(dtoResolver.resolveProductConnectionDto(product.getProductConnection()));
        dto.setProductTypeDto(dtoResolver.resolveProductTypeDto(product.getProductType()));
        return dto;
    }

    /**
     * Maps a {@link Country} entity to its corresponding {@link CountryDto}.
     *
     * @param country the {@link Country} entity to be mapped; may be {@code null}
     * @return the corresponding {@link CountryDto}, or {@code null} if {@code country} is null
     * @throws CoreException if an error occurs during the DTO resolution process
     */
    private CountryDto mapCountry(Country country) throws CoreException {
        return dtoResolver.resolveCountryDto(country);
    }

    /**
     * Maps a {@link Shelf} entity to its corresponding {@link ShelfDto}.
     * <p>
     * Resolves the shelf fields and sets the related storage rack DTO.
     * </p>
     *
     * @param shelf the {@link Shelf} entity to be mapped; must not be {@code null}
     * @return a fully populated {@link ShelfDto}
     * @throws CoreException if an error occurs during the DTO resolution process
     */
    private ShelfDto mapShelf(Shelf shelf) throws CoreException {
        ShelfDto shelfDto = dtoResolver.resolveShelfDto(shelf);
        shelfDto.setStorageRackDto(dtoResolver.resolveStorageRackDto(shelf.getStorageRack()));
        return shelfDto;
    }

    /**
     * Maps a {@link ProductSkuStatus} entity to its corresponding {@link ProductSkuStatusDto}.
     *
     * @param status the {@link ProductSkuStatus} entity to be mapped; may be {@code null}
     * @return the corresponding {@link ProductSkuStatusDto}, or {@code null} if {@code status} is null
     * @throws CoreException if an error occurs during the DTO resolution process
     */
    private ProductSkuStatusDto mapStatus(ProductSkuStatus status) throws CoreException {
        return dtoResolver.resolveProductSkuStatusDto(status);
    }
}
