package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.entity.Dto.ProductCompanyDto;
import org.study.hydrowarehouse.entity.Dto.ProductDto;
import org.study.hydrowarehouse.entity.Dto.ProductTypeDto;
import org.study.hydrowarehouse.entity.Dto.StorageRackDto;
import org.study.hydrowarehouse.entity.Product;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The ProductService interface {@link ProductService} provides methods for managing products.
 * It defines basic operations for creating, updating etc. products.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductService {

    /**
     * Creates a new product based on the provided data.
     * @param productDto a {@link ProductDto} object containing the new product data.
     * @return true if the product was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(ProductDto productDto) throws CoreException;

    /**
     * Updates the data of an existing product.
     * @param productDto a {@link ProductDto} object containing the updated product data.
     * @return true if the product was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean update(ProductDto productDto) throws CoreException;

    /**
     * Deletes the data of an existing product.
     * @param id the unique identifier of the product to be deleted.
     * @return true if the product was successfully deleted; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean remove(int id) throws CoreException;

    /**
     * Retrieves a productDto by its unique identifier.
     * @param id the unique identifier of the product.
     * @return an Optional object of {@link ProductDto} object representing the product with the specified id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<ProductDto> findById(int id) throws CoreException;


    /**
     * Retrieves a productDto by its unique identifier.
     * @param id the unique identifier of the product.
     * @return an Optional object of {@link Product} object representing the product with the specified id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<Product> findProductById(int id) throws CoreException;

    /**
     * Retrieves a list of {@link ProductDto} objects with optional filtering by product SKU status.
     *
     * @param offset the index of the first productDto to retrieve.
     * @param limit  the maximum number of productDto to include in the result.
     * @param criteriaStatus the status of the related product SKUs to filter by;
     *               only products having at least one SKU with the given status will be included.
     *               If {@code null} or empty, all products are returned regardless of SKU status.
     *
     * @return a list of {@link ProductDto} objects matching the specified criteria.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAll(int offset, int limit, String criteriaStatus) throws CoreException;

    /**
     * Retrieves a paginated list of {@link ProductDto} filtered by a specific pressure value
     * and product SKU status.
     *
     * @param limit     the maximum number of products to include in the list.
     * @param offset    the starting position of the product list.
     * @param pressure  the pressure value used to filter the products.
     * @param criteriaStatus    the status of associated product SKUs to filter by;
     *                  only products having at least one SKU with the given status will be included.
     * @return a list of {@link ProductDto} objects representing products that match
     *         the specified pressure and status criteria.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByPressure(int offset, int limit, int pressure, String criteriaStatus) throws CoreException;

    /**
     * Retrieves a paginated list of {@link ProductDto} filtered by a specific flowRate value
     * and product SKU status.
     *
     * @param limit     the maximum number of products to include in the list.
     * @param offset    the starting position of the product list.
     * @param flowRate  the flowRate value used to filter the products.
     * @param criteriaStatus    the status of associated product SKUs to filter by;
     *                  only products having at least one SKU with the given status will be included.
     * @return a list of {@link ProductDto} objects representing products that match
     *         the specified flowRate and status criteria.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByFlowRate(int offset, int limit, int flowRate, String criteriaStatus) throws CoreException;

    /**
     * Retrieves a paginated list of {@link ProductDto} filtered by a specific product type
     * and product SKU status.
     *
     * @param limit   the maximum number of products to include in the list.
     * @param offset  the starting position of the product list.
     * @param type    a {@link ProductTypeDto} object representing the product type to filter by.
     * @param criteriaStatus  the status of associated product SKUs to filter by;
     *                only products having at least one SKU with the given status will be included.
     * @return a list of {@link ProductDto} objects representing products that match
     *         the specified product type and status criteria.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByType(int offset, int limit, ProductTypeDto type, String criteriaStatus) throws CoreException;

    /**
     * Retrieves a paginated list of {@link ProductDto} filtered by a specific product company
     * and product SKU status.
     *
     * @param limit    the maximum number of products to include in the list.
     * @param offset   the starting position of the product list.
     * @param company  a {@link ProductCompanyDto} object representing the product company to filter by.
     * @param criteriaStatus   the status of associated product SKUs to filter by;
     *                 only products having at least one SKU with the given status will be included.
     * @return a list of {@link ProductDto} objects representing products that match
     *         the specified product company and status criteria.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByCompany(int offset, int limit, ProductCompanyDto company, String criteriaStatus) throws CoreException;

    /**
     * Retrieves a paginated list of {@link ProductDto} filtered by a specific storage rack
     * and product SKU status.
     *
     * @param limit        the maximum number of products to include in the list.
     * @param offset       the starting position of the product list.
     * @param storageRack  a {@link StorageRackDto} object representing the storage rack to filter by.
     * @param criteriaStatus       the status of associated product SKUs to filter by;
     *                     only products having at least one SKU with the given status will be included.
     * @return a list of {@link ProductDto} objects representing products that match
     *         the specified storage rack and status criteria.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByStorageRack(int offset, int limit, StorageRackDto storageRack, String criteriaStatus) throws CoreException;
}
