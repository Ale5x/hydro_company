package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.entity.Dto.ShelfDto;
import org.study.hydrowarehouse.entity.Shelf;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The ShelfService interface {@link ShelfService} provides methods for managing shelves.
 * It defines basic operations for removing, updating etc. shelves.
 *
 * @author Aliaksandr Pishchala
 */
public interface ShelfService {

    /**
     * Updates the data of an existing shelf.
     * @param shelfDto a {@link ShelfDto} object containing the updated shelf data.
     * @return true if the shelf was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean update(ShelfDto shelfDto) throws CoreException;

    /**
     * Retrieves a shelfDto by its unique identifier.
     * @param id the unique identifier of the shelf.
     * @return an Optional object of {@link ShelfDto} object representing the shelf with the specified id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<ShelfDto> findById(int id) throws CoreException;

    /**
     * Retrieves a shelf by its name.
     * @param name the name of the shelf.
     * @return an Optional object of {@link Shelf} object representing the shelf with its name.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<Shelf> findShelfByName(String name) throws CoreException;

    /**
     * Retrieves a shelf by its name.
     * @param name the name of the shelf.
     * @return an Optional object of {@link ShelfDto} object representing the shelf with its name.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<ShelfDto> findByName(String name) throws CoreException;

    /**
     * Deletes the data of an existing shelf.
     * @param id the unique identifier of the shelf to be deleted.
     * @return true if the shelf was successfully deleted; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean remove(int id) throws CoreException;

    /**
     * Retrieves a list of the ShelfDto.
     * @return the list of {@link ShelfDto} objects representing the shelf.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ShelfDto> findAll() throws CoreException;


    /**
     * Creates a new shelf based on the provided data.
     * @param shelfDto a {@link ShelfDto} object containing the new shelf data.
     * @return true if the shelf was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(ShelfDto shelfDto) throws CoreException;
}
