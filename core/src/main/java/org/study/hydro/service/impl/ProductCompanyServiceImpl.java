package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.ProductCompanyDao;
import org.study.hydro.entity.Dto.ProductCompanyDto;
import org.study.hydro.entity.ProductCompany;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.ProductCompanyService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductCompanyServiceImpl extends EntityMapper<ProductCompanyDto, ProductCompany> implements ProductCompanyService {

    private final ProductCompanyDao productCompanyDao;

    private final static String PRODUCT_COMPANY_BY_ID_NOT_FOUND_ERROR = "ProductCompany by id not found";

    @Autowired
    public ProductCompanyServiceImpl(ProductCompanyDao productCompanyDao) {
        this.productCompanyDao = productCompanyDao;
    }

    @Override
    public boolean create(ProductCompanyDto productCompanyDto) throws CoreException {
        return productCompanyDao.create(mapToEntityFromDto(productCompanyDto, false));
    }

    @Override
    public boolean update(ProductCompanyDto productCompanyDto) throws CoreException {
        return productCompanyDao.update(mapToEntityFromDto(productCompanyDto, true));
    }

    @Override
    public List<ProductCompanyDto> findAll(int offset, int limit) throws CoreException {
        return mapToListObjectsDto(productCompanyDao.getProductCompanies(offset, limit));
    }

    @Override
    public List<ProductCompanyDto> findAllByName(String name) throws CoreException {
        return mapToListObjectsDto(productCompanyDao.getProductCompaniesByName(name));
    }

    @Override
    public Optional<ProductCompanyDto> findById(int id) throws CoreException {
        Optional<ProductCompany> productCompany = productCompanyDao.getById(id);
        if (productCompany.isEmpty()) {
            return Optional.empty();
        }
        return productCompany.map(this::mapToObjectDto);
    }

    @Override
    public List<ProductCompanyDto> mapToListObjectsDto(List<ProductCompany> objectsList) {
        List<ProductCompanyDto> prodCompanyDtoList = new ArrayList<>();
        for (ProductCompany company : objectsList) {

            prodCompanyDtoList.add(mapToObjectDto(company));
        }
        return prodCompanyDtoList;
    }

    @Override
    public ProductCompanyDto mapToObjectDto(ProductCompany object) {
        ProductCompanyDto productCompanyDto = new ProductCompanyDto();

        productCompanyDto.setProductCompanyDtoId(object.getProductCompanyId());
        productCompanyDto.setName(object.getName());
        return productCompanyDto;
    }

    @Override
    public ProductCompany mapToEntityFromDto(ProductCompanyDto objectDto, boolean isUpdate) {
        ProductCompany productCompany = new ProductCompany();

        if (isUpdate) {
            productCompany.setProductCompanyId(objectDto.getProductCompanyDtoId());
        }

        productCompany.setName(objectDto.getName());
        return productCompany;
    }
}
