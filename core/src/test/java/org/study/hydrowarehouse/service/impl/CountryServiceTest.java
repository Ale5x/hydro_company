package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydrowarehouse.dao.impl.CountryDaoImpl;
import org.study.hydrowarehouse.entity.UserCompany;
import org.study.hydrowarehouse.entity.Country;
import org.study.hydrowarehouse.entity.Dto.CountryDto;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    @InjectMocks
    private CountryServiceImpl countryService;

    @Mock
    private CountryDaoImpl countryDao;

    private Set<Country> countriesSet = new HashSet<>();
    private Set<Country> countriesSearchCriteriaSet = new HashSet<>();
    private Country optionalCountry = new Country(1, "Name ");
    private List<CountryDto> countryDtoList = new ArrayList<>();
    private List<CountryDto> countryDtoSearchCriteriaList = new ArrayList<>();
    private List<CountryDto> expectedCountryDtoSearchCriteriaList = new ArrayList<>();
    private  List<CountryDto> expectedListCounties;

    @BeforeEach
    void init() {
        for(int i = 0; i < 10; i++) {
            Country country = new Country();
            CountryDto countryDto = new CountryDto();

            country.setCountryId(i);
            country.setName("name country " + Math.random() * 100 + i);

            countryDto.setCountryId(country.getCountryId());
            countryDto.setName(country.getName());

            countryDtoList.add(countryDto);
            countriesSet.add(country);
        }

        Country countrySearchCriteria1 = new Country(111, "Search");
        Country countrySearchCriteria2 = new Country(333, "Sea");
        Country countrySearchCriteria3 = new Country(222, "Se");

        countriesSearchCriteriaSet.add(countrySearchCriteria1);
        countriesSearchCriteriaSet.add(countrySearchCriteria2);
        countriesSearchCriteriaSet.add(countrySearchCriteria3);

        CountryDto countryDtoSearchCriteria = new CountryDto();
        countryDtoSearchCriteria.setName("Search");
        countryDtoSearchCriteria.setCountryId(111);

        countryDtoSearchCriteriaList.add(countryDtoSearchCriteria);

        CountryDto countryDtoSearchCriteria1 = new CountryDto();
        countryDtoSearchCriteria1.setName("Se");
        countryDtoSearchCriteria1.setCountryId(222);

        countryDtoSearchCriteriaList.add(countryDtoSearchCriteria1);

        CountryDto countryDtoSearchCriteria2 = new CountryDto();
        countryDtoSearchCriteria2.setName("Sea");
        countryDtoSearchCriteria2.setCountryId(333);

        countryDtoSearchCriteriaList.add(countryDtoSearchCriteria2);

        expectedCountryDtoSearchCriteriaList = countryDtoSearchCriteriaList
                .stream()
                .sorted(Comparator.comparing(CountryDto::getName))
                .collect(Collectors.toList());
        expectedListCounties = countryDtoList
                .stream()
                .sorted(Comparator.comparing(CountryDto::getName))
                .collect(Collectors.toList());
    }

    @Test
    void getByIdRightTest() {
        int id = 1;
        given(countryDao.countryById(id)).willReturn(Optional.of(optionalCountry));
        Optional<CountryDto> countryDto = countryService.findById(id);
        assertTrue(countryDto.isPresent());
    }

    @Test
    void getByIdWrongTest() {
        int id = 1;
        given(countryDao.countryById(id)).willReturn(Optional.empty());
        Optional<CountryDto> countryDto = countryService.findById(id);
        assertFalse(countryDto.isPresent());
        assertTrue(countryDto.isEmpty());
    }

    @Test
    void getCountryByIdRightTest() {
        int id = 1;
        given(countryDao.countryById(id)).willReturn(Optional.of(optionalCountry));
        Optional<CountryDto> countryDto = countryService.findById(id);
        assertTrue(countryDto.isPresent());
    }

    @Test
    void getCountryByIdWrongTest() {
        int id = 1;
        given(countryDao.countryById(id)).willReturn(Optional.empty());
        Optional<CountryDto> countryDto = countryService.findById(id);
        assertFalse(countryDto.isPresent());
        assertTrue(countryDto.isEmpty());
    }

    @Test
    void getCountriesRightTest() {
        int testOne = 0;
        int testTwo = 1;
        int testThree = 2;
        given(countryDao.countries()).willReturn(countriesSet);
        List<CountryDto> actualListCountries = countryService.findCountries();

        assertEquals(expectedListCounties.get(testOne), actualListCountries.get(testOne));
        assertEquals(expectedListCounties.get(testTwo), actualListCountries.get(testTwo));
        assertEquals(expectedListCounties.get(testThree), actualListCountries.get(testThree));
    }

    @Test
    void getCountriesWrongTest() {
        int testOne = 0;
        int testTwo = 1;
        int testThree = 2;
        given(countryDao.countries()).willReturn(countriesSet);
        List<CountryDto> actualListCountries = countryService.findCountries();

        assertFalse(actualListCountries.get(testOne).equals(countryDtoList.get(testOne)));
        assertFalse(actualListCountries.get(testTwo).equals(countryDtoList.get(testTwo)));
        assertFalse(actualListCountries.get(testThree).equals(countryDtoList.get(testThree)));
    }

    @Test
    void getCountriesByNameRightTest() {
        int testOne = 0;
        int testTwo = 1;
        int testThree = 2;
        String searchCriteria = "search";
        given(countryDao.countriesByName(searchCriteria)).willReturn(countriesSearchCriteriaSet);
        List<CountryDto> actualListCountries = countryService.findByName(searchCriteria);

        assertEquals(expectedCountryDtoSearchCriteriaList.get(testOne), actualListCountries.get(testOne));
        assertEquals(expectedCountryDtoSearchCriteriaList.get(testTwo), actualListCountries.get(testTwo));
        assertEquals(expectedCountryDtoSearchCriteriaList.get(testThree), actualListCountries.get(testThree));
    }

    @Test
    void getCountriesByNameWrongTest() {
        int testOne = 0;
        int testTwo = 1;
        int testThree = 2;
        String searchCriteria = "search";
        given(countryDao.countriesByName(searchCriteria)).willReturn(countriesSearchCriteriaSet);
        List<CountryDto> actualListCountries = countryService.findByName(searchCriteria);

        assertFalse(countryDtoSearchCriteriaList.get(testOne).equals(actualListCountries.get(testOne)));
        assertFalse(countryDtoSearchCriteriaList.get(testTwo).equals(actualListCountries.get(testTwo)));
        assertFalse(countryDtoSearchCriteriaList.get(testThree).equals(actualListCountries.get(testThree)));
    }
}
