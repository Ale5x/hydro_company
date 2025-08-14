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
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.mapping.DtoResolver;
import org.study.hydrowarehouse.mapping.EntityMapper;
import org.study.hydrowarehouse.service.PictureService;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.utill.filestorage.ImageStorage;
import org.study.hydrowarehouse.utill.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing {@link Picture} entities.
 * <p>
 * Provides business logic and transactional operations for creating, updating, retrieving, and deleting pictures
 * associated with products. Extends {@link EntityMapper} to handle mapping between {@link PictureDto} and
 * {@link Picture} entities.
 * </p>
 *
 * @see PictureService
 * @see Picture
 * @see PictureDto
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class PictureServiceImpl extends EntityMapper<PictureDto, Picture> implements PictureService {

    @Value("${file.limit-pictures}")
    private int maxPhotoLimit;

    private final PictureDao pictureDao;
    private final ServiceMediator serviceMediator;
    private final DtoResolver dtoResolver;
    private final ImageStorage imageStorage;

    @Autowired
    public PictureServiceImpl(PictureDao pictureDao, ServiceMediator serviceMediator, DtoResolver dtoResolver,
                              ImageStorage imageStorage) {
        this.pictureDao = pictureDao;
        this.serviceMediator = serviceMediator;
        this.dtoResolver = dtoResolver;
        this.imageStorage = imageStorage;
    }

    @Override
    public boolean create(PictureDto pictureDto) throws CoreException {
        Product product = serviceMediator.findProductById(pictureDto.getProductId())
                .orElseThrow(() -> new CoreException(
                        String.format(ExceptionMessages.PRODUCT_BY_ID_NOT_FOUND_FOR_PICTURES_MESSAGE,
                                pictureDto.getProductId())));

        if (product.getPicturePath().size() >= maxPhotoLimit) {
            throw new CoreException(
                    String.format(ExceptionMessages.PICTURE_LIMIT_EXCEEDED_MESSAGE, product.getProductId()));
        }

        Picture picture = new Picture();
        picture.setProduct(product);
        picture.setPath(pictureDto.getPath());

        return pictureDao.create(picture) > 0;
    }

    @Override
    public boolean update(PictureDto pictureDto) throws CoreException {
        Picture existingPicture = pictureDao.findById(pictureDto.getPictureDtoId())
                .orElseThrow(() -> {
                    //logger
                    throw new CoreException(String.format(
                                                ExceptionMessages.PICTURE_NOT_FOUND_BY_ID_MESSAGE,
                                                pictureDto.getPictureDtoId()));
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
                            String.format(
                                    ExceptionMessages.PRODUCT_BY_ID_NOT_FOUND_FOR_PICTURES_MESSAGE,
                                    pictureDto.getProductId()));
                });
        existingPicture.setProduct(existingPicture.getProduct().getProductId().equals(product.getProductId())
                ? existingPicture.getProduct() : product);
        return pictureDao.update(existingPicture);
    }

    @Override
    public boolean remove(int id) throws CoreException {
        Picture picture = pictureDao.findById(id)
                .orElseThrow(() -> new CoreException(
                        String.format(
                                ExceptionMessages.PICTURE_NOT_FOUND_BY_ID_MESSAGE,
                                id)));

        boolean isRemoved = pictureDao.remove(id);
        if (isRemoved) {
            return imageStorage.removeFile(picture.getPath());
        } else {
            throw new CoreException(String.format(
                    ExceptionMessages.PICTURE_NOT_REMOVE_MESSAGE,
                    id));
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
        if (objectsDtoList == null) return null;
        List<PictureDto> pictureDtoList = new ArrayList<>();
        for (Picture picture : objectsDtoList) {
            pictureDtoList.add(mapToObjectDto(picture));
        }
        return pictureDtoList;
    }

    @Override
    public PictureDto mapToObjectDto(Picture object) {
        return dtoResolver.resolvePictureDto(object);
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
