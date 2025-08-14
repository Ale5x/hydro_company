package org.study.hydrowarehouse.mapping;

import org.springframework.stereotype.Component;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.*;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.service.ServiceMediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Component responsible for converting various domain entities into their corresponding Data Transfer Objects (DTOs).
 * <p>
 * This class provides methods to map entities such as {@link Country}, {@link Picture}, {@link ProductSku}, {@link Product},
 * {@link ProductSkuStatus}, {@link ProductSku}, {@link UserCompany}, {@link User}, and {@link UserStatus},
 * {@link ProductCompany}, {@link ProductType}, {@link Shelf}, {@link StorageRack}, {@link ProductConnection} into their
 * respective DTO representations. It handles null checks by returning empty DTO instances when the source entity is null.
 * <p>
 * This helps to decouple the domain model from the data transfer layer, facilitating cleaner architecture and easier
 * data handling.
 *
 * @author Aliaksandr Pishchala
 */
@Component
public class DtoResolver {

    private final ServiceMediator serviceMediator;

    public DtoResolver(ServiceMediator serviceMediator) {
        this.serviceMediator = serviceMediator;
    }

    /**
     * Converts a {@link Country} entity to its corresponding {@link CountryDto}.
     * <p>
     * This method extracts the identifier and name from the {@code Country} object
     * and maps them into a new instance of {@code CountryDto}.
     * </p>
     *
     * @param country the {@code Country} entity to convert; must not be {@code null}
     * @return a {@code CountryDto} containing mapped data from the input entity
     * @throws NullPointerException if the input {@code country} is {@code null}
     */
    public CountryDto resolveCountryDto(Country country) {
        if (country == null) return null;

        CountryDto dto = new CountryDto();
        dto.setCountryId(country.getCountryId());
        dto.setName(country.getName());
        return dto;
    }

    /**
     * Converts a set of Country entities to a list of CountryDto objects.
     *
     * @param countrySet the set of Country entities to convert
     * @return a list of corresponding CountryDto objects
     */
    public List<CountryDto> resolveCountryDtoList(Set<Country> countrySet) {
        if (countrySet == null || countrySet.isEmpty()) {
            return Collections.emptyList();
        }
        return countrySet.stream()
                .map(this::resolveCountryDto)
                .collect(Collectors.toList());
    }

    /**
     * Converts a {@link StorageRack} entity to its corresponding {@link StorageRackDto}.
     *
     * <p>If the input {@code storageRack} is {@code null}, this method returns {@code null}.
     * Otherwise, it maps the entity's fields to a new DTO instance.</p>
     *
     * @param storageRack the {@link StorageRack} entity to be converted
     * @return the corresponding {@link StorageRackDto}, or {@code null} if the input is {@code null}
     */
    public StorageRackDto resolveStorageRackDto(StorageRack storageRack) {
        if (storageRack == null) return null;

        StorageRackDto storageRackDto = new StorageRackDto();
        storageRackDto.setStorageRackDtoId(storageRack.getRackId());
        storageRackDto.setName(storageRack.getName());
        return storageRackDto;
    }

    /**
     * Converts the Shelf entity to ShelfDto.
     *
     * @param shelf the source Shelf entity
     * @return the mapped ShelfDto, or null if input is null
     */
    public ShelfDto resolveShelfDto(Shelf shelf) {
        if (shelf == null) return new ShelfDto();

        ShelfDto shelfDto = new ShelfDto();
        shelfDto.setShelfDtoId(shelf.getShelfId());
        shelfDto.setName(shelf.getName());
        return shelfDto;
    }

