package org.study.hydro.service;

import org.study.hydro.entity.Dto.ProductCompanyDto;
import org.study.hydro.entity.Dto.ProductDto;
import org.study.hydro.entity.Dto.ProductTypeDto;
import org.study.hydro.entity.Dto.StorageRackDto;
import org.study.hydro.entity.Product;
import org.study.hydro.exception.CoreException;

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
     * Retrieves a list of all productsDto.
     * @param limit the maximum number of productsDto to include in the list.
     * @param offset the starting position of the productDto list.
     * @return a list of all productsDto as {@link ProductDto} objects.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAll(int limit, int offset) throws CoreException;

    /**
     * Retrieves a paginated list of productsDto filtered by a specific pressure value.
     * @param limit the maximum number of productsDto to include in the list.
     * @param offset the starting position of the productDto list.
     * @param pressure the pressure value used to filter the products.
     * @return a list of {@link ProductDto} objects representing the productsDto that match the specified pressure value.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByPressure(int limit, int offset, int pressure) throws CoreException;

    /**
     * Retrieves a paginated list of productsDto filtered by a specific flowRate value.
     * @param limit the maximum number of productsDto to include in the list.
     * @param offset the starting position of the productDto list.
     * @param flowRate the flowRate value used to filter the products.
     * @return a list of {@link ProductDto} objects representing the productsDto that match the specified flowRate value.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByFlowRate(int limit, int offset, int flowRate) throws CoreException;

    /**
     * Retrieves a paginated list of products filtered by a specific productDto type by its unique identifier.
     * @param limit the maximum number of productsDto to include in the list.
     * @param offset the starting position of the productDto list.
     * @param type a {@link ProductTypeDto} object containing the data of the product type.
     * @return a list of {@link ProductDto} objects representing the productsDto that match the specified product type by
     * its unique identifier.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByType(int limit, int offset, ProductTypeDto type) throws CoreException;

    /**
     * Retrieves a paginated list of productsDto filtered by a specific product company by its unique identifier.
     * @param limit the maximum number of productsDto to include in the list.
     * @param offset the starting position of the productDto list.
     * @param company a {@link ProductCompanyDto} object containing the data of the product company.
     * @return a list of {@link ProductDto} objects representing the productsDto that match the specified product
     * company by its unique identifier.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByCompany(int limit, int offset, ProductCompanyDto company) throws CoreException;

    /**
     * Retrieves a paginated list of productsDto filtered by a specific storage rack by its unique name.
     * @param limit the maximum number of productsDto to include in the list.
     * @param offset the starting position of the productDto list.
     * @param storageRack a {@link StorageRackDto} object containing the data of the storage rack.
     * @return a list of {@link ProductDto} objects representing the productsDto that match the specified storage rack by
     * its unique name.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductDto> findAllByStorageRack(int limit, int offset, StorageRackDto storageRack) throws CoreException;
}
