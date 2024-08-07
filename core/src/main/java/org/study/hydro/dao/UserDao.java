package org.study.hydro.dao;

import org.study.hydro.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link UserDao} provides operation with data of database table 'users'.
 *
 * @author Aliaksandr Pishchala
 */
public interface UserDao {

    /**
     * The method creates new record in database table.
     *
     * @param user entity that specifies creation of new records in database table.
     *
     * @return returns the number of modified rows in the table.
     */
    int save(User user);

    /**
     * The method will return list of users.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of users.
     */
    List<User> users(int limit, int offset);

    /**
     * The method returns specified User by id.
     *
     * @param id the User's id.
     *
     * @return the specified Optional User by id.
     */
    Optional<User> getUserById(int id);

    /**
     * The method returns specified User by email.
     *
     * @param email the User email.
     *
     * @return the specified Optional User by email.
     */
    Optional<User> getUserByEmail(String email);
}
