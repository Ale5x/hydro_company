package org.study.hydrowarehouse.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.*;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.utill.StringUtils;

/**
 * Component responsible for resolving domain entities from their corresponding Data Transfer Objects (DTOs).
 * <p>
 * This class provides methods to convert DTOs such as {@link ProductCompanyDto}, {@link ProductTypeDto},
 * {@link ProductConnectionDto}, {@link CountryDto}, {@link ProductDto}, {@link ShelfDto}, and {@link StorageRackDto}
 * into their respective domain entities.
 * <p>
 * Resolution logic includes:
 * <ul>
 *   <li>Creation of new entity instances when DTO IDs are missing or null.</li>
 *   <li>Fetching existing entities from the database via {@link ServiceMediator} when IDs are provided.</li>
 *   <li>Throwing {@link CoreException} with appropriate messages if an entity cannot be found by its ID.</li>
 * </ul>
 * <p>
 * This abstraction helps maintain clean separation between the DTO layer and the persistence layer,
 * centralizing the logic of entity resolution and validation.
 *
 * @author Aliaksandr Pishchala
 */
@Component
public class EntityResolver {

    private final ServiceMediator serviceMediator;

    @Autowired
    public EntityResolver(ServiceMediator serviceMediator) {
        this.serviceMediator = serviceMediator;
    }


    /**
     * Resolves a {@link ProductCompany} based on the provided {@link ProductCompanyDto}.
     * <p>
     * This method checks if the {@code productCompanyDtoId} is null or missing. If the ID is missing,
     * a new {@link ProductCompany} is created using the provided name from the DTO. If the ID is present,
     * it attempts to find the corresponding {@link ProductCompany} by its ID from the database. If no matching
     * company is found, an exception is thrown.
     *
     * @param dto the {@link ProductCompanyDto} containing the company information (name and optional ID)
     * @return a {@link ProductCompany} object either newly created or fetched from the database
     * @throws CoreException if the company ID is provided but no company is found in the database
     */
    public ProductCompany resolveProductCompany(ProductCompanyDto dto) throws CoreException {
        if (StringUtils.isNullNumericObject(dto.getProductCompanyDtoId())) {
            return new ProductCompany(dto.getName());
        }
        return serviceMediator.findProductCompanyById(dto.getProductCompanyDtoId())
                .orElseThrow(() -> {
                    throw new CoreException(String.format(
                            ExceptionMessages.PRODUCT_COMPANY_BY_ID_NOT_FOUND_MESSAGE,
                            dto.getProductCompanyDtoId()));
                });
    }

    /**
     * Resolves a {@link ProductType} based on the provided {@link ProductTypeDto}.
     * <p>
     * This method checks if the {@code productTypeId} is null or missing. If the ID is missing,
     * a new {@link ProductType} is created using the provided name from the DTO. If the ID is present,
     * it attempts to find the corresponding {@link ProductType} by its ID from the database. If no matching
     * type is found, an exception is thrown.
     *
     * @param dto the {@link ProductTypeDto} containing the product type information (name and optional ID)
     * @return a {@link ProductType} object either newly created or fetched from the database
     * @throws CoreException if the product type ID is provided but no matching product type is found in the database
     */
    public ProductType resolveProductType(ProductTypeDto dto) throws CoreException  {
        if (StringUtils.isNullNumericObject(dto.getProductTypeId())) {
            return new ProductType(dto.getName());
        }
        return serviceMediator.findProductTypeById(
                        dto.getProductTypeId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                            ExceptionMessages.PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE,
                            dto.getProductTypeId()));
                });
    }

    /**
     * Resolves a {@link ProductConnection} based on the provided {@link ProductConnectionDto}.
     * <p>
     * This method checks if the {@code productConnectionId} is null or missing. If the ID is missing,
     * a new {@link ProductConnection} is created using the provided size from the DTO. If the ID is present,
     * it attempts to find the corresponding {@link ProductConnection} by its ID from the database. If no matching
     * connection is found, an exception is thrown.
     *
     * @param dto the {@link ProductConnectionDto} containing the product connection information (size and optional ID)
     * @return a {@link ProductConnection} object either newly created or fetched from the database
     * @throws CoreException if the product connection ID is provided but no matching product connection is found in the database
     */
    public ProductConnection resolveProductConnection(ProductConnectionDto dto) throws CoreException  {
        if (StringUtils.isNullNumericObject(dto.getProductConnectionId())) {
            return new ProductConnection(dto.getSize());
        }
        return serviceMediator.findProductConnectionById(
                        dto.getProductConnectionId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                            ExceptionMessages.PRODUCT_CONNECTION_BY_ID_NOT_FOUND_MESSAGE,
                            dto.getProductConnectionId()));
                });
    }

    /**
     * Resolves a Country entity from the given CountryDto.
     *
     * @param dto the CountryDto containing the ID of the country to resolve
     * @return the resolved Country entity
     * @throws CoreException if the ID is null or the Country is not found
     */
    public Country resolveCountry(CountryDto dto) {
        if (dto == null) return null;
        return serviceMediator.findCountryById(dto.getCountryId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                            ExceptionMessages.COUNTRY_BY_ID_NOT_FOUND_MESSAGE,
                            dto.getCountryId()));
                });
    }

    /**
     * Resolves a Product entity from the given ProductDto.
     *
     * @param dto the ProductDto containing the ID of the product to resolve
     * @return the resolved Product entity
     * @throws CoreException if the ID is null or the Product is not found
     */
    public Product resolveProduct(ProductDto dto) {
        if (dto == null) return null;
        return serviceMediator.findProductById(dto.getProductDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                            ExceptionMessages.PRODUCT_BY_ID_NOT_FOUND_MESSAGE,
                            dto.getProductDtoId()));
                });
    }

    /**
     * Resolves a Shelf entity from the given ShelfDto.
     *
     * @param dto the ShelfDto containing the ID of the shelf to resolve
     * @return the resolved Shelf entity
     * @throws CoreException if the ID is null or the Shelf is not found
     */
    public Shelf resolveShelf(ShelfDto dto) {
        if (dto == null) return null;
        return serviceMediator.findShelfById(dto.getShelfDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                            ExceptionMessages.SHELF_BY_ID_NOT_FOUND_MESSAGE,
                            dto.getShelfDtoId(),
                            dto.getName()));
                });
    }

    /**
     * Converts a {@link StorageRackDto} object into a {@link StorageRack} entity.
     *
     * <p>Copies the name from the DTO to the entity. If the DTO contains a non-null ID,
     * it is also set on the resulting entity.</p>
     *
     * @param storageRackDto the DTO object to convert
     * @return a {@link StorageRack} entity with values copied from the DTO
     * @throws CoreException if a mapping error occurs during the conversion
     */
    public StorageRack resolveStorageRack(StorageRackDto storageRackDto) throws CoreException {
        if (storageRackDto == null) return null;
        StorageRack rack = new StorageRack();
        rack.setName(storageRackDto.getName());
        if (storageRackDto.getStorageRackDtoId() != null) {
            rack.setRackId(storageRackDto.getStorageRackDtoId());
        }
        return rack;
    }
}
