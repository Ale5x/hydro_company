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
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.ProductCompanyService;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductCompanyServiceImpl extends EntityMapper<ProductCompanyDto, ProductCompany> implements ProductCompanyService {

    private final ProductCompanyDao productCompanyDao;
    private final ServiceMediator serviceMediator;

    @Autowired
    public ProductCompanyServiceImpl(ProductCompanyDao productCompanyDao, ServiceMediator serviceMediator) {
        this.productCompanyDao = productCompanyDao;
        this.serviceMediator = serviceMediator;
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
        productCompanyDto.setCountries(toCountryDtoList(object.getCompanyCountries()));
        return productCompanyDto;
    }

    /**
     * Converts a set of Country entities to a list of CountryDto objects.
     *
     * @param countrySet the set of Country entities to convert
     * @return a list of corresponding CountryDto objects
     */
    public List<CountryDto> toCountryDtoList(Set<Country> countrySet) {
        if (countrySet == null || countrySet.isEmpty()) {
            return Collections.emptyList();
        }
        return countrySet.stream()
                .map(this::mapToCountryDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a {@link Country} entity to its corresponding {@link CountryDto}.
     * <p>
     * This method extracts the identifier and name from the {@code Country} object
     * and maps them into a new instance of {@code CountryDto}.
     * </p>
     *
     * @param country the {@code Country} entity to convert; must not be {@code null}
     * @return a {@code CountryDto} containing mapped data from the input entity
     * @throws NullPointerException if the input {@code country} is {@code null}
     */
    private CountryDto mapToCountryDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setCountryId(country.getCountryId());
        dto.setName(country.getName());
        return dto;
    }
}
