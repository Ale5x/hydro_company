package org.study.hydro.service;

import org.study.hydro.entity.Dto.UserCompanyDto;
import org.study.hydro.entity.UserCompany;
import org.study.hydro.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The interface {@link UserCompanyService} User service contains methods for business logic with company.
 *
 * @author Aliaksandr Pishchala
 */
public interface UserCompanyService {

    /**
     * The method creates a new company in the database.
     *
     * @param userCompanyDto is a companyDto type that contains some information about the new company.
     * @return The boolean result. If the new company is created that return true. Else the method will return false.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(UserCompanyDto userCompanyDto) throws CoreException;

    boolean update(UserCompanyDto userCompanyDto) throws CoreException;

    /**
     * The method returns specified list of companies by name.
     *
     * @param name the company's name.
     *
     * @return the specified list of CompaniesDto by name.
     */
    List<UserCompanyDto> findByName(String name) throws CoreException;

    /**
     * The method will return list of companies.
     *
     * @param size the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of companiesDto.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<UserCompanyDto> findAll(int offset, int size) throws CoreException;

    /**
     * The method returns specified CompanyDto by id.
     *
     * @param id the Company's id.
     *
     * @return the specified Optional CompanyDto by id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<UserCompanyDto> findById(int id) throws CoreException;

    /**
     * The method returns specified Company by id.
     *
     * @param id the Company's id.
     *
     * @return the specified Optional Company by id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<UserCompany> findCompanyById(int id) throws CoreException;
}
