package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.entity.Dto.ProductDto;
import org.study.hydrowarehouse.entity.Dto.ProductSkuDto;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing Product SKU entities.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductSkuService {

    /**
     * Saves a new Product SKU.
     *
     * @param skuDto the ProductSkuDto to be saved; must not be null
     * @return {@code true} if the SKU was successfully saved, {@code false} otherwise
     * @throws CoreException if an error occurs during the save operation
     */
    boolean save(ProductSkuDto skuDto) throws CoreException;

    /**
     * Updates an existing Product SKU.
     *
     * @param skuDto the ProductSkuDto with updated information; must not be null
     * @return {@code true} if the SKU was successfully updated, {@code false} otherwise
     * @throws CoreException if the SKU does not exist or update fails
     */
    boolean update(ProductSkuDto skuDto) throws CoreException;

    /**
     * Updates the status of an existing Product SKU.
     *
     * @param skuDto the ProductSkuDto whose status is to be updated; must not be null
     * @param status the new status string; must not be null or empty
     * @return {@code true} if the status was successfully updated, {@code false} otherwise
     * @throws CoreException if the SKU does not exist or update fails
     */
    boolean updateStatus(ProductSkuDto skuDto, String status) throws CoreException;

    /**
     * Removes a Product SKU by its ID.
     *
     * @param id the ID of the Product SKU to be removed; must not be null
     * @return {@code true} if the SKU was successfully removed, {@code false} otherwise
     * @throws CoreException if the SKU does not exist or removal fails
     */
    boolean remove(Integer id) throws CoreException;

    /**
     * Finds a Product SKU by its ID.
     *
     * @param id the ID of the Product SKU to find; must not be null
     * @return an {@link Optional} containing the found ProductSkuDto, or empty if not found
     * @throws CoreException if an error occurs during the search
     */
    Optional<ProductSkuDto> findById(Integer id) throws CoreException;

    /**
     * Finds all Product SKUs matching the given code.
     *
     * @param code the SKU code to search for; must not be null or empty
     * @return a list of ProductSkuDto objects with the specified code; may be empty
     * @throws CoreException if an error occurs during the search
     */
    List<ProductSkuDto> findByCode(String code) throws CoreException;

    /**
     * Finds all Product SKUs with the specified status, using pagination.
     *
     * @param limit the maximum number of results to return; must be greater than 0
     * @param offset the index of the first result to return; must be 0 or greater
     * @param status the status to filter by; must not be null or empty
     * @return a list of ProductSkuDto objects with the specified status; may be empty
     * @throws CoreException if an error occurs during the search
     */
    List<ProductSkuDto> findAllByStatus(int limit, int offset, String status) throws CoreException;

    /**
     * Finds all Product SKUs associated with the given product, using pagination.
     *
     * @param limit the maximum number of results to return; must be greater than 0
     * @param offset the index of the first result to return; must be 0 or greater
     * @param product the product to filter by; must not be null
     * @return a list of ProductSkuDto objects related to the specified product; may be empty
     * @throws CoreException if an error occurs during the search
     */
    List<ProductSkuDto> findAllByProduct(int limit, int offset, ProductDto product) throws CoreException;
}
