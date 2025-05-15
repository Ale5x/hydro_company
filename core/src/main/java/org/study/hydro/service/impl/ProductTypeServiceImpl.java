package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.ProductTypeDao;
import org.study.hydro.entity.Dto.ProductTypeDto;
import org.study.hydro.entity.ProductType;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.ProductTypeService;
import org.study.hydro.utill.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductTypeServiceImpl extends EntityMapper<ProductTypeDto, ProductType> implements ProductTypeService {

    private final static String PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE = "Product type not found. [id = %s]";

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
                            String.format(PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE, productTypeDto.getProductTypeId()));
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
            throw new CoreException(PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE);
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

    protected ProductType updateProductTypeFromDto (ProductTypeDto productTypeDto) {
        ProductType productType = new ProductType();
        Optional<ProductType> productTypeOpt = productTypeDao.getProductTypeById(productTypeDto.getProductTypeId());
        if (productTypeOpt.isPresent()) {
            productType.setProductTypeId(productTypeDto.getProductTypeId());
            productType.setName(productTypeDto.getName() == null
                    ? productTypeOpt.get().getName() : productTypeDto.getName());
        } else {
            //logger
            throw new CoreException(String.format(PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE, productTypeDto.getProductTypeId()));
        }
        return productType;
    }
}
