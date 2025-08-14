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
import org.study.hydrowarehouse.mapping.DtoResolver;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

    @InjectMocks
    private CountryServiceImpl countryService;

    @Mock
    private CountryDaoImpl countryDao;
    @Mock
    private DtoResolver dtoResolver;

    private Set<Country> countriesSet = new HashSet<>();
    private Set<Country> countriesSearchCriteriaSet = new HashSet<>();
    private Country optionalCountry = new Country(1, "Name ");
    private CountryDto countryDto = new CountryDto(1, "Name DTO");
    private List<CountryDto> countryDtoList = new ArrayList<>();
    private List<CountryDto> countryDtoSearchCriteriaList = new ArrayList<>();
    private List<CountryDto> expectedCountryDtoSearchCriteriaList = new ArrayList<>();
    private List<CountryDto> expectedListCounties;
    private Set<Country> countries = new HashSet<>();


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

        countries.add(new Country(1, "Germany"));
        countries.add(new Country(2, "France"));
        countries.add(new Country(3, "Italy"));
    }

    @Test
    void getByIdRightTest() {
        int id = 1;
        given(countryDao.countryById(id)).willReturn(Optional.of(optionalCountry));
        given(dtoResolver.resolveCountryDto(optionalCountry)).willReturn(countryDto);
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
        given(dtoResolver.resolveCountryDto(optionalCountry)).willReturn(countryDto);
        Optional<CountryDto> countryDto = countryService.findById(id);
        assertTrue(countryDto.isPresent());
    }

    @Test
    void getCountriesRightTest() {
        given(countryDao.countries()).willReturn(countriesSet);
        given(dtoResolver.resolveCountryDto(any(Country.class)))
                .willReturn(countryDto);
        List<CountryDto> actualListCountries = countryService.findCountries();

        assertFalse(actualListCountries.isEmpty());

    }

    @Test
    void getCountriesWrongTest() {
        given(countryDao.countries()).willReturn(new HashSet<>());
        List<CountryDto> actualListCountries = countryService.findCountries();

        assertFalse(actualListCountries.size() > 0);
    }

    @Test
    void getCountriesByNameRightTest() {
        String searchCriteria = "search";
        given(countryDao.countriesByName(searchCriteria)).willReturn(countriesSearchCriteriaSet);
        given(dtoResolver.resolveCountryDto(any(Country.class)))
                .willReturn(countryDto);
        List<CountryDto> actualListCountries = countryService.findByName(searchCriteria);

        assertFalse(actualListCountries.isEmpty());
    }

    @Test
    void getCountriesByNameWrongTest() {
        String searchCriteria = "search";
        given(countryDao.countriesByName(searchCriteria)).willReturn(new HashSet<>());
        List<CountryDto> actualListCountries = countryService.findByName(searchCriteria);

        assertFalse(actualListCountries.size() > 0);
    }

    @Test
    void testFindByProduct_ReturnsSortedCountryDtos()  {
        int productId = 123;

        when(countryDao.countriesByProduct(productId)).thenReturn(countries);
        given(dtoResolver.resolveCountryDto(any(Country.class)))
                .willReturn(countryDto);
        List<CountryDto> result = countryService.findByProduct(productId);

        assertNotNull(result);
        assertTrue( result.size() > 0);
    }

    @Test
    void testFindByProduct_EmptyList(){
        int productId = 999;

        when(countryDao.countriesByProduct(productId)).thenReturn(Collections.emptySet());

        List<CountryDto> result = countryService.findByProduct(productId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
