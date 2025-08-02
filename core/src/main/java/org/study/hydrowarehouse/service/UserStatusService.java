package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.entity.Dto.UserStatusDto;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing user statuses.
 * <p>
 * Provides business-level operations related to {@link UserStatusDto}.
 * <p>
 *
 * @author Aliaksandr Pishchala
 */
public interface UserStatusService {

    /**
     * Retrieves all user statuses.
     *
     * @return a list of all {@link UserStatusDto} objects.
     * @throws CoreException if an error occurs during retrieval.
     */
    List<UserStatusDto> findAll() throws CoreException;

    /**
     * Finds a user status by its status name.
     *
     * @param status the name of the status to search for.
     * @return an {@link Optional} containing the found {@link UserStatusDto},
     *         or an empty {@link Optional} if not found.
     * @throws CoreException if an error occurs during the search.
     */
    Optional<UserStatusDto> findByStatus(String status) throws CoreException;

    /**
     * Finds a user status by its unique identifier.
     *
     * @param statusId the ID of the status to retrieve.
     * @return an {@link Optional} containing the found {@link UserStatusDto},
     *         or an empty {@link Optional} if not found.
     * @throws CoreException if an error occurs during the lookup.
     */
    Optional<UserStatusDto> findById(Long statusId) throws CoreException;
}
