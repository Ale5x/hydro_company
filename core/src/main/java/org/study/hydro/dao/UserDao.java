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
     * The method creates the new record in the database table.
     *
     * @param user entity that specifies the creation of new record in the database table.
     *
     * @return returns the number of the modified rows in the table.
     */
    int save(User user);

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
     * The method returns the specified User by id.
     *
     * @param id is the User's id.
     *
     * @return the specified Optional User by id.
     */
    Optional<User> getUserById(int id);

    /**
     * The method returns the specified User by email.
     *
     * @param email is the User's email.
     *
     * @return the specified Optional User by email.
     */
    Optional<User> getUserByEmail(String email);
}
