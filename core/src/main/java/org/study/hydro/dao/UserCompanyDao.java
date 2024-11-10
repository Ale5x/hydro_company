package org.study.hydro.dao;

import org.study.hydro.entity.UserCompany;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link UserCompanyDao} provides operation with data of database table 'companies'.
 *
 * @author Aliaksandr Pishchala
 */
public interface UserCompanyDao {

    /**
     * The method creates new record in rhe database table.
     *
     * @param userCompany entity that specifies creation of the new record in the database table.
     *
     * @return returns the number of the modified rows in the table.
     */
    int save(UserCompany userCompany);

    /**
     * The method will return list of the companies.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of the companies.
     */
    List<UserCompany> companies(int limit, int offset);

    /**
     * The method returns the specified companies by name.
     *
     * @param name is the Company's name.
     *
     * @return the specified list of the Companies by name.
     */
    List<UserCompany> companiesByName(String name);

    /**
     * The method returns the specified company by id.
     *
     * @param id is the Company's id.
     *
     * @return the specified Optional Company by id.
     */
    Optional<UserCompany> companyById(int id);
}
