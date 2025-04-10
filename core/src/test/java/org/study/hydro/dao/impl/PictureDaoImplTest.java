package org.study.hydro.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.PictureDao;
import org.study.hydro.entity.Picture;
import org.study.hydro.entity.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class PictureDaoImplTest {

    @Autowired
    private PictureDao pictureDao;

    private Product product = new Product(1);
    private Picture picture = new Picture("new path", product);
    private int pictureId = 1;
    private int productId = 1;
    private int maxLimit = 100;
    private int offset = 0;

    List<Picture> pictureList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        // Создаём несколько объектов Picture для теста
        pictureList = Arrays.asList(
                new Picture("path/to/picture1", new Product(1)),
                new Picture("path/to/picture2", new Product(1)),
                new Picture("path/to/picture3", new Product(2))
        );
    }


    @Test
    void create() {
        List<Picture> pictureListBefore = pictureDao.getPictures(maxLimit, offset);
        assertTrue(pictureListBefore.size() > 0);
        boolean condition = pictureDao.create(picture);
        assertTrue(condition);

        List<Picture> pictureListAfter = pictureDao.getPictures(maxLimit, offset);

        assertTrue(pictureListAfter.size() > 0);
        assertTrue(pictureListAfter.size() > pictureListBefore.size());
    }
    @Value("${file.multipart.max-size-file}")
    private String maxSizeFile1;
    @Test
    void createList() {
        System.out.println("maxSizeFile1 " + maxSizeFile1);
        List<Picture> pictureListBefore = pictureDao.getPictures(maxLimit, offset);
        assertTrue(pictureListBefore.size() > 0);
        boolean condition = pictureDao.createList(pictureList);
//        assertTrue(condition);

        List<Picture> pictureListAfter = pictureDao.getPictures(maxLimit, offset);

        assertTrue(pictureListAfter.size() > 0);
        assertTrue(pictureListAfter.size() > pictureListBefore.size());
    }

    @Test
    void remove() {
        List<Picture> pictureListBefore = pictureDao.getPictures(maxLimit, offset);
        assertTrue(pictureListBefore.size() > 0);
        boolean condition = pictureDao.remove(pictureId);
        assertTrue(condition);

        List<Picture> pictureListAfter = pictureDao.getPictures(maxLimit, offset);

        assertTrue(pictureListAfter.size() > 0);
        assertTrue(pictureListAfter.size() < pictureListBefore.size());
    }

    @Test
    void getPicturesByProductId() {
        List<Picture> pictures = pictureDao.getPicturesByProductId(pictureId);

        assertFalse(pictures.isEmpty());
        assertEquals(productId, pictures.get(0).getProduct().getProductId());
    }
}