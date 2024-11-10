package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.study.hydro.dao.PictureDao;
import org.study.hydro.entity.Dto.PictureDto;
import org.study.hydro.entity.Picture;
import org.study.hydro.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PictureServiceImplTest {

    @Mock
    private PictureDao pictureDao;
    @Mock
    private ProductServiceImpl productService;

    @InjectMocks
    private PictureServiceImpl pictureService;

    private PictureDto pictureDto = new PictureDto();
    private Picture picture = new Picture();
    private List<PictureDto> pictureDtoList = new ArrayList<>();
    private List<Picture> pictureList = new ArrayList<>();

    private int limit = 10;
    private int offset = 0;
    private int someId = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pictureDto.setProductId(1);
        pictureDto.setPath("path");
        pictureDto.setProductId(1);

        pictureDtoList.add(pictureDto);

        picture.setProduct(new Product(someId));

        pictureList.add(picture);

    }

    @Test
    void create() {
        when(productService.findProductById(someId)).thenReturn(Optional.of(new Product(someId)));
        when(pictureDao.create(any(Picture.class))).thenReturn(true);

        boolean condition = pictureService.create(pictureDto);
        assertTrue(condition);
        verify(pictureDao, times(1)).create(any(Picture.class));
    }

    @Test
    void remove() {
        when(pictureDao.findById(1)).thenReturn(Optional.of(new Picture("path", new Product(someId))));
        when(pictureDao.remove(someId)).thenReturn(true);

        boolean condition = pictureService.remove(someId);

        assertTrue(condition);
        verify(pictureDao, times(1)).remove(someId);
    }

    @Test
    void findPicturesByProductId() {
        when(pictureDao.getPicturesByProductId(someId)).thenReturn(pictureList);

        List<PictureDto> list = pictureService.findPicturesByProductId(someId);

        assertNotNull(list);
        verify(pictureDao, times(1)).getPicturesByProductId(someId);
    }

    @Test
    void findAll() {
        when(pictureDao.getPictures(limit, offset)).thenReturn(pictureList);

        List<PictureDto> list = pictureService.findAll(limit, offset);

        assertNotNull(list);
        verify(pictureDao, times(1)).getPictures(limit, offset);
    }
}