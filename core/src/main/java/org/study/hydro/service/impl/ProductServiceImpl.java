package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.ProductDao;
import org.study.hydro.entity.*;
import org.study.hydro.entity.Dto.*;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl extends EntityMapper<ProductDto, Product> implements ProductService {

    private final ProductDao productDao;
    private final static String PRODUCT_BY_ID_NOT_FOUND_ERROR = "Product by id not found.";

    @Autowired
    public ProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public boolean create(ProductDto productDto) throws CoreException {
        return productDao.create(mapToEntityFromDto(productDto, false));
    }

    @Override
    public boolean update(ProductDto productDto) throws CoreException {
        return productDao.update(mapToEntityFromDto(productDto, true));
    }

    @Override
    public boolean remove(int id) throws CoreException {
        Optional<Product> product = productDao.getProductById(id);
        if (product.isPresent()) {
            return productDao.remove(id);
        } else {
            throw new CoreException(PRODUCT_BY_ID_NOT_FOUND_ERROR);
        }
    }

    @Override
    public Optional<ProductDto> findById(int id) throws CoreException {
        return Optional.of(mapToObjectDto(productDao.getProductById(id)
                .orElseThrow(() -> new CoreException(PRODUCT_BY_ID_NOT_FOUND_ERROR))));
    }

    @Override
    public Optional<Product> findProductById(int id) throws CoreException {
        return productDao.getProductById(id);
    }

    @Override
    public List<ProductDto> findAll(int limit, int offset) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsList(limit, offset));
    }

    @Override
    public List<ProductDto> findAllByPressure(int limit, int offset, int pressure) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsByPressure(limit, offset, pressure));
    }

    @Override
    public List<ProductDto> findAllByFlowRate(int limit, int offset, int flowRate) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsByFlowRate(limit, offset, flowRate));
    }

    @Override
    public List<ProductDto> findAllByType(int limit, int offset, ProductTypeDto type) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsByTypeId(limit, offset, type.getProductTypeId()));
    }

    @Override
    public List<ProductDto> findAllByCompany(int limit, int offset, ProductCompanyDto company) throws CoreException {
        return mapToListObjectsDto(productDao.getProductsByCompanyId(limit, offset, company.getProductCompanyDtoId()));
    }

    @Override
    public List<ProductDto> findAllByStorageRack(int limit, int offset, StorageRackDto storageRack) throws CoreException {
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
        prDto.setStockKeepingUnit(object.getStockKeepingUnit());
        prDto.setFlowRate(object.getFlowRate());
        prDto.setPressure(object.getPressure());
        prDto.setPressureMax(object.getPressureMax());
        prDto.setWeight(object.getWeight());
        prDto.setAdditionalInformation(object.getAdditionalInformation());
        prDto.setProductTypeDto(new ProductTypeDto(
                object.getProductType().getProductTypeId(),
                object.getProductType().getName()));

        prDto.setProductCompanyDto(new ProductCompanyDto(
                object.getProductCompany().getProductCompanyId(),
                object.getProductCompany().getName()));
        prDto.setProductConnectionDto(new ProductConnectionDto(
                object.getProductConnection().getProductConnectionId(),
                object.getProductConnection().getSize()));
        prDto.setPathHydraulicScheme(object.getPathHydraulicScheme());

        prDto.setCountryDto(new CountryDto(
                object.getCountryProduct().getCountryId(),
                object.getCountryProduct().getName()));

        prDto.setImagesPaths(convertToPicturesDtoList(object.getPicturePath()));
        prDto.setStorageRackDtoList(convertToStorageRackDtoList(object.getStorageRackList()));

        System.out.println("Product DTO -> " + prDto);

        return prDto;
    }

    @Override
    public Product mapToEntityFromDto(ProductDto objectDto, boolean isUpdate) {
        Product product = new Product();

        if (isUpdate) {
            product.setProductId(objectDto.getProductDtoId());
        }

        product.setCount(objectDto.getCount());
        product.setStockKeepingUnit(objectDto.getStockKeepingUnit());
        product.setFlowRate(objectDto.getFlowRate());
        product.setPressure(objectDto.getPressure());
        product.setPressureMax(objectDto.getPressureMax());
        product.setWeight(objectDto.getWeight());
        product.setPathHydraulicScheme(objectDto.getPathHydraulicScheme());
        product.setAdditionalInformation(objectDto.getAdditionalInformation());
        product.setProductType(new ProductType(
                objectDto.getProductTypeDto().getProductTypeId(),
                objectDto.getProductTypeDto().getName()));
        product.setProductCompany(new ProductCompany(
                objectDto.getProductCompanyDto().getProductCompanyDtoId(),
                objectDto.getProductCompanyDto().getName()));
        product.setProductConnection(new ProductConnection(
                objectDto.getProductConnectionDto().getProductConnectionId(),
                objectDto.getProductConnectionDto().getSize()));
        product.setStorageRackList(convertFromStorageRackDtoList(objectDto.getStorageRackDtoList()));
        product.setCountryProduct(new Country(
                objectDto.getCountryDto().getCountryId(),
                objectDto.getCountryDto().getName()));

        System.out.println("Product  -> " + product);
        return product;
    }

    /**
     * The method converts storage racks objects into storage racks DTO objects.
     * @param storageList the storage racks objects
     * @return the list of storage racks DTO objects.
     */
    private List<StorageRackDto> convertToStorageRackDtoList(List<StorageRack> storageList) {
        List<StorageRackDto> storageDtoList = new ArrayList<>();
        for (StorageRack storageRack : storageList) {
            StorageRackDto storageRackDto = new StorageRackDto();
            storageRackDto.setStorageRackDtoId(storageRack.getStorageRackId());
            storageRackDto.setName(storageRack.getName());
            storageRackDto.setShelfName(storageRack.getShelf().getName());

            storageDtoList.add(storageRackDto);
        }
        return storageDtoList;
    }

    /**
     * The method converts storage racks DTO objects into the list of the storage racks.
     * @param storageDtoList the storage racks DTO objects
     * @return the list of storage racks objects.
     */
    private List<StorageRack> convertFromStorageRackDtoList(List<StorageRackDto> storageDtoList) {
        List<StorageRack> storageList = new ArrayList<>();
        for (StorageRackDto storageRackDto : storageDtoList) {
            StorageRack storageRack = new StorageRack(
                    storageRackDto.getStorageRackDtoId(),
                    storageRackDto.getName(),
                    new Shelf(storageRackDto.getShelfName())
            );

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
}
