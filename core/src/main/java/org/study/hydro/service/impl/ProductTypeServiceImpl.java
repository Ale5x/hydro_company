package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.ProductTypeDao;
import org.study.hydro.entity.Dto.ProductTypeDto;
import org.study.hydro.entity.ProductType;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.ProductTypeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeServiceImpl extends EntityMapper<ProductTypeDto, ProductType> implements ProductTypeService {

    private final ProductTypeDao productTypeDao;
    private final static String PRODUCT_TYPE_BY_ID_NOT_FOUND_ERROR = "Product Type by id not found.";
    private final static String PRODUCT_TYPE_BY_NAME_NOT_FOUND_ERROR = "Product Type by name not found.";

    @Autowired
    public ProductTypeServiceImpl(ProductTypeDao productTypeDao) {
        this.productTypeDao = productTypeDao;
    }


    @Override
    public boolean create(ProductTypeDto productTypeDto) throws CoreException {
        return productTypeDao.create(mapToEntityFromDto(productTypeDto, false));
    }

    @Override
    public boolean update(ProductTypeDto productTypeDto) throws CoreException {
        return productTypeDao.update(mapToEntityFromDto(productTypeDto, true));
    }

    @Override
    public boolean remove(int id) throws CoreException {
        Optional<ProductType> productType = productTypeDao.getProductTypeById(id);
        if(productType.isPresent()) {
            return productTypeDao.remove(id);
        } else {
            throw new CoreException(PRODUCT_TYPE_BY_ID_NOT_FOUND_ERROR);
        }
    }

    @Override
    public List<ProductTypeDto> findAll() throws CoreException {
        return mapToListObjectsDto(productTypeDao.getProductTypes());
    }

    @Override
    public Optional<ProductTypeDto> findById(int id) throws CoreException {
        return Optional.of(mapToObjectDto(productTypeDao.getProductTypeById(id)
                .orElseThrow(() -> new CoreException(PRODUCT_TYPE_BY_ID_NOT_FOUND_ERROR))));
    }

    @Override
    public List<ProductTypeDto> findByName(String name) throws CoreException {
        return mapToListObjectsDto(productTypeDao.getProductTypeByName(name));
    }

    @Override
    public List<ProductTypeDto> mapToListObjectsDto(List<ProductType> objectsList) {
        List<ProductTypeDto> productTypeDtoList = new ArrayList<>();
        for (ProductType productType : objectsList) {

            productTypeDtoList.add(mapToObjectDto(productType));
        }
        return productTypeDtoList;
    }

    @Override
    public ProductTypeDto mapToObjectDto(ProductType object) {
        ProductTypeDto productTypeDto = new ProductTypeDto();

        productTypeDto.setProductTypeId(object.getProductTypeId());
        productTypeDto.setName(object.getName());
        return productTypeDto;
    }

    @Override
    public ProductType mapToEntityFromDto(ProductTypeDto objectDto, boolean isUpdate) {
        ProductType productType = new ProductType();

        if (isUpdate) {
            productType.setProductTypeId(objectDto.getProductTypeId());
        }
        productType.setName(objectDto.getName());
        return productType;
    }
}
