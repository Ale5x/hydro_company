package org.study.hydro.service;

import org.study.hydro.entity.Dto.StorageRackDto;
import org.study.hydro.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The StorageRackService interface {@link StorageRackService} provides methods for managing storage racks.
 * It defines basic operations for creating, updating etc. storage racks.
 *
 * @author Aliaksandr Pishchala
 */
public interface StorageRackService {

    /**
     * Creates a new storage rack based on the provided data.
     * @param storageRackDto a {@link StorageRackDto} object containing the new storage rack data.
     * @return true if the storage rack was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(StorageRackDto storageRackDto) throws CoreException;

    /**
     * Updates the data of an existing storage rack.
     * @param storageRackDto a {@link StorageRackDto} object containing the updated storage rack data.
     * @return true if the storage rack was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean update(StorageRackDto storageRackDto) throws CoreException;

    /**
     * Retrieves a storageRackDto by its unique identifier.
     * @param id the unique identifier of the storage rack.
     * @return an Optional object of {@link StorageRackDto} object representing the storage rack with the specified id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<StorageRackDto> findById(int id) throws CoreException;

    /**
     * Retrieves a list of all storageRacksDto.
     * @param limit the maximum number of storageRacksDto to include in the list.
     * @param offset the starting position of the storageRackDto list.
     * @return a list of all storageRacksDto as {@link StorageRackDto} objects.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<StorageRackDto> storageRackList(int limit, int offset) throws CoreException;
}
