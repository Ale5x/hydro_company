package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.study.hydrowarehouse.dao.PictureDao;
import org.study.hydrowarehouse.entity.Dto.PictureDto;
import org.study.hydrowarehouse.entity.Picture;
import org.study.hydrowarehouse.entity.Product;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.exception.ExceptionMessages;
import org.study.hydrowarehouse.mapping.DtoResolver;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.utill.filestorage.ImageStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PictureServiceImplTest {

    @Mock
    private PictureDao pictureDao;
    @Mock
    private ServiceMediator serviceMediator;
    @Mock
    private DtoResolver dtoResolver;

    @Mock
    private ImageStorage imageStorage;

    @InjectMocks
    private PictureServiceImpl pictureService;

    private PictureDto pictureDto = new PictureDto();
    private Picture picture = new Picture();
    private Product product = new Product();
    private Product productWithMaxPictures = new Product();
    private Product productWithMinPictures = new Product();
    private List<PictureDto> pictureDtoList = new ArrayList<>();
    private List<Picture> maxPictureList = new ArrayList<>();
    private List<Picture> minPictureList = new ArrayList<>();

    private int maxPhotoLimit = 10;
    private int minPhotoLimit = 5;
    private int limit = 10;
    private int offset = 0;
    private int someId = 1;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);

        pictureDto.setProductId(1);
        pictureDto.setPath("path");
        pictureDto.setProductId(1);

        pictureDtoList.add(pictureDto);

        picture.setProduct(new Product(someId));

        maxPictureList.add(picture);

        ReflectionTestUtils.setField(pictureService, "maxPhotoLimit", 10);

        productWithMaxPictures.setProductId(someId);
        for (int i = 0; i <= maxPhotoLimit; i++) {
            Picture pic = new Picture();
            pic.setPictureId(i);
            pic.setPath("Path -> " + i);
            pic.setProduct(productWithMaxPictures);

            maxPictureList.add(pic);
        }
        productWithMaxPictures.setPicturePath(maxPictureList);

        productWithMinPictures.setProductId(someId);
        for (int i = 0; i <= minPhotoLimit; i++) {
            Picture pic = new Picture();
            pic.setPictureId(i);
            pic.setPath("Path -> " + i);
            pic.setProduct(productWithMinPictures);

            minPictureList.add(pic);
        }
        productWithMinPictures.setPicturePath(minPictureList);


        picture.setPath("some path");

        product.setProductId(someId);
    }

    @Test
    void create() {
        when(serviceMediator.findProductById(someId)).thenReturn(Optional.of(productWithMinPictures));
        when(pictureDao.create(any(Picture.class))).thenReturn(3);

        boolean condition = pictureService.create(pictureDto);
        assertTrue(condition);
        verify(pictureDao, times(1)).create(any(Picture.class));
    }

    @Test
    void create_shouldThrowException_whenMaxPhotoLimitReached() {
           when(serviceMediator.findProductById(someId)).thenReturn(Optional.of(productWithMaxPictures));
        CoreException ex = assertThrows(CoreException.class,
                () -> pictureService.create(pictureDto));

        assertTrue(ex.getMessage().contains(
                String.format(ExceptionMessages.PICTURE_LIMIT_EXCEEDED_MESSAGE, someId)
        ));

        verify(serviceMediator).findProductById(someId);
        verifyNoInteractions(pictureDao);
    }

    @Test
    void testUpdate_shouldUpdateSuccessfully() throws CoreException {
        int pictureId = 1;
        int productId = 10;
        String oldPath = "old/path/image.png";
        String newPath = "new/path/image.png";

        Picture existingPicture = new Picture(oldPath, new Product(productId));
        existingPicture.setPictureId(pictureId);

        PictureDto pictureDto = new PictureDto();
        pictureDto.setPictureDtoId(pictureId);
        pictureDto.setProductId(productId);
        pictureDto.setPath(newPath);

        Product product = new Product(productId);

        when(pictureDao.findById(pictureId)).thenReturn(Optional.of(existingPicture));
        when(serviceMediator.findProductById(productId)).thenReturn(Optional.of(product));
        when(pictureDao.update(any(Picture.class))).thenReturn(true);

        when(imageStorage.removeFile(anyString())).thenReturn(true);

        boolean result = pictureService.update(pictureDto);

        assertTrue(result);
        verify(pictureDao).findById(pictureId);
        verify(serviceMediator).findProductById(productId);
        verify(imageStorage).removeFile(oldPath);
        verify(pictureDao).update(any(Picture.class));
    }

    @Test
    void testUpdate_pictureNotFound_shouldThrowException() {
        PictureDto pictureDto = new PictureDto();
        pictureDto.setPictureDtoId(1);

        when(pictureDao.findById(pictureDto.getPictureDtoId())).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> pictureService.update(pictureDto));
        assertTrue(ex.getMessage().contains("Picture not found"));
    }

    @Test
    void testUpdate_productNotFound_shouldThrowException() {
        PictureDto pictureDto = new PictureDto();
        pictureDto.setPictureDtoId(1);
        pictureDto.setProductId(2);

        Picture existingPicture = new Picture();
        existingPicture.setPictureId(1);
        existingPicture.setPath("oldPath");

        when(pictureDao.findById(pictureDto.getPictureDtoId())).thenReturn(Optional.of(existingPicture));

        when(serviceMediator.findProductById(pictureDto.getProductId())).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> pictureService.update(pictureDto));
        assertTrue(ex.getMessage().contains("Product not found"));
    }

    @Test
    void testRemoveFileWhenPathsDiffer() throws CoreException {
        Product product = new Product();
        product.setProductId(1);

        Picture existingPicture = new Picture();
        existingPicture.setPictureId(1);
        existingPicture.setPath("A");
        existingPicture.setProduct(product);
        existingPicture.getProduct().setProductId(1);

        PictureDto pictureDto = new PictureDto();
        pictureDto.setPictureDtoId(1);
        pictureDto.setProductId(1);
        pictureDto.setPath("B");

        when(pictureDao.findById(1)).thenReturn(Optional.of(existingPicture));
        when(serviceMediator.findProductById(anyInt())).thenReturn(Optional.of(product));

        pictureService.update(pictureDto);

        verify(imageStorage, times(1)).removeFile("A");
    }

    @Test
    void testDoNotRemoveFileWhenPathsAreEqual() throws CoreException {
        Product product = new Product();
        product.setProductId(1);

        Picture existingPicture = new Picture();
        existingPicture.setPictureId(1);
        existingPicture.setPath("A");
        existingPicture.setProduct(product);
        existingPicture.getProduct().setProductId(1);

        PictureDto pictureDto = mock(PictureDto.class);
        when(pictureDto.getPath()).thenReturn("A");
        when(pictureDto.getPictureDtoId()).thenReturn(1);

        when(pictureDto.getPath()).thenReturn("A");
        when(pictureDto.getPictureDtoId()).thenReturn(1);
        when(pictureDao.findById(1)).thenReturn(Optional.of(existingPicture));
        when(serviceMediator.findProductById(anyInt())).thenReturn(Optional.of(product));

        pictureService.update(pictureDto);

        verify(imageStorage, never()).removeFile(anyString());
    }

    @Test
    void testThrowExceptionWhenPictureNotFound() throws CoreException {
        Product product = new Product();
        product.setProductId(1);

        Picture existingPicture = new Picture();
        existingPicture.setPictureId(1);
        existingPicture.setPath("A");
        existingPicture.setProduct(new Product());
        existingPicture.getProduct().setProductId(1);

        PictureDto pictureDto = mock(PictureDto.class);
        when(pictureDto.getPictureDtoId()).thenReturn(1);

        when(pictureDao.findById(1)).thenReturn(Optional.empty());

        assertThrows(CoreException.class, () -> pictureService.update(pictureDto));
    }

    @Test
    void remove() {
        when(pictureDao.findById(1)).thenReturn(Optional.of(picture));
        when(pictureDao.remove(someId)).thenReturn(true);
        when(imageStorage.removeFile(picture.getPath())).thenReturn(true);

        boolean condition = pictureService.remove(someId);

        assertTrue(condition);
        verify(pictureDao, times(1)).remove(someId);
    }

    @Test
    void findPicturesByProductId() {
        when(pictureDao.getPicturesByProductId(someId)).thenReturn(maxPictureList);
        given(dtoResolver.resolvePictureDto(any(Picture.class)))
                    .willReturn(pictureDto);
        List<PictureDto> list = pictureService.findPicturesByProductId(someId);

        assertNotNull(list);
        verify(pictureDao, times(1)).getPicturesByProductId(someId);
    }

    @Test
    void findPicturesById() {
        when(pictureDao.findById(someId)).thenReturn(Optional.of(picture));
        given(dtoResolver.resolvePictureDto(any(Picture.class)))
                .willReturn(pictureDto);
        Optional<PictureDto> pictureDtoOptional = pictureService.findPictureById(someId);

        assertTrue(pictureDtoOptional.isPresent());
        verify(pictureDao, times(1)).findById(someId);
    }

    @Test
    void findAll() {
        when(pictureDao.getPictures(limit, offset)).thenReturn(maxPictureList);
        given(dtoResolver.resolvePictureDto(any(Picture.class)))
                .willReturn(pictureDto);
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