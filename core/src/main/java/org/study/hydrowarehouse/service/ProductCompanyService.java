package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.entity.Dto.ProductCompanyDto;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The ProductCompanyService interface {@link ProductCompanyService} provides methods for managing product companies.
 * It defines basic operations for creating, updating etc. product companies.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductCompanyService {

    /**
     * Creates a new product company based on the provided data.
     * @param productCompanyDto a {@link ProductCompanyDto} object containing the new product company data.
     * @return true if the product company was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(ProductCompanyDto productCompanyDto) throws CoreException;

    /**
     * Updates the data of an existing product company.
     * @param productCompanyDto a {@link ProductCompanyDto} object containing the updated product company data.
     * @return true if the product company was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean update(ProductCompanyDto productCompanyDto) throws CoreException;

    /**
     * Retrieves a list of all productCompaniesDto.
     * @param limit the maximum number of productCompanyDto to include in the list.
     * @param offset the starting position of the productCompanyDto list.
     * @return a list of all productCompaniesDto as {@link ProductCompanyDto} objects.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductCompanyDto> findAll(int offset, int limit) throws CoreException;

    /**
     * Retrieves list of productCompaniesDto filtered by a specific name value.
     * @param name the name value used to filter the product companies.
     * @return a list of {@link ProductCompanyDto} objects representing the productCompaniesDto that match
     * the specified name value.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<ProductCompanyDto> findAllByName(String name) throws CoreException;

    /**
     * Retrieves a productCompanyDto by its unique identifier.
     * @param id the unique identifier of the product company.
     * @return an Optional object of {@link ProductCompanyDto} object representing the product company
     * with the specified id.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<ProductCompanyDto> findById(int id) throws CoreException;
}
