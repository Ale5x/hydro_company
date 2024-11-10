package org.study.hydro.service;

import org.study.hydro.entity.Dto.CompanyDto;
import org.study.hydro.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The interface {@link CompanyService} User service contains methods for business logic with company.
 *
 * @author Aliaksandr Pishchala
 */
public interface CompanyService {

    /**
     * The method creates a new company in the database.
     *
     * @param companyDto is a companyDto type that contains some information about the new company.
     * @return The boolean result. If the new company is created that return true. Else the method will return false.
     * @throws CoreException
     */
    boolean create(CompanyDto companyDto);

    /**
     * The method returns specified list of companies by name.
     *
     * @param name the company's name.
     *
     * @return the specified list of CompaniesDto by name.
     */
    List<CompanyDto> findByName(String name);

    /**
     * The method will return list of companies.
     *
     * @param size the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of companiesDto.
     */
    List<CompanyDto> findAll(int offset, int size);

    /**
     * The method returns specified CompanyDto by id.
     *
     * @param id the Company's id.
     *
     * @return the specified Optional CompanyDto by id.
     */
    Optional<CompanyDto> findById(int id);
}
