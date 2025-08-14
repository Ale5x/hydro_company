package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ProductCompanyDao;
import org.study.hydrowarehouse.entity.Country;
import org.study.hydrowarehouse.entity.Dto.CountryDto;
import org.study.hydrowarehouse.entity.Dto.ProductCompanyDto;
import org.study.hydrowarehouse.entity.ProductCompany;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.mapping.DtoResolver;
import org.study.hydrowarehouse.mapping.EntityMapper;
import org.study.hydrowarehouse.service.ProductCompanyService;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing {@link ProductCompany} entities.
 * <p>
 * Provides business logic and transactional operations for creating, updating, retrieving, and deleting product
 * companies. Extends {@link EntityMapper} to handle mapping between {@link ProductCompanyDto} and {@link ProductCompany}
 * entities.
 * </p>
 *
 * @see EntityMapper
 * @see DtoResolver
 * @see ProductCompanyService
 * @see ProductCompany
 * @see ProductCompanyDto
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class ProductCompanyServiceImpl extends EntityMapper<ProductCompanyDto, ProductCompany> implements ProductCompanyService {

    private final ProductCompanyDao productCompanyDao;
    private final ServiceMediator serviceMediator;
    private final DtoResolver dtoResolver;

    @Autowired
    public ProductCompanyServiceImpl(ProductCompanyDao productCompanyDao, ServiceMediator serviceMediator,
                                     DtoResolver dtoResolver) {
        this.productCompanyDao = productCompanyDao;
        this.serviceMediator = serviceMediator;
        this.dtoResolver = dtoResolver;
    }

    @Override
    public boolean create(ProductCompanyDto productCompanyDto) throws CoreException {
        ProductCompany productCompany = new ProductCompany();
        productCompany.setName(productCompanyDto.getName());
        productCompany.setCompanyCountries(getCountriesFromDto(productCompanyDto));
        return productCompanyDao.create(productCompany) > 0;
    }

    @Override
    public boolean update(ProductCompanyDto productCompanyDto) throws CoreException {
        ProductCompany existingCompany = productCompanyDao.getById(productCompanyDto.getProductCompanyDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                                                ExceptionMessages.PRODUCT_COMPANY_BY_ID_NOT_FOUND_MESSAGE,
                                                productCompanyDto.getProductCompanyDtoId()));
                });
        existingCompany.setName(StringUtils.isBlankOrNullText(productCompanyDto.getName())
                ? existingCompany.getName() : productCompanyDto.getName());
        existingCompany.setCompanyCountries(getCountriesFromDto(productCompanyDto));
        return productCompanyDao.update(existingCompany);
    }

    protected Set<Country> getCountriesFromDto(ProductCompanyDto productCompanyDto) {
        Set<Country> countrySet = new HashSet<>();
        for(CountryDto countryDto : productCompanyDto.getCountries()) {
            Country country = serviceMediator.findCountryById(countryDto.getCountryId())
                    .orElseThrow(() -> {
                        //logger
                        throw new CoreException(String.format(
                                                    ExceptionMessages.COUNTRY_FOR_PRODUCT_COMPANY_NOT_FOUND_MESSAGE,
                                                    countryDto.getCountryId(),
                                                    countryDto.getName()));
                    });
            countrySet.add(country);
        }
        return countrySet;
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
        if (objectsList == null) return null;
        List<ProductCompanyDto> prodCompanyDtoList = new ArrayList<>();
        for (ProductCompany company : objectsList) {

            prodCompanyDtoList.add(mapToObjectDto(company));
        }
        return prodCompanyDtoList;
    }

    @Override
    public ProductCompanyDto mapToObjectDto(ProductCompany object) {
        if (object == null) return new ProductCompanyDto();
        ProductCompanyDto companyDto = dtoResolver.resolveProductCompanyDto(object);
        companyDto.setCountries(dtoResolver.resolveCountryDtoList(object.getCompanyCountries()));
        return companyDto;
    }
}
