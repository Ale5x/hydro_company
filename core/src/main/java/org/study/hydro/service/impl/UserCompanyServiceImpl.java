package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.UserCompanyDao;
import org.study.hydro.entity.UserCompany;
import org.study.hydro.entity.Dto.UserCompanyDto;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.UserCompanyService;

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

    private final static String USER_COMPANY_BY_ID_NOT_FOUND = "User company by id not found.";
    private UserCompanyDao userCompanyDao;

    @Autowired
    public UserCompanyServiceImpl(UserCompanyDao userCompanyDao) {
        this.userCompanyDao = userCompanyDao;
    }

    @Override
    public boolean create(UserCompanyDto userCompanyDto) throws CoreException {
        return userCompanyDao.save(mapToEntityFromDto(userCompanyDto, false)) > 0;
    }

    @Override
    public List<UserCompanyDto> findByName(String name) throws CoreException {
        return mapToListObjectsDto(userCompanyDao.companiesByName(name));
    }

    @Override
    public Optional<UserCompanyDto> findById(int id) throws CoreException {
        return Optional.of(mapToObjectDto(userCompanyDao.companyById(id)
                .orElseThrow(() -> new CoreException(USER_COMPANY_BY_ID_NOT_FOUND))));
    }

    @Override
    public List<UserCompanyDto> findAll(int offset, int size) throws CoreException {
        return mapToListObjectsDto(userCompanyDao.companies(size, offset));
    }

    @Override
    public List<UserCompanyDto> mapToListObjectsDto(List<UserCompany> objectsList) throws CoreException {
        List<UserCompanyDto> userCompanyDtoList = new ArrayList<>();
        for (UserCompany userCompany : objectsList) {

            userCompanyDtoList.add(mapToObjectDto(userCompany));
        }
        return userCompanyDtoList;
    }

    @Override
    public UserCompanyDto mapToObjectDto(UserCompany object) throws CoreException {
        UserCompanyDto userCompanyDto = new UserCompanyDto();

        userCompanyDto.setCompanyDtoId(object.getUserCompanyId());
        userCompanyDto.setName(object.getName());
        userCompanyDto.setAddress(object.getAddress());
        return userCompanyDto;
    }

    @Override
    public UserCompany mapToEntityFromDto(UserCompanyDto objectDto, boolean isUpdate) {
        UserCompany userCompany = new UserCompany();

        if (isUpdate) {
            userCompany.setUserCompanyId(objectDto.getCompanyDtoId());
        }

        userCompany.setName(objectDto.getName());
        userCompany.setAddress(objectDto.getAddress());
        return userCompany;
    }
}
