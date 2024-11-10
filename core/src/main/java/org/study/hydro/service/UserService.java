package org.study.hydro.service;

import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The interface {@link UserService} User service contains methods for business logic with user.
 *
 * @author Aliaksandr Pishchala
 */
public interface UserService {

    /**
     * The method creates a new user in the database.
     *
     * @param user is a userDto type that contains some information about the new user.
     * @return The boolean result. If the new user is created that return true. Else the method will return false.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(UserDto user) throws CoreException;

    /**
     * The method will return list of users.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of usersDto.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<UserDto> findAll(int limit, int offset) throws CoreException;

    /**
     * The method returns specified UserDto by id.
     *
     * @param id the User's id.
     *
     * @return the specified Optional UserDto by id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<UserDto> findUserById(int id) throws CoreException;

    /**
     * The method returns specified UserDto by email.
     *
     * @param email the User email.
     *
     * @return the specified Optional UserDto by email.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<UserDto> findUserByEmail(String email) throws CoreException;
}
