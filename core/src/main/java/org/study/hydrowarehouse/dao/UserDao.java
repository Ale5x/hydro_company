package org.study.hydrowarehouse.dao;

import org.study.hydrowarehouse.entity.ERole;
import org.study.hydrowarehouse.entity.Role;
import org.study.hydrowarehouse.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link UserDao} provides operation with data of database table 'users'.
 *
 * @author Aliaksandr Pishchala
 */
public interface UserDao {

    /**
     * Persists the given {@link User} entity in the database.
     * <p>
     * This method saves the {@code user} entity using the current Hibernate session.
     * It explicitly flushes the session to ensure that the insert operation is executed immediately,
     * and then returns the generated ID of the saved user.
     *
     * @param user the {@link User} entity to be persisted in the database
     * @return the generated ID of the saved user
     */
    int save(User user);

    /**
     * Updates the given {@link User} entity in the data store.
     * <p>
     * This method attempts to update an existing {@link User} with the data provided.
     * It returns {@code true} if the update was successful, or {@code false} if the user
     * does not exist or the update operation failed.
     *
     * @param user the {@link User} entity containing updated information
     * @return {@code true} if the user was successfully updated; {@code false} otherwise
     */
    boolean update(User user);

    /**
     * The method will return list of the users.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of the users.
     */
    List<User> users(int limit, int offset);

    /**
     * Retrieves a paginated list of users filtered by their role.
     *
     * @param roleName   the role of the users to find
     * @param limit  the maximum number of users to retrieve
     * @param offset the starting position (zero-based) from which to retrieve users
     * @return a list of {@link User} matching the specified role within the given range
     */
    List<User> findAllByRole(ERole roleName, int limit, int offset);

    /**
     * The method returns the specified User by id.
     *
     * @param id is the User's id.
     *
     * @return the specified Optional User by id.
     */
    Optional<User> getUserById(Integer id);

    /**
     * The method returns the specified User by email.
     *
     * @param email is the User's email.
     *
     * @return the specified Optional User by email.
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Finds a list of users filtered by their status with pagination support.
     *
     * @param status the status to filter users by (e.g., "ACTIVE", "BLOCKED")
     * @param limit the maximum number of users to return
     * @param offset the number of users to skip before starting to collect the result set
     * @return a list of users matching the specified status within the given pagination constraints; empty list if no users found
     */
    List<User> findByStatus(String status, int limit, int offset);

    /**
     * Retrieves a list of users associated with the specified country.
     *
     * @param countryId the unique identifier of the country
     * @param limit     the maximum number of results to return (for pagination)
     * @param offset    the starting position of the first result (for pagination)
     * @return a list of {@link User} entities from the specified country;
     *         an empty list if no users are found
     */
    List<User> findByCountry(int countryId, int limit, int offset);
}
