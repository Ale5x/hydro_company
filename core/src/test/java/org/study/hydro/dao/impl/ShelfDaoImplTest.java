package org.study.hydro.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.ShelfDao;
import org.study.hydro.entity.Shelf;
import org.study.hydro.entity.StorageRack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class ShelfDaoImplTest {

    @Autowired
    private ShelfDao shelfDao;

    @Test
    void findById() {
        int shelfId = 1;
        Optional<Shelf> shelf = shelfDao.findById(shelfId);
        assertTrue(shelf.isPresent());
    }

    @Test
    void findByName() {
        int shelfId = 1;
        Optional<Shelf> shelfOptional = shelfDao.findById(shelfId);

        assertTrue(shelfOptional.isPresent());

        Optional<Shelf> shelf = shelfDao.findByName(shelfOptional.get().getName());
        assertTrue(shelf.isPresent());
    }

    @Test
    void update() {
        int shelfId = 1;
        String numberUpdateShelf = "111";
        Optional<Shelf> shelfBeforeOperation = shelfDao.findById(shelfId);
        assertTrue(shelfBeforeOperation.isPresent());

        Shelf shelf = new Shelf(shelfBeforeOperation.get().getShelfId(), shelfBeforeOperation.get().getName());
        shelf.setName(numberUpdateShelf);
        shelfDao.update(shelf);

        Optional<Shelf> shelAfterOperation = shelfDao.findById(shelfId);
        assertTrue(shelAfterOperation.isPresent());

        assertEquals(numberUpdateShelf, shelAfterOperation.get().getName());
    }

    @Test
    void remove() {
        int shelfId = 1;
        Optional<Shelf> shelf = shelfDao.findById(shelfId);
        assertTrue(shelf.isPresent());

        boolean condition = shelfDao.remove(shelfId);
        assertTrue(condition);
        Optional<Shelf> shelAfterOperation = shelfDao.findById(shelfId);
        assertFalse(shelAfterOperation.isPresent());
        assertNotNull(shelAfterOperation);
    }
}