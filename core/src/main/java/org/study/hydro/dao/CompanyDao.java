package org.study.hydro.dao;

import org.study.hydro.entity.Company;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link CompanyDao} provides operation with data of database table 'companies'.
 *
 * @author Aliaksandr Pishchala
 */
public interface CompanyDao {

    /**
     * The method creates new record in database table.
     *
     * @param company entity that specifies creation of new records in database table.
     *
     * @return returns the number of modified rows in the table.
     */
    int save(Company company);

    /**
     * The method will return list of companies.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of companies.
     */
    List<Company> companies(int limit, int offset);

    /**
     * The method returns specified companies by name.
     *
     * @param name the Company's name.
     *
     * @return the specified list of Companies by name.
     */
    List<Company> companiesByName(String name);

    /**
     * The method returns specified company by id.
     *
     * @param id the Company's id.
     *
     * @return the specified Optional Company by id.
     */
    Optional<Company> companyById(int id);
}
