package org.study.hydro.service;

import org.study.hydro.entity.Dto.ProductConnectionDto;
import org.study.hydro.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The ProductConnectionService interface {@link ProductConnectionService} provides methods for managing
 * product connection.
 * It defines basic operations for creating, updating etc. product connection.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductConnectionService {

    /**
     * Creates a new product connection based on the provided data.
     * @param productConnectionDto a {@link ProductConnectionDto} object containing the new product connection data.
     * @return true if the product connection was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(ProductConnectionDto productConnectionDto) throws CoreException;

    /**
     * Updates the data of an existing product connection.
     * @param productConnectionDto a {@link ProductConnectionDto} object containing the updated product connection data.
     * @return true if the product connection was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean update(ProductConnectionDto productConnectionDto) throws CoreException;

    List<ProductConnectionDto> findAll() throws CoreException;

    /**
     * Retrieves a productConnectionDto by its unique identifier.
     * @param id the unique identifier of the product connection.
     * @return an Optional object of {@link ProductConnectionDto} object representing the product connection
     * with the specified id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<ProductConnectionDto> findById(int id) throws CoreException;
}
