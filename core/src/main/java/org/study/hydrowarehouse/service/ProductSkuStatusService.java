package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.entity.Dto.ProductSkuStatusDto;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing product SKU statuses.
 * <p>
 * Provides methods to create, update, delete and retrieve SKU statuses.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductSkuStatusService {

    /**
     * Saves a new ProductSkuStatus.
     *
     * @param skuStatusDto the DTO containing SKU status information to be saved
     * @return {@code true} if the SKU status was saved successfully, {@code false} otherwise
     * @throws CoreException if an error occurs during the save operation
     */
    boolean save(ProductSkuStatusDto skuStatusDto) throws CoreException;

    /**
     * Updates an existing ProductSkuStatus.
     *
     * @param skuStatusDto the DTO containing updated SKU status information
     * @return {@code true} if the SKU status was updated successfully, {@code false} otherwise
     * @throws CoreException if the SKU status does not exist or an error occurs during the update
     */
    boolean update(ProductSkuStatusDto skuStatusDto) throws CoreException;

    /**
     * Removes the ProductSkuStatus by its ID.
     *
     * @param id the ID of the SKU status to be removed
     * @return {@code true} if the SKU status was removed successfully, {@code false} otherwise
     * @throws CoreException if the SKU status does not exist or cannot be deleted
     */
    boolean remove(Integer id) throws CoreException;

    /**
     * Retrieves all existing ProductSkuStatus entries.
     *
     * @return a list of all {@link ProductSkuStatusDto} instances
     * @throws CoreException if an error occurs during the retrieval
     */
    List<ProductSkuStatusDto> findAll() throws CoreException;

    /**
     * Finds a ProductSkuStatus by its status string.
     *
     * @param status the status value to search for
     * @return an {@link Optional} containing the matching {@link ProductSkuStatusDto},
     *         or empty if not found
     * @throws CoreException if an error occurs during the lookup
     */
    Optional<ProductSkuStatusDto> findByStatus(String status) throws CoreException;

    /**
     * Finds a ProductSkuStatus by its ID.
     *
     * @param statusId the ID of the SKU status
     * @return an {@link Optional} containing the matching {@link ProductSkuStatusDto},
     *         or empty if not found
     * @throws CoreException if an error occurs during the lookup
     */
    Optional<ProductSkuStatusDto> findById(Integer statusId) throws CoreException;
}
