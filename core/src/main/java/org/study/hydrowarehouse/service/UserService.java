package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.entity.Dto.UserDto;
import org.study.hydrowarehouse.exception.CoreException;

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
     * Updates an existing user using the provided {@link UserDto} data.
     * <p>
     * This method performs validation and business logic checks before attempting
     * to update the user in the underlying data store. If the user is not found,
     * or if the update cannot be completed due to business rules or system constraints,
     * a {@link CoreException} is thrown.
     *
     * @param userDto the {@link UserDto} containing the updated user information
     * @return {@code true} if the update was successful; {@code false} if no changes were applied
     * @throws CoreException if the user cannot be updated due to validation errors or internal issues
     */
    boolean update(UserDto userDto) throws CoreException;

    /**
     * The method will return list of users.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of usersDto.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<UserDto> findAll(int offset, int limit) throws CoreException;

    /**
     * Retrieves a list of users with the specified status, using pagination.
     *
     * @param status the status of users to filter by (e.g., "ACTIVE", "BLOCKED")
     * @param offset the starting index of the result set (for pagination)
     * @param limit the maximum number of results to return
     * @return a list of {@link UserDto} objects matching the given status
     * @throws CoreException if an error occurs during the retrieval process
     */
    List<UserDto> findAllByStatus(String status, int offset, int limit) throws CoreException;

    /**
     * Retrieves a paginated list of users filtered by their role.
     *
     * @param role   the role of the users to find (e.g., "ADMIN", "USER")
     * @param offset the starting position (zero-based) from which to retrieve users
     * @param limit  the maximum number of users to retrieve
     * @return a list of {@link UserDto} matching the specified role within the given range
     * @throws CoreException if an error occurs during the retrieval process
     */
    List<UserDto> findAllByRole(String role, int offset, int limit) throws CoreException;

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

    /**
     * Changes the status of a user by their ID.
     *
     * @param userId the ID of the user whose status should be changed
     * @param newStatus the new status to set (e.g., "ACTIVE", "BLOCKED", "INACTIVE")
     * @return {@code true} if the status was successfully changed, {@code false} otherwise
     * @throws CoreException if the user is not found or the status is invalid
     */
    boolean changeStatus(Integer userId, String newStatus) throws CoreException;

    /**
     * Changes the role of a user identified by the given user ID.
     *
     * @param userId  the unique identifier of the user whose role is to be changed
     * @param newRole the new role to assign to the user; must be a valid role name
     * @return {@code true} if the role was successfully changed; {@code false} otherwise
     * @throws CoreException if the new role is invalid or if the role change operation fails
     */
    boolean changeRole(Integer userId, String newRole) throws CoreException;
}