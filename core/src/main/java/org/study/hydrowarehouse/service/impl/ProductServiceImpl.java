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
import org.study.hydrowarehouse.mapping.DtoResolver;
import org.study.hydrowarehouse.mapping.EntityMapper;
import org.study.hydrowarehouse.mapping.EntityResolver;
import org.study.hydrowarehouse.service.ProductService;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.utill.filestorage.ImageStorage;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.*;

/**
 * Service implementation for managing {@link Product} entities.
 * <p>
 * Provides business logic and transactional operations for creating, updating, retrieving, and deleting products.
 * Extends {@link EntityMapper} to handle mapping between {@link ProductDto} and {@link Product} entities.
 * </p>
 *
 * @see EntityMapper
 * @see DtoResolver
 * @see ProductService
 * @see Product
 * @see ProductDto
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class ProductServiceImpl extends EntityMapper<ProductDto, Product> implements ProductService {

    @Value("${product-sku-status}")
    private String defaultStatus;

    private final ProductDao productDao;

//    private final ServiceMediator serviceMediator;

    private final ImageStorage imageStorage;

    private final DtoResolver dtoResolver;

    private final EntityResolver entityResolver;

    @Autowired
    public ProductServiceImpl(ProductDao productDao, ServiceMediator serviceMediator, ImageStorage imageStorage,
                              DtoResolver dtoResolver, EntityResolver entityResolver) {
        this.productDao = productDao;
//        this.serviceMediator = serviceMediator;
        this.imageStorage = imageStorage;
        this.dtoResolver = dtoResolver;
        this.entityResolver = entityResolver;
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

        product.setProductCompany(entityResolver.resolveProductCompany(productDto.getProductCompanyDto()));
        product.setProductType(entityResolver.resolveProductType(productDto.getProductTypeDto()));
        product.setProductConnection(entityResolver.resolveProductConnection(productDto.getProductConnectionDto()));

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
            existingProduct.setProductConnection(entityResolver.resolveProductConnection(connectionDto));
        }

        ProductCompanyDto companyDto = productDto.getProductCompanyDto();
        ProductCompany currentCompany = existingProduct.getProductCompany();

        if (companyDto != null &&
                (currentCompany == null || !Objects.equals(currentCompany.getProductCompanyId(), companyDto.getProductCompanyDtoId()))) {
            existingProduct.setProductCompany(entityResolver.resolveProductCompany(companyDto));
        }

        ProductTypeDto typeDto = productDto.getProductTypeDto();
        ProductType currentType = existingProduct.getProductType();

        if (typeDto != null &&
                (currentType == null || !Objects.equals(currentType.getProductTypeId(), typeDto.getProductTypeId()))) {
            existingProduct.setProductType(entityResolver.resolveProductType(typeDto));
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
        if (object == null) return null;
        ProductDto dto = dtoResolver.resolveProductBasicFields(object);

        dto.setProductTypeDto(dtoResolver.resolveProductTypeDto(object.getProductType()));
        dto.setProductCompanyDto(dtoResolver.resolveProductCompanyDto(object.getProductCompany()));
        dto.setProductConnectionDto(dtoResolver.resolveProductConnectionDto(object.getProductConnection()));
        dto.setPathHydraulicScheme(object.getPathHydraulicScheme());
        dto.setImagesPaths(convertToPicturesDtoList(object.getPicturePath()));

        return dto;
    }

    /**
     * The method takes the paths of the pictures and creates a list of paths.
     * @param pictures the list of the pictures objects.
     * @return the list of strings includes pictures paths.
     */
    private List<String> convertToPicturesDtoList(List<Picture> pictures) {
        if (pictures == null) return null;
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
