package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CountryDao;
import org.study.hydrowarehouse.entity.Country;
import org.study.hydrowarehouse.entity.Dto.CountryDto;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.mapping.DtoResolver;
import org.study.hydrowarehouse.service.CountryService;
import org.study.hydrowarehouse.mapping.EntityMapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing {@link Country} entities.
 * <p>
 * Provides business logic and transactional operations for creating, updating, retrieving, and deleting countries.
 * Extends {@link EntityMapper} to handle mapping between {@link CountryDto} and {@link Country} entities.
 * </p>
 *
 * @see EntityMapper
 * @see CountryService
 * @see Country
 * @see CountryDto
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class CountryServiceImpl extends EntityMapper<CountryDto, Country> implements CountryService {

    private final CountryDao countryDao;
    private final DtoResolver dtoResolver;

    @Autowired
    public CountryServiceImpl(CountryDao countryDao, DtoResolver dtoResolver) {
        this.countryDao = countryDao;
        this.dtoResolver = dtoResolver;
    }

    @Override
    public List<CountryDto> findCountries() {
        return mapToListObjectsDto(new ArrayList<>(countryDao.countries()));
    }

    @Override
    public List<CountryDto> findByName(String name) {
        return mapToListObjectsDto(new ArrayList<>(countryDao.countriesByName(name)))
                .stream()
                .sorted(Comparator.comparing(CountryDto::getName))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CountryDto> findById(int id) {
        Optional<Country> country = countryDao.countryById(id);
        if (country.isEmpty()) {
            //logger
            return Optional.empty();
        }
        return country.map(this::mapToObjectDto);
    }

    @Override
    public Optional<Country> findCountryById(int id) {
        return countryDao.countryById(id);
    }

    @Override
    public List<CountryDto> findByProduct(int productId) throws CoreException {
        return mapToListObjectsDto(new ArrayList<>(countryDao.countriesByProduct(productId)))
                .stream()
                .sorted(Comparator.comparing(CountryDto::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<CountryDto> mapToListObjectsDto(List<Country> objectsDtoList) {
        if (objectsDtoList == null || objectsDtoList.isEmpty()) return null;
        List<CountryDto> countryDtoList = new ArrayList<>();
        for(Country country : objectsDtoList) {
            countryDtoList.add(mapToObjectDto(country));
        }
        return countryDtoList;
    }

    @Override
    public CountryDto mapToObjectDto(Country object) {
        return dtoResolver.resolveCountryDto(object);
    }
}
