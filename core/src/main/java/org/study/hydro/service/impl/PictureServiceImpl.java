package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.PictureDao;
import org.study.hydro.entity.Dto.PictureDto;
import org.study.hydro.entity.Picture;
import org.study.hydro.exception.CoreException;
import org.study.hydro.service.EntityMapper;
import org.study.hydro.service.PictureService;
import org.study.hydro.service.ServiceMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PictureServiceImpl extends EntityMapper<PictureDto, Picture> implements PictureService {

    private final static String PICTURE_NOT_FOUND_BY_ID_ERROR = "Picture not found by id";
    private final static String PRODUCT_BY_ID_NOT_FOUND_FOR_PICTURES_ERROR = "Product not found for the picture";

    private final PictureDao pictureDao;
    private final ServiceMediator serviceMediator;

    @Autowired
    public PictureServiceImpl(PictureDao pictureDao, ServiceMediator serviceMediator) {
        this.pictureDao = pictureDao;
        this.serviceMediator = serviceMediator;
    }

    @Override
    public boolean create(PictureDto pictureDto) throws CoreException {
        return pictureDao.create(mapToEntityFromDto(pictureDto, false));
    }

    @Override
    public boolean remove(int id) throws CoreException {
        Optional<Picture> picture = pictureDao.findById(id);
        if(picture.isPresent()) {
            return pictureDao.remove(id);
        } else {
            throw new CoreException(PICTURE_NOT_FOUND_BY_ID_ERROR);
        }
    }

    @Override
    public List<PictureDto> findPicturesByProductId(int productId) throws CoreException {
        return mapToListObjectsDto(pictureDao.getPicturesByProductId(productId));
    }

    @Override
    public List<PictureDto> findAll(int limit, int offset) throws CoreException {
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
    public Picture mapToEntityFromDto(PictureDto objectDto, boolean isUpdate) {
        Picture picture = new Picture();

        picture.setPictureId(objectDto.getPictureDtoId());

        picture.setProduct(serviceMediator.findProductById(objectDto.getProductId())
                .orElseThrow(() -> new CoreException(PRODUCT_BY_ID_NOT_FOUND_FOR_PICTURES_ERROR)));

        picture.setPath(objectDto.getPath());

        return picture;
    }
}
