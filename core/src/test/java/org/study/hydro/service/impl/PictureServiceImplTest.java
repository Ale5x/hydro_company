package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.study.hydro.dao.PictureDao;
import org.study.hydro.entity.Dto.PictureDto;
import org.study.hydro.entity.Picture;
import org.study.hydro.entity.Product;
import org.study.hydro.service.ServiceMediator;

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
    private ServiceMediator serviceMediator;

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

        ReflectionTestUtils.setField(pictureService, "maxPhotoLimit", 10);

    }

    @Test
    void create() {
        when(serviceMediator.findProductById(someId)).thenReturn(Optional.of(new Product(someId)));
        when(pictureService.findAllPicturesByProductId(someId)).thenReturn(pictureList);
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
    void findPicturesById() {
        when(pictureDao.findById(someId)).thenReturn(Optional.of(picture));

        Optional<PictureDto> pictureDtoOptional = pictureService.findPictureById(someId);

        assertTrue(pictureDtoOptional.isPresent());
        verify(pictureDao, times(1)).findById(someId);
    }

    @Test
    void findAll() {
        when(pictureDao.getPictures(limit, offset)).thenReturn(pictureList);

        List<PictureDto> list = pictureService.findAll(offset, limit);

        assertNotNull(list);
        verify(pictureDao, times(1)).getPictures(limit, offset);
    }

    @Test
    void getMaxPhotoLimit() {
        int expected = 10;
        int actual = pictureService.getMaxPhotoLimit();
        assertEquals(expected, actual);
    }
}