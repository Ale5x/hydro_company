package org.study.hydro.service;

import org.study.hydro.entity.Dto.ProductTypeDto;
import org.study.hydro.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The ProductTypeService interface {@link ProductTypeService} provides methods for managing product types.
 * It defines basic operations for creating, updating, removing etc. product types.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductTypeService {

    /**
     * Creates a new product type based on the provided data.
     * @param productTypeDto a {@link ProductTypeDto} object containing the new product type data.
     * @return true if the product type was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(ProductTypeDto productTypeDto) throws CoreException;

    /**
     * Updates the data of an existing product type.
     * @param productTypeDto a {@link ProductTypeDto} object containing the updated product type data.
     * @return true if the product type was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean update(ProductTypeDto productTypeDto) throws CoreException;

    /**
     * Deletes the data of an existing product type.
     * @param id the unique identifier of the product type to be deleted.
     * @return true if the product type was successfully deleted; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean remove(int id) throws CoreException;

    List<ProductTypeDto> findAll() throws CoreException;

    /**
     * Retrieves a productTypeDto by its unique identifier.
     * @param id the unique identifier of the product type.
     * @return an Optional object of {@link ProductTypeDto} object representing the product type with the specified id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<ProductTypeDto> findById(int id) throws CoreException;

    /**
     * Retrieves a productTypeDto by its name.
     * @param name the name of the product type.
     * @return the list of {@link ProductTypeDto} objects representing the product type with its name.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductTypeDto> findByName(String name) throws CoreException;
}
