package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CompanyDao;
import org.study.hydro.entity.Company;
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
        Company company = new Company();

        company.setCompanyId(companyDto.getCompanyDtoId() == 0 ? 0 : companyDto.getCompanyDtoId());
        company.setName(companyDto.getName());
        company.setAddress(companyDto.getAddress());
        return companyDao.save(company) > 0;
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
    private List<CompanyDto> buildCompanyDto(List<Company> companiesList) {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (Company company : companiesList) {
            CompanyDto companyDto = new CompanyDto();
            companyDto.setCompanyDtoId(company.getCompanyId());
            companyDto.setName(company.getName());
            companyDto.setAddress(company.getAddress());

            companyDtoList.add(companyDto);
        }

        return companyDtoList;
    }
}
