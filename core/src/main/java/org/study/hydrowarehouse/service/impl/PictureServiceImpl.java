package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.PictureDao;
import org.study.hydrowarehouse.entity.Dto.PictureDto;
import org.study.hydrowarehouse.entity.Picture;
import org.study.hydrowarehouse.entity.Product;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.service.EntityMapper;
import org.study.hydrowarehouse.service.PictureService;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.utill.filestorage.ImageStorage;
import org.study.hydrowarehouse.utill.filestorage.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class PictureServiceImpl extends EntityMapper<PictureDto, Picture> implements PictureService {

    private final static String PICTURE_NOT_FOUND_BY_ID_MESSAGE = "Picture not found. [id = %s]";
    private final static String PICTURE_NOT_REMOVE_MESSAGE = "Picture don't remove. [id = %s]";
    private final static String PRODUCT_BY_ID_NOT_FOUND_FOR_PICTURES_MESSAGE = "Product not found for the picture. [id = %s]";

    @Value("${file.limit-pictures}")
    private int maxPhotoLimit;

    private final PictureDao pictureDao;
    private final ServiceMediator serviceMediator;

    private final ImageStorage imageStorage;

    @Autowired
    public PictureServiceImpl(PictureDao pictureDao, ServiceMediator serviceMediator, ImageStorage imageStorage) {
        this.pictureDao = pictureDao;
        this.serviceMediator = serviceMediator;
        this.imageStorage = imageStorage;
    }

    @Override
    public boolean create(PictureDto pictureDto) throws CoreException {
        Picture picture = new Picture();

        picture.setProduct(serviceMediator.findProductById(pictureDto.getProductId())
                .orElseThrow(() -> new CoreException(
                        String.format(PRODUCT_BY_ID_NOT_FOUND_FOR_PICTURES_MESSAGE, pictureDto.getProductId()))));

        picture.setPath(pictureDto.getPath());
        validatePhotoCountLimit(picture.getProduct().getProductId());
        return pictureDao.create(picture) > 0;
    }

    @Override
    public boolean update(PictureDto pictureDto) throws CoreException {
        Picture existingPicture = pictureDao.findById(pictureDto.getPictureDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(PICTURE_NOT_FOUND_BY_ID_MESSAGE, pictureDto.getPictureDtoId()));
        });
        if (!StringUtils.isBlankOrNullText(pictureDto.getPath())
                && !pictureDto.getPath().equals(existingPicture.getPath())) {
            imageStorage.removeFile(existingPicture.getPath());
            existingPicture.setPath(pictureDto.getPath());
        }

        Product product = serviceMediator.findProductById(pictureDto.getProductId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(
                            String.format(PRODUCT_BY_ID_NOT_FOUND_FOR_PICTURES_MESSAGE, pictureDto.getProductId()));
                });
        existingPicture.setProduct(existingPicture.getProduct().getProductId().equals(product.getProductId())
                ? existingPicture.getProduct() : product);
        return pictureDao.update(existingPicture);
    }

    @Override
    public boolean remove(int id) throws CoreException {
        Picture picture = pictureDao.findById(id)
                .orElseThrow(() -> new CoreException(String.format(PICTURE_NOT_FOUND_BY_ID_MESSAGE, id)));

        boolean isRemoved = pictureDao.remove(id);
        if (isRemoved) {
            return imageStorage.removeFile(picture.getPath());
        } else {
            throw new CoreException(String.format(PICTURE_NOT_REMOVE_MESSAGE, id));
        }
    }

    @Override
    public List<PictureDto> findPicturesByProductId(int productId) throws CoreException {
        return mapToListObjectsDto(pictureDao.getPicturesByProductId(productId));
    }

    @Override
    public Optional<PictureDto> findPictureById(int id) throws CoreException {
        Optional<Picture> picture = pictureDao.findById(id);
        if(picture.isEmpty()) {
            //logger
            return Optional.empty();
        }
        return picture.map(this::mapToObjectDto);
    }

    @Override
    public List<PictureDto> findAll(int offset, int limit) throws CoreException {
        return mapToListObjectsDto(pictureDao.getPictures(limit, offset));
    }

    @Override
    public List<PictureDto> mapToListObjectsDto(List<Picture> objectsDtoList) {
        List<PictureDto> pictureDtoList = new ArrayList<>();
        for (Picture picture : objectsDtoList) {
            pictureDtoList.add(mapToObjectDto(picture));
        }
        return pictureDtoList;
    }

    @Override
    public PictureDto mapToObjectDto(Picture object) {
        PictureDto pictureDto = new PictureDto();

        pictureDto.setPictureDtoId(object.getPictureId());
        pictureDto.setPath(object.getPath());
        pictureDto.setProductId(object.getProduct().getProductId());
        return pictureDto;
    }

    @Override
    public List<Picture> findAllPicturesByProductId(int productId) throws CoreException {
        return pictureDao.getPicturesByProductId(productId);
    }

    @Override
    public int getMaxPhotoLimit() {
        return maxPhotoLimit;
    }
}
