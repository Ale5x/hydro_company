package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CountryDao;
import org.study.hydrowarehouse.entity.Country;
import org.study.hydrowarehouse.entity.Dto.CountryDto;
import org.study.hydrowarehouse.service.CountryService;
import org.study.hydrowarehouse.service.EntityMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CountryServiceImpl extends EntityMapper<CountryDto, Country> implements CountryService {

    private static final String COUNTRY_NOT_FOUND_ERROR = "Country not found.";
    private final CountryDao countryDao;

    @Autowired
    public CountryServiceImpl(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    @Override
    public List<CountryDto> findCountries() {
        return mapToListObjectsDto(new ArrayList<>(countryDao.countries()))
                .stream()
                .sorted(Comparator.comparing(CountryDto::getName))
                .collect(Collectors.toList());
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
    public List<CountryDto> mapToListObjectsDto(List<Country> objectsDtoList) {
        List<CountryDto> countryDtoList = new ArrayList<>();
        for(Country country : objectsDtoList) {
            CountryDto countryDto = new CountryDto();
            countryDto.setCountryId(country.getCountryId());
            countryDto.setName(country.getName());
            countryDtoList.add(countryDto);
        }
        return countryDtoList;
    }

    @Override
    public CountryDto mapToObjectDto(Country object) {
        CountryDto countryDto = new CountryDto();
        countryDto.setCountryId(object.getCountryId());
        countryDto.setName(object.getName());
        return countryDto;
    }
}
