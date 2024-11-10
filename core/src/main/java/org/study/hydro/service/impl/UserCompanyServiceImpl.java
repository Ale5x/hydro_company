package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.CompanyDao;
import org.study.hydro.entity.UserCompany;
import org.study.hydro.entity.Dto.CompanyDto;
import org.study.hydro.service.CompanyService;

import java.util.*;

/**
 * The class {@link CompanyServiceImpl} implements methods of the CompanyService interface.
 * The class is annotated as a service, which qualifies it to be automatically created by component-scanning.
 *
 * @author Aliaksandr Pishchala
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyDao companyDao;

    @Override
    public boolean create(CompanyDto companyDto) {
        UserCompany userCompany = new UserCompany();

        userCompany.setCompanyId(companyDto.getCompanyDtoId() == 0 ? 0 : companyDto.getCompanyDtoId());
        userCompany.setName(companyDto.getName());
        userCompany.setAddress(companyDto.getAddress());
        return companyDao.save(userCompany) > 0;
    }

    @Override
    public List<CompanyDto> findByName(String name) {
        return buildCompanyDto(companyDao.companiesByName(name));
    }

    @Override
    public Optional<CompanyDto> findById(int id) {

        return companyDao.companyById(id).map(
                value -> buildCompanyDto(Collections.singletonList(value)).get(0));
    }

    @Override
    public List<CompanyDto> findAll(int offset, int size) {
        return buildCompanyDto(companyDao.companies(size, offset));
    }

    /**
     * The method creates a list of the Company type DTO from the company's list for transport between layers.
     * @param companiesList contains companies.
     * @return The list of the companies Dto.
     */
    private List<CompanyDto> buildCompanyDto(List<UserCompany> companiesList) {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (UserCompany userCompany : companiesList) {
            CompanyDto companyDto = new CompanyDto();
            companyDto.setCompanyDtoId(userCompany.getCompanyId());
            companyDto.setName(userCompany.getName());
            companyDto.setAddress(userCompany.getAddress());

            companyDtoList.add(companyDto);
        }

        return companyDtoList;
    }
}
