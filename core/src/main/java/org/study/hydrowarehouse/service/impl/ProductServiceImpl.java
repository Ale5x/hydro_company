package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ProductDao;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.*;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.ProductService;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.utill.filestorage.ImageStorage;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl extends EntityMapper<ProductDto, Product> implements ProductService {

    @Value("${product-sku-status}")
    private String defaultStatus;

    private final ProductDao productDao;

    private final ServiceMediator serviceMediator;

    private final ImageStorage imageStorage;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ServiceMediator serviceMediator, ImageStorage imageStorage) {
        this.productDao = productDao;
        this.serviceMediator = serviceMediator;
        this.imageStorage = imageStorage;
    }

    @Override
    public boolean create(ProductDto productDto) throws CoreException {
        Product product = new Product();
        product.setCount(productDto.getProductSkuDtos().size());
        product.setFlowRate(productDto.getFlowRate());

        product.setPressure(productDto.getPressure());
        product.setPressureMax(productDto.getPressureMax());
        product.setWeight(productDto.getWeight());

        product.setPathHydraulicScheme(productDto.getPathHydraulicScheme());
        product.setAdditionalInformation(productDto.getAdditionalInformation());

        product.setProductCompany(resolveProductCompany(productDto.getProductCompanyDto()));
        product.setProductType(resolveProductType(productDto.getProductTypeDto()));
        product.setProductConnection(resolveProductConnection(productDto.getProductConnectionDto()));

        product.setPicturePath(generatePictureList(productDto.getImagesPaths(), product));
        return productDao.create(product) > 0;
    }

    @Override
    public boolean update(ProductDto productDto) throws CoreException {
        Product existingProduct = productDao.getProductById(productDto.getProductDtoId()).orElseThrow(() -> {
            //logger
            throw new CoreException(String.format(
                                        ExceptionMessages.PRODUCT_BY_ID_NOT_FOUND_MESSAGE,
                                        productDto.getProductDtoId()));
        });

        existingProduct.setFlowRate(StringUtils.isNullNumericObject(productDto.getFlowRate())
                ? existingProduct.getFlowRate() : productDto.getFlowRate());

        existingProduct.setPressure(StringUtils.isNullNumericObject(productDto.getPressure())
                ? existingProduct.getPressure() : productDto.getPressure());
        existingProduct.setPressureMax(StringUtils.isNullNumericObject(productDto.getPressureMax())
                ? existingProduct.getPressureMax() : productDto.getPressureMax());
        existingProduct.setWeight(StringUtils.isNullNumericObject(productDto.getWeight())
                ? existingProduct.getWeight(): productDto.getWeight());

        existingProduct.setPathHydraulicScheme(StringUtils.isBlankOrNullText(productDto.getPathHydraulicScheme())
                ? existingProduct.getPathHydraulicScheme() : productDto.getPathHydraulicScheme());
        existingProduct.setAdditionalInformation(StringUtils.isBlankOrNullText(productDto.getAdditionalInformation())
                ? existingProduct.getAdditionalInformation() : productDto.getAdditionalInformation());

        ProductConnectionDto connectionDto = productDto.getProductConnectionDto();
        ProductConnection currentConnection = existingProduct.getProductConnection();

        if (connectionDto != null && (currentConnection == null
                || !Objects.equals(currentConnection.getProductConnectionId(), connectionDto.getProductConnectionId()))) {
            existingProduct.setProductConnection(resolveProductConnection(connectionDto));
        }

        ProductCompanyDto companyDto = productDto.getProductCompanyDto();
        ProductCompany currentCompany = existingProduct.getProductCompany();

        if (companyDto != null &&
                (currentCompany == null || !Objects.equals(currentCompany.getProductCompanyId(), companyDto.getProductCompanyDtoId()))) {
            existingProduct.setProductCompany(resolveProductCompany(companyDto));
        }

        ProductTypeDto typeDto = productDto.getProductTypeDto();
        ProductType currentType = existingProduct.getProductType();

        if (typeDto != null &&
                (currentType == null || !Objects.equals(currentType.getProductTypeId(), typeDto.getProductTypeId()))) {
            existingProduct.setProductType(resolveProductType(typeDto));
        }
        return productDao.update(existingProduct);
    }

    @Override
    public boolean remove(int id) throws CoreException {
        Product product = productDao.getProductById(id).orElseThrow(
                () -> {
                    //logger
                    throw new CoreException(String.format(
                            ExceptionMessages.PRODUCT_BY_ID_NOT_FOUND_MESSAGE,
                            id));
                });

        if (productDao.remove(product.getProductId())) {
            removeAllProductFiles(product);
            return true;
        }
        return false;
    }

    /**
     * Attempts to delete all image files associated with the given {@link Product}.
     *
     * <p>The method iterates through the list of {@link Picture} objects linked to the product,
     * and uses the {@code imageStorage} service to remove each file by its path.
     * If any file cannot be deleted, a {@link CoreException} is thrown with the list of failed paths.</p>
     *
     * @param product the product whose image files should be removed; must not be {@code null}
     * @throws CoreException if one or more files fail to be deleted
     */
    private void removeAllProductFiles(Product product) {
        List<String> failedPaths = product.getPicturePath().stream()
                .map(Picture::getPath)
                .filter(path -> !imageStorage.removeFile(path))
                .toList();
        if (!failedPaths.isEmpty()) {
            //logging
            throw new CoreException(ExceptionMessages.FILED_REMOVING_FILES_ERROR + failedPaths);
        }
    }

    @Override
    public Optional<ProductDto> findById(int id) throws CoreException {
        Optional<Product> product = productDao.getProductById(id);
        if (product.isEmpty()) {
            //logger
            return Optional.empty();
        }
        return product.map(this::mapToObjectDto);
    }

    @Override
    public Optional<Product> findProductById(int id) throws CoreException {
        return productDao.getProductById(id);
    }

    @Override
    public List<ProductDto> findAll(int offset, int limit, String criteriaStatus) throws CoreException {
        String status = getStatus(criteriaStatus);
        return mapToListObjectsDto(productDao.getProductsList(limit, offset, status));
    }

    @Override
    public List<ProductDto> findAllByPressure(int offset, int limit, int pressure, String criteriaStatus) throws CoreException {
        String status = getStatus(criteriaStatus);
        return mapToListObjectsDto(productDao.getProductsByPressure(limit, offset, pressure, status));
    }

    @Override
    public List<ProductDto> findAllByFlowRate(int limit, int offset, int flowRate, String criteriaStatus) throws CoreException {
        String status = getStatus(criteriaStatus);
        return mapToListObjectsDto(productDao.getProductsByFlowRate(limit, offset, flowRate, status));
    }

    @Override
    public List<ProductDto> findAllByType(int offset, int limit, ProductTypeDto type, String criteriaStatus) throws CoreException {
        String status = getStatus(criteriaStatus);
        return mapToListObjectsDto(productDao.getProductsByTypeId(limit, offset, type.getProductTypeId(), status));
    }

    @Override
    public List<ProductDto> findAllByCompany(int offset, int limit, ProductCompanyDto company, String criteriaStatus) throws CoreException {
        String status = getStatus(criteriaStatus);
        return mapToListObjectsDto(productDao.getProductsByCompanyId(limit, offset, company.getProductCompanyDtoId(), status));
    }

    @Override
    public List<ProductDto> findAllByStorageRack(int offset, int limit, StorageRackDto storageRack, String criteriaStatus) throws CoreException {
        String status = getStatus(criteriaStatus);
        return mapToListObjectsDto(productDao.getProductsByStorageRackName(limit, offset, storageRack.getName(), status));
    }

    @Override
    public List<ProductDto> mapToListObjectsDto(List<Product> objectsList) {
        if (objectsList == null) return null;
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : objectsList) {
            productDtoList.add(mapToObjectDto(product));
        }
        return productDtoList;
    }

    @Override
    public ProductDto mapToObjectDto(Product object) {
        ProductDto dto = new ProductDto();

        resolveBasicFields(dto, object);
        dto.setProductTypeDto(resolveProductTypeDto(object.getProductType()));
        dto.setProductCompanyDto(resolveProductCompanyDto(object.getProductCompany()));
        dto.setProductConnectionDto(resolveProductConnectionDto(object.getProductConnection()));
        dto.setPathHydraulicScheme(object.getPathHydraulicScheme());
        dto.setImagesPaths(convertToPicturesDtoList(object.getPicturePath()));
        dto.setProductSkuDtos(resolveProductSkuDto(object.getProductSkus()));

        return dto;
    }

    /**
     * Converts the ProductType entity to ProductTypeDto.
     *
     * @param productType the source ProductType entity
     * @return the mapped ProductTypeDto, or null if input is null
     */
    private ProductTypeDto resolveProductTypeDto(ProductType productType) {
        if (productType == null) return null;
        return new ProductTypeDto(productType.getProductTypeId(), productType.getName());
    }

    /**
     * Converts the ProductCompany entity to ProductCompanyDto, including associated countries.
     *
     * @param productCompany the source ProductCompany entity
     * @return the mapped ProductCompanyDto, or null if input is null
     */
    private ProductCompanyDto resolveProductCompanyDto(ProductCompany productCompany) {
        if (productCompany == null) return null;
        return new ProductCompanyDto(
                productCompany.getProductCompanyId(),
                productCompany.getName(),
                toCountryDtoList(productCompany.getCompanyCountries())
        );
    }

    /**
     * Converts the ProductConnection entity to ProductConnectionDto.
     *
     * @param connection the source ProductConnection entity
     * @return the mapped ProductConnectionDto, or null if input is null
     */
    private ProductConnectionDto resolveProductConnectionDto(ProductConnection connection) {
        if (connection == null) return null;
        return new ProductConnectionDto(connection.getProductConnectionId(), connection.getSize());
    }


    /**
     * Sets the basic fields of the product such as ID, count, pressure, etc.
     *
     * @param dto    the target ProductDto
     * @param entity the source Product entity
     */
    private void resolveBasicFields(ProductDto dto, Product entity) {
        dto.setProductDtoId(entity.getProductId());
        dto.setCount(entity.getCount());
        dto.setFlowRate(entity.getFlowRate());
        dto.setPressure(entity.getPressure());
        dto.setPressureMax(entity.getPressureMax());
        dto.setWeight(entity.getWeight());
        dto.setAdditionalInformation(entity.getAdditionalInformation());
    }

    /**
     * The method converts storage racks DTO objects into the list of the storage racks.
     * @param storageDtoList the storage racks DTO objects
     * @return the list of storage racks objects.
     */
    private List<StorageRack> convertFromStorageRackDtoList(List<StorageRackDto> storageDtoList) {
        List<StorageRack> storageList = new ArrayList<>();
        for (StorageRackDto storageRackDto : storageDtoList) {
            StorageRack storageRack = serviceMediator.findStorageRackById(storageRackDto.getStorageRackDtoId())
                    .orElseThrow(() -> {
                        //logger
                        throw new CoreException(String.format(
                                                    ExceptionMessages.STORAGE_RACK_BY_ID_NOT_FOUND_MESSAGE,
                                                    storageRackDto.getStorageRackDtoId()));
                    });
            storageList.add(storageRack);
        }
        return storageList;
    }

    /**
     * The method takes the paths of the pictures and creates a list of paths.
     * @param pictures the list of the pictures objects.
     * @return the list of strings includes pictures paths.
     */
    private List<String> convertToPicturesDtoList(List<Picture> pictures) {
        List<String> picturePathsList = new ArrayList<>();
        if (pictures != null) {
            for (Picture picture : pictures) {
                picturePathsList.add(picture.getPath());
            }
        }
        return picturePathsList;
    }

    /**
     * Generates a list of {@link Picture} entities based on the given list of image paths.
     * Optionally associates each picture with a {@link Product} if a valid product ID is provided.
     *
     * @param picturesPaths list of image file paths to be converted into Picture entities
     * @return a list of {@link Picture} objects with paths and optional product reference
     */
    private List<Picture> generatePictureList(List<String> picturesPaths, Product product) {
        if (picturesPaths == null) return null;
        List<Picture> pictures = new ArrayList<>();
        for(String path : picturesPaths) {
            Picture picture = new Picture(path);
            picture.setProduct(product);
            pictures.add(picture);
        }
        return pictures;
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
    private ProductCompany resolveProductCompany(ProductCompanyDto dto) {
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
    private ProductType resolveProductType(ProductTypeDto dto) {
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
     * Converts a list of {@link ProductSku} entities to a list of corresponding {@link ProductSkuDto} objects.
     *
     * <p>Each {@code ProductSku} is mapped to a {@code ProductSkuDto} by copying its basic fields and resolving
     * nested entities using helper methods like {@code resolveShelfDto}, {@code resolveCountryDto}, and
     * {@code resolveProductSkuStatusDto}.</p>
     *
     * @param productSkuList the list of {@link ProductSku} entities to convert; may be {@code null}
     * @return a list of {@link ProductSkuDto} objects, or {@code null} if {@code productSkuList} is {@code null}
     */
    private List<ProductSkuDto> resolveProductSkuDto(List<ProductSku> productSkuList) {
        if (productSkuList == null) return null;
        List<ProductSkuDto> skuDtoList = new ArrayList<>();
        for (ProductSku sku : productSkuList) {
            ProductSkuDto skuDto = new ProductSkuDto();

            skuDto.setProductSkuDtoId(sku.getProductSkuId());
            skuDto.setCode(sku.getCode());
            skuDto.setShelfDto(resolveShelfDto(sku.getShelf()));
            skuDto.setCountryDto(resolveCountryDto(sku.getCountry()));
            skuDto.setStatus(resolveProductSkuStatusDto(sku.getStatus()));

            skuDtoList.add(skuDto);
        }
        return skuDtoList;
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
    private ProductSkuStatusDto resolveProductSkuStatusDto(ProductSkuStatus skuStatus) {
        if (skuStatus == null) return null;
        ProductSkuStatusDto skuStatusDto = new ProductSkuStatusDto();
        skuStatusDto.setProductSkuStatusDtoId(skuStatus.getProductSkuStatusId());
        skuStatusDto.setStatus(skuStatus.getStatus());
        return skuStatusDto;
    }

    /**
     * Converts the Shelf entity to ShelfDto.
     *
     * @param shelf the source Shelf entity
     * @return the mapped ShelfDto, or null if input is null
     */
    private ShelfDto resolveShelfDto(Shelf shelf) {
        if (shelf == null) return null;
        ShelfDto shelfDto = new ShelfDto();
        shelfDto.setShelfDtoId(shelf.getShelfId());
        shelfDto.setName(shelf.getName());

        shelfDto.setStorageRackDto(resolveStorageRackDto(shelf.getStorageRack()));
        return shelfDto;
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
    private StorageRackDto resolveStorageRackDto(StorageRack storageRack) {
        if (storageRack == null) return null;
        StorageRackDto storageRackDto = new StorageRackDto();
        storageRackDto.setStorageRackDtoId(storageRack.getRackId());
        storageRackDto.setName(storageRack.getName());
        return storageRackDto;
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
    private ProductConnection resolveProductConnection(ProductConnectionDto dto) {
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
     * Converts a set of Country entities to a list of CountryDto objects.
     *
     * @param countrySet the set of Country entities to convert
     * @return a list of corresponding CountryDto objects
     */
    public List<CountryDto> toCountryDtoList(Set<Country> countrySet) {
        if (countrySet == null || countrySet.isEmpty()) {
            return Collections.emptyList();
        }
        return countrySet.stream()
                .map(this::resolveCountryDto)
                .collect(Collectors.toList());
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
    private CountryDto resolveCountryDto(Country country) {
        if (country == null) return null;
        CountryDto dto = new CountryDto();
        dto.setCountryId(country.getCountryId());
        dto.setName(country.getName());
        return dto;
    }

    /**
     * Returns the provided status if it is not null or empty; otherwise returns the default status.
     *
     * @param status the status value passed from the controller, can be null or empty.
     * @return the given status if present; otherwise, the default status from properties.
     */
    private String getStatus(String status) {
        if (StringUtils.isBlankOrNullText(status)) {
            return defaultStatus;
        }
        return status;
    }
}
