package org.study.hydrowarehouse.dao;

import org.study.hydrowarehouse.entity.UserStatus;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for {@link UserStatus} entities.
 * <p>
 * Provides methods to perform CRUD operations and queries related to user statuses.
 *
 * @author Aliaksandr Pishchala
 */
public interface UserStatusDao {

    /**
     * Retrieves all user statuses from the database.
     *
     * @return a list of all {@link UserStatus} entities.
     */
    List<UserStatus> findAll();

    /**
     * Finds a {@link UserStatus} entity by its status name.
     *
     * @param status the name of the status to search for.
     * @return an {@link Optional} containing the found {@link UserStatus},
     *         or an empty {@link Optional} if not found.
     */
    Optional<UserStatus> findByStatus(String status);

    /**
     * Finds a {@link UserStatus} entity by its unique ID.
     *
     * @param statusId the ID of the status to retrieve.
     * @return an {@link Optional} containing the found {@link UserStatus},
     *         or an empty {@link Optional} if not found.
     */
    Optional<UserStatus> findById(Long statusId);
}
