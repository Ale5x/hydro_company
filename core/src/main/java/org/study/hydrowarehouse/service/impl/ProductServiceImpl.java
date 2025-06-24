package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.ProductDao;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.*;
import org.study.hydrowarehouse.exception.CoreException;
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

    private final static String PRODUCT_BY_ID_NOT_FOUND_MESSAGE = "Product not found. [id = %s]";
    private final static String PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE = "Product Type not found. [id = %s]";
    private final static String PRODUCT_CONNECTION_BY_ID_NOT_FOUND_MESSAGE = "Product Connection not found. [id = %s]";
    private final static String PRODUCT_COMPANY_BY_ID_NOT_FOUND_MESSAGE = "Product Company not found. [id = %s]";
    private final static String STORAGE_RACK_BY_ID_NOT_FOUND_MESSAGE = "Storage rack not found. [id = %s]";
    private final static String COUNTRY_BY_ID_NOT_FOUND_MESSAGE = "Country not found. [id = %s]";
    private final static String FILED_REMOVING_FILES_ERROR = "Failed to remove some product's files: ";

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
            throw new CoreException(String.format(PRODUCT_BY_ID_NOT_FOUND_MESSAGE, productDto.getProductDtoId()));
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
                () -> new CoreException(PRODUCT_BY_ID_NOT_FOUND_MESSAGE));

        if (productDao.remove(product.getProductId())) {
            removeAllProductFiles(product);
            return true;
        }
        return false;
    }

    private void removeAllProductFiles(Product product) {
        List<String> failedPaths = product.getPicturePath().stream()
                .map(Picture::getPath)
                .filter(path -> !imageStorage.removeFile(path))
                .toList();
        if (!failedPaths.isEmpty()) {
            //logging
            throw new CoreException(FILED_REMOVING_FILES_ERROR + failedPaths);
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
    public List<ProductDto> findAll(int offset, int limit) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsList(limit, offset));
    }

    @Override
    public List<ProductDto> findAllByPressure(int offset, int limit, int pressure) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsByPressure(limit, offset, pressure));
    }

    @Override
    public List<ProductDto> findAllByFlowRate(int limit, int offset, int flowRate) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsByFlowRate(limit, offset, flowRate));
    }

    @Override
    public List<ProductDto> findAllByType(int offset, int limit, ProductTypeDto type) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsByTypeId(limit, offset, type.getProductTypeId()));
    }

    @Override
    public List<ProductDto> findAllByCompany(int offset, int limit, ProductCompanyDto company) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsByCompanyId(limit, offset, company.getProductCompanyDtoId()));
    }

    @Override
    public List<ProductDto> findAllByStorageRack(int offset, int limit, StorageRackDto storageRack) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsByStorageRackName(limit, offset, storageRack.getName()));
    }

    @Override
    public List<ProductDto> mapToListObjectsDto(List<Product> objectsList) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : objectsList) {
            productDtoList.add(mapToObjectDto(product));
        }
        return productDtoList;
    }

    @Override
    public ProductDto mapToObjectDto(Product object) {
        ProductDto prDto = new ProductDto();

        prDto.setProductDtoId(object.getProductId());
        prDto.setCount(object.getCount());

        prDto.setFlowRate(object.getFlowRate());
        prDto.setPressure(object.getPressure());
        prDto.setPressureMax(object.getPressureMax());

        prDto.setWeight(object.getWeight());
        prDto.setAdditionalInformation(object.getAdditionalInformation());
        prDto.setProductTypeDto(new ProductTypeDto(
                object.getProductType().getProductTypeId(),
                object.getProductType().getName()));

        List<CountryDto> countryDtoList = toCountryDtoList(object.getProductCompany().getCompanyCountries());
        prDto.setProductCompanyDto(new ProductCompanyDto(
                object.getProductCompany().getProductCompanyId(),
                object.getProductCompany().getName(),
                toCountryDtoList(object.getProductCompany().getCompanyCountries())));


        prDto.setProductConnectionDto(new ProductConnectionDto(
                object.getProductConnection().getProductConnectionId(),
                object.getProductConnection().getSize()));

        prDto.setPathHydraulicScheme(object.getPathHydraulicScheme());


        prDto.setImagesPaths(convertToPicturesDtoList(object.getPicturePath()));

        return prDto;
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
                        throw new CoreException(String.format(STORAGE_RACK_BY_ID_NOT_FOUND_MESSAGE,
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
                            PRODUCT_COMPANY_BY_ID_NOT_FOUND_MESSAGE,
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
                    throw new CoreException(String.format(PRODUCT_TYPE_BY_ID_NOT_FOUND_MESSAGE,
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
    private ProductConnection resolveProductConnection(ProductConnectionDto dto) {
        if (StringUtils.isNullNumericObject(dto.getProductConnectionId())) {
            return new ProductConnection(dto.getSize());
        }
        return serviceMediator.findProductConnectionById(
                        dto.getProductConnectionId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(PRODUCT_CONNECTION_BY_ID_NOT_FOUND_MESSAGE,
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
                .map(this::mapToCountryDto)
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
    private CountryDto mapToCountryDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setCountryId(country.getCountryId());
        dto.setName(country.getName());
        return dto;
    }
}