    /**
     * Converts a {@link Picture} entity into a {@link PictureDto}.
     * <p>
     * If the provided {@code picture} is {@code null}, a new empty {@link PictureDto}
     * instance is returned. Otherwise, the method maps the following fields:
     * <ul>
     *   <li>{@code pictureId} → {@code pictureDtoId}</li>
     *   <li>{@code path} → {@code path}</li>
     *   <li>{@code product.productId} → {@code productId}</li>
     * </ul>
     *
     * @param picture the {@link Picture} entity to convert, may be {@code null}
     * @return a {@link PictureDto} with mapped fields, or an empty {@link PictureDto} if the input is {@code null}
     */
    public PictureDto resolvePictureDto(Picture picture) {
        if (picture == null) return new PictureDto();
        PictureDto pictureDto = new PictureDto();

        pictureDto.setPictureDtoId(picture.getPictureId());
        pictureDto.setPath(picture.getPath());
        pictureDto.setProductId(picture.getProduct().getProductId());
        return pictureDto;
    }

    /**
     * Converts a {@link ProductSkuStatus} entity to its corresponding {@link ProductSkuStatusDto}.
     *
     * <p>This method is used to map entity fields to a DTO object. If the input {@code skuStatus} is {@code null},
     * the method returns {@code null}.</p>
     *
     * @param skuStatus the {@link ProductSkuStatus} entity to convert
     * @return the corresponding {@link ProductSkuStatusDto}, or {@code null} if the input is {@code null}
     */
    public ProductSkuStatusDto resolveProductSkuStatusDto(ProductSkuStatus skuStatus) {
        if (skuStatus == null) return null;

        ProductSkuStatusDto skuStatusDto = new ProductSkuStatusDto();
        skuStatusDto.setProductSkuStatusDtoId(skuStatus.getProductSkuStatusId());
        skuStatusDto.setStatus(skuStatus.getStatus());
        return skuStatusDto;
    }

    /**
     * Converts a list of {@link ProductSku} entities to a list of corresponding {@link ProductSkuDto} objects.
     *
     * <p>Each {@code ProductSku} is mapped to a {@code ProductSkuDto} by copying its basic fields and resolving
     * nested entities using helper methods like {@code resolveShelfDto}, {@code resolveCountryDto}, and
     * {@code resolveProductSkuStatusDto}.</p>
     *
     * @param productSkuList the list of {@link ProductSku} entities to convert; may be {@code null}
     * @return a list of {@link ProductSkuDto} objects, or {@code null} if {@code productSkuList} is {@code null}
     */
    public List<ProductSkuDto> resolveProductSkuDtoList(List<ProductSku> productSkuList) {
        if (productSkuList == null) return null;
        List<ProductSkuDto> skuDtoList = new ArrayList<>();
        for (ProductSku sku : productSkuList) {
            ProductSkuDto skuDto = new ProductSkuDto();

            skuDto.setProductSkuDtoId(sku.getProductSkuId());
            skuDto.setCode(sku.getCode());
            skuDto.setCountryDto(resolveCountryDto(sku.getCountry()));
            skuDto.setStatus(resolveProductSkuStatusDto(sku.getStatus()));

            skuDtoList.add(skuDto);
        }
        return skuDtoList;
    }

    /**
     * Converts a {@link ProductSku} entity into a {@link ProductSkuDto}.
     * <p>
     * If the provided {@code sku} is {@code null}, a new empty {@link ProductSkuDto}
     * instance is returned. Otherwise, the method maps the following fields:
     * <ul>
     *   <li>{@code productSkuId} → {@code productSkuDtoId}</li>
     *   <li>{@code code} → {@code code}</li>
     * </ul>
     *
     * @param sku the {@link ProductSku} entity to convert, may be {@code null}
     * @return a {@link ProductSkuDto} with mapped fields, or an empty {@link ProductSkuDto} if the input is {@code null}
     * @throws CoreException if an error occurs during the conversion process
     */
    public ProductSkuDto resolveProductSkuDto(ProductSku sku) throws CoreException {
        if (sku == null) return new ProductSkuDto();

        ProductSkuDto skuDto = new ProductSkuDto();
        skuDto.setProductSkuDtoId(sku.getProductSkuId());
        skuDto.setCode(sku.getCode());

        return skuDto;
    }

