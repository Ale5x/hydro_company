package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.UserCompanyDao;
import org.study.hydrowarehouse.entity.Country;
import org.study.hydrowarehouse.entity.Dto.CountryDto;
import org.study.hydrowarehouse.entity.UserCompany;
import org.study.hydrowarehouse.entity.Dto.UserCompanyDto;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.service.CountryService;
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.UserCompanyService;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.*;

/**
 * The class {@link UserCompanyServiceImpl} implements methods of the CompanyService interface.
 * The class is annotated as a service, which qualifies it to be automatically created by component-scanning.
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class UserCompanyServiceImpl extends EntityMapper<UserCompanyDto, UserCompany> implements UserCompanyService {
    private final UserCompanyDao userCompanyDao;

    private final CountryService countryService;

    @Autowired
    public UserCompanyServiceImpl(UserCompanyDao userCompanyDao, CountryService countryService) {
        this.userCompanyDao = userCompanyDao;
        this.countryService = countryService;
    }

    @Override
    public boolean create(UserCompanyDto userCompanyDto) throws CoreException {
        UserCompany userCompany = new UserCompany();
        userCompany.setAddress(userCompanyDto.getAddress());
        userCompany.setName(userCompanyDto.getName());
        userCompany.setCountry(findCountryForCompany(userCompanyDto.getCountryDto()));
        return userCompanyDao.save(userCompany) > 0;
    }

    @Override
    public boolean update(UserCompanyDto userCompanyDto) throws CoreException {
        UserCompany existingUserCompany =userCompanyDao.companyById(userCompanyDto.getCompanyDtoId())
                .orElseThrow(() -> {
                    //logger
                    return new CoreException(String.format(
                                                ExceptionMessages.USER_COMPANY_BY_ID_NOT_FOUND_MESSAGE,
                                                userCompanyDto.getCompanyDtoId()));
                });

        existingUserCompany.setName(StringUtils.isBlankOrNullText(userCompanyDto.getName())
                ? existingUserCompany.getName() : userCompanyDto.getName());
        Country newCountry = findCountryForCompany(userCompanyDto.getCountryDto());
        existingUserCompany.setCountry(
                newCountry.getCountryId().equals(userCompanyDto.getCountryDto().getCountryId()) ? newCountry :
                        existingUserCompany.getCountry());

        existingUserCompany.setAddress(StringUtils.isBlankOrNullText(userCompanyDto.getAddress())
                ? existingUserCompany.getAddress() : userCompanyDto.getAddress());

        return userCompanyDao.update(existingUserCompany);
    }

    @Override
    public List<UserCompanyDto> findByName(String name) throws CoreException {
        return mapToListObjectsDto(userCompanyDao.companiesByName(name));
    }

    @Override
    public Optional<UserCompanyDto> findById(int id) throws CoreException {
        Optional<UserCompany> company = userCompanyDao.companyById(id);
        if (company.isEmpty()) {
            //logger
            return Optional.empty();
        }
        return company.map(this::mapToObjectDto);
    }

    @Override
    public List<UserCompanyDto> findAll(int offset, int size) throws CoreException {
        return mapToListObjectsDto(userCompanyDao.companies(size, offset));
    }

    @Override
    public List<UserCompanyDto> mapToListObjectsDto(List<UserCompany> objectsList) throws CoreException {
        if (objectsList == null) return null;
        List<UserCompanyDto> userCompanyDtoList = new ArrayList<>();
        for (UserCompany userCompany : objectsList) {
            userCompanyDtoList.add(mapToObjectDto(userCompany));
        }
        return userCompanyDtoList;
    }

    @Override
    public UserCompanyDto mapToObjectDto(UserCompany object) throws CoreException {
        if (object == null) return null;
        UserCompanyDto userCompanyDto = new UserCompanyDto();

        userCompanyDto.setCompanyDtoId(object.getUserCompanyId());
        userCompanyDto.setName(object.getName());
        userCompanyDto.setAddress(object.getAddress());
        return userCompanyDto;
    }

    @Override
    public Optional<UserCompany> findCompanyById(int id) throws CoreException {
        return userCompanyDao.companyById(id);
    }

    protected Country findCountryForCompany(CountryDto countryDto) {
        if (countryDto == null) return null;
        return countryService.findCountryById(countryDto.getCountryId())
                .orElseThrow(() -> {
                    // Logger
                    throw new CoreException(
                            String.format(
                                    ExceptionMessages.COUNTRY_NOT_FOUND_MESSAGE,
                                    countryDto.getCountryId(),
                                    countryDto.getName())
                    );
                });
    }
}