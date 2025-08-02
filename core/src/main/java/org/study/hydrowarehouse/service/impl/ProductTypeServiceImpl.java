package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ProductTypeDao;
import org.study.hydrowarehouse.entity.Dto.ProductTypeDto;
import org.study.hydrowarehouse.entity.ProductType;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.ProductTypeService;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing {@link ProductType} entities.
 * <p>
 * Provides business logic and transactional operations for creating, updating, retrieving, and deleting product types.
 * Extends {@link EntityMapper} to handle mapping between {@link ProductTypeDto} and {@link ProductType} entities.
 * </p>
 *
 * @see EntityMapper
 * @see ProductTypeService
 * @see ProductType
 * @see ProductTypeDto
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class ProductTypeServiceImpl extends EntityMapper<ProductTypeDto, ProductType> implements ProductTypeService {

    private final ProductTypeDao productTypeDao;

    @Autowired
    public ProductTypeServiceImpl(ProductTypeDao productTypeDao) {
        this.productTypeDao = productTypeDao;
    }

    @Override
    public boolean create(ProductTypeDto productTypeDto) throws CoreException {
        ProductType productType = new ProductType();
        productType.setName(productTypeDto.getName());
        return productTypeDao.create(productType) > 0;
    }

    @Override
    public boolean update(ProductTypeDto productTypeDto) throws CoreException {
        ProductType existingProductType = productTypeDao.getProductTypeById(productTypeDto.getProductTypeId())
                .orElseThrow(() -> {
                    //logger
                    return new CoreException(
                            String.format(
                                    ExceptionMessages.PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE,
                                    productTypeDto.getProductTypeId()));
                });
        existingProductType.setName(StringUtils.isBlankOrNullText(productTypeDto.getName())
                ? existingProductType.getName() : productTypeDto.getName());
        return productTypeDao.update(existingProductType);
    }

    @Override
    public boolean remove(int id) throws CoreException {
        Optional<ProductType> productType = productTypeDao.getProductTypeById(id);
        if(productType.isPresent()) {
            return productTypeDao.remove(id);
        } else {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE,
                                        id));
        }
    }

    @Override
    public List<ProductTypeDto> findAll() throws CoreException {
        return mapToListObjectsDto(productTypeDao.getProductTypes());
    }

    @Override
    public Optional<ProductTypeDto> findById(int id) throws CoreException {
        Optional<ProductType> productType = productTypeDao.getProductTypeById(id);
        if (productType.isEmpty()) {
            //logger
            return Optional.empty();
        }
        return productType.map(this::mapToObjectDto);
    }

    @Override
    public List<ProductTypeDto> findByName(String name) throws CoreException {
        return mapToListObjectsDto(productTypeDao.getProductTypeByName(name));
    }

    @Override
    public List<ProductTypeDto> mapToListObjectsDto(List<ProductType> objectsList) {
        if (objectsList == null) return null;

        List<ProductTypeDto> productTypeDtoList = new ArrayList<>();
        for (ProductType productType : objectsList) {

            productTypeDtoList.add(mapToObjectDto(productType));
        }
        return productTypeDtoList;
    }

    @Override
    public ProductTypeDto mapToObjectDto(ProductType object) {
        if (object == null) return null;
        ProductTypeDto productTypeDto = new ProductTypeDto();

        productTypeDto.setProductTypeId(object.getProductTypeId());
        productTypeDto.setName(object.getName());
        return productTypeDto;
    }

    protected ProductType updateProductTypeFromDto (ProductTypeDto productTypeDto) {
        ProductType productType = new ProductType();
        Optional<ProductType> productTypeOpt = productTypeDao.getProductTypeById(productTypeDto.getProductTypeId());
        if (productTypeOpt.isPresent()) {
            productType.setProductTypeId(productTypeDto.getProductTypeId());
            productType.setName(productTypeDto.getName() == null
                    ? productTypeOpt.get().getName() : productTypeDto.getName());
        } else {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE,
                                        productTypeDto.getProductTypeId()));
        }
        return productType;
    }
}