    /**
     * Converts a {@link Product} entity to a {@link ProductDto}, mapping its basic fields.
     * <p>
     * If the provided {@code entity} is {@code null}, returns a new empty {@link ProductDto}.
     * The following fields are mapped from the entity to the DTO:
     * <ul>
     *   <li>{@code productId} → {@code productDtoId}</li>
     *   <li>{@code count} → {@code count}</li>
     *   <li>{@code flowRate} → {@code flowRate}</li>
     *   <li>{@code pressure} → {@code pressure}</li>
     *   <li>{@code pressureMax} → {@code pressureMax}</li>
     *   <li>{@code weight} → {@code weight}</li>
     *   <li>{@code additionalInformation} → {@code additionalInformation}</li>
     * </ul>
     *
     * @param entity the {@link Product} entity to convert, may be {@code null}
     * @return a {@link ProductDto} with mapped basic fields, or an empty {@link ProductDto} if the input is {@code null}
     */
    public ProductDto resolveProductBasicFields(Product entity) {
        if (entity == null) return new ProductDto();

        ProductDto dto = new ProductDto();

        dto.setProductDtoId(entity.getProductId());
        dto.setCount(entity.getCount());
        dto.setFlowRate(entity.getFlowRate());
        dto.setPressure(entity.getPressure());
        dto.setPressureMax(entity.getPressureMax());
        dto.setWeight(entity.getWeight());
        dto.setAdditionalInformation(entity.getAdditionalInformation());

        return dto;
    }

    /**
     * Converts the ProductConnection entity to ProductConnectionDto.
     *
     * @param connection the source ProductConnection entity
     * @return the mapped ProductConnectionDto, or null if input is null
     */
    public ProductConnectionDto resolveProductConnectionDto(ProductConnection connection) {
        if (connection == null) return new ProductConnectionDto();
        return new ProductConnectionDto(connection.getProductConnectionId(), connection.getSize());
    }

    /**
     * Converts the ProductType entity to ProductTypeDto.
     *
     * @param productType the source ProductType entity
     * @return the mapped ProductTypeDto, or null if input is null
     */
    public ProductTypeDto resolveProductTypeDto(ProductType productType) {
        if (productType == null) return new ProductTypeDto();
        return new ProductTypeDto(productType.getProductTypeId(), productType.getName());
    }

    /**
     * Converts the ProductCompany entity to ProductCompanyDto, including associated countries.
     *
     * @param productCompany the source ProductCompany entity
     * @return the mapped ProductCompanyDto, or null if input is null
     */
    public ProductCompanyDto resolveProductCompanyDto(ProductCompany productCompany) {
        if (productCompany == null) return new ProductCompanyDto();
        return new ProductCompanyDto(
                productCompany.getProductCompanyId(),
                productCompany.getName(),
                resolveCountryDtoList(productCompany.getCompanyCountries())
        );
    }

    /**
     * Converts a {@link ProductSkuStatus} entity into a {@link ProductSkuStatusDto}.
     * <p>
     * If the provided {@code status} is {@code null}, returns a new empty {@link ProductSkuStatusDto}.
     * The following fields are mapped from the entity to the DTO:
     * <ul>
     *   <li>{@code productSkuStatusId} → {@code productSkuStatusDtoId}</li>
     *   <li>{@code status} → {@code status}</li>
     * </ul>
     *
     * @param status the {@link ProductSkuStatus} entity to convert, may be {@code null}
     * @return a {@link ProductSkuStatusDto} with mapped fields, or an empty {@link ProductSkuStatusDto} if the input is {@code null}
     * @throws CoreException if an error occurs during the conversion process
     */
    public ProductSkuStatusDto resolveProductSkuStatus(ProductSkuStatus status) throws CoreException {
        if (status == null) return new ProductSkuStatusDto();
        ProductSkuStatusDto skuDto = new ProductSkuStatusDto();

        skuDto.setProductSkuStatusDtoId(status.getProductSkuStatusId());
        skuDto.setStatus(status.getStatus());
        return skuDto;
    }

