package org.study.hydrowarehouse.dao;

import org.study.hydrowarehouse.entity.UserCompany;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link UserCompanyDao} provides operation with data of database table 'companies'.
 *
 * @author Aliaksandr Pishchala
 */
public interface UserCompanyDao {

    /**
     * Persists the given {@link UserCompany} entity in the database.
     * <p>
     * This method saves the {@code userCompany} entity using the current Hibernate session.
     * It explicitly flushes the session to ensure that the insert operation is executed immediately,
     * and then returns the generated ID of the saved userCompany.
     *
     * @param userCompany the {@link UserCompany} entity to be persisted in the database
     * @return the generated ID of the saved userCompany
     */
    int save(UserCompany userCompany);

    /**
     * Updates the given {@link UserCompany} entity in the data store.
     * <p>
     * This method attempts to apply changes to an existing {@link UserCompany} record.
     * It returns {@code true} if the update was successful, or {@code false} if the entity
     * was not found or the update could not be completed.
     *
     * @param userCompany the {@link UserCompany} entity containing the updated data
     * @return {@code true} if the update was successful; {@code false} otherwise
     */
    boolean update(UserCompany userCompany);

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
    Optional<UserCompany> companyById(Integer id);
}