    /**
     * Converts a {@link UserCompany} entity into a {@link UserCompanyDto}.
     * <p>
     * If the provided {@code userCompany} is {@code null}, returns a new empty {@link UserCompanyDto}.
     * The following fields are mapped from the entity to the DTO:
     * <ul>
     *   <li>{@code userCompanyId} → {@code companyDtoId}</li>
     *   <li>{@code name} → {@code name}</li>
     *   <li>{@code address} → {@code address}</li>
     * </ul>
     *
     * @param userCompany the {@link UserCompany} entity to convert, may be {@code null}
     * @return a {@link UserCompanyDto} with mapped fields, or an empty {@link UserCompanyDto} if the input is {@code null}
     * @throws CoreException if an error occurs during the conversion process
     */
    public UserCompanyDto resolveUserCompanyDto(UserCompany userCompany) throws CoreException {
        if (userCompany == null) return new UserCompanyDto();
        UserCompanyDto userCompanyDto = new UserCompanyDto();

        userCompanyDto.setCompanyDtoId(userCompany.getUserCompanyId());
        userCompanyDto.setName(userCompany.getName());
        userCompanyDto.setAddress(userCompany.getAddress());

        return userCompanyDto;
    }

    /**
     * Converts a {@link User} entity into a {@link UserDto}.
     * <p>
     * If the provided {@code user} is {@code null}, returns a new empty {@link UserDto}.
     * The following fields are mapped from the entity to the DTO:
     * <ul>
     *   <li>{@code userId} → {@code userDtoId}</li>
     *   <li>{@code firstName} → {@code firstName}</li>
     *   <li>{@code lastName} → {@code lastName}</li>
     *   <li>{@code email} → {@code email}</li>
     *   <li>{@code pathPhoto} → {@code pathPhoto}</li>
     *   <li>{@code registration} → {@code registration}</li>
     *   <li>{@code status.status} → {@code status}</li>
     * </ul>
     * <p>
     * Note: This method assumes {@code user.getStatus()} is not {@code null} and
     * may throw a {@link NullPointerException} if it is.
     *
     * @param user the {@link User} entity to convert, may be {@code null}
     * @return a {@link UserDto} with mapped fields, or an empty {@link UserDto} if the input is {@code null}
     * @throws CoreException if an error occurs during the conversion process
     */
    public UserDto resolveUserDto(User user) throws CoreException {
        if (user == null) return new UserDto();
        UserDto userDto = new UserDto();

        userDto.setUserDtoId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPathPhoto(user.getPathPhoto());
        userDto.setRegistration(user.getRegistration());
        userDto.setStatus(user.getStatus().getStatus());

        return userDto;
    }

    /**
     * Converts a {@link UserStatus} entity into a {@link UserStatusDto}.
     * <p>
     * If the provided {@code status} is {@code null}, returns a new empty {@link UserStatusDto}.
     * The following fields are mapped from the entity to the DTO:
     * <ul>
     *   <li>{@code userStatusId} → {@code userStatusIdDto}</li>
     *   <li>{@code status} → {@code status}</li>
     * </ul>
     *
     * @param status the {@link UserStatus} entity to convert, may be {@code null}
     * @return a {@link UserStatusDto} with mapped fields, or an empty {@link UserStatusDto} if the input is {@code null}
     * @throws CoreException if an error occurs during the conversion process
     */
    public UserStatusDto resolveUserStatusDto(UserStatus status) throws CoreException {
        if (status == null) return new UserStatusDto();

        UserStatusDto userStatusDto = new UserStatusDto();
        userStatusDto.setUserStatusIdDto(status.getUserStatusId());
        userStatusDto.setStatus(status.getStatus());

        return userStatusDto;
    }
}
