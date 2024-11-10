package org.study.hydro.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.StorageRackDao;
import org.study.hydro.entity.Shelf;
import org.study.hydro.entity.StorageRack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class StorageRackDaoImplTest {

    @Autowired
    private StorageRackDao storageRackDao;

    private int limit = 100;
    private int maxLimit = 100;
    private int offset = 1;
    private int storageRackId = 1;
    private String newStorageRackName = "new StorageRack";

    private Shelf shelf = new Shelf("name");

    private StorageRack storageRack = new StorageRack(1, newStorageRackName, shelf);

    @BeforeEach
    void setUp() {
    }

    @Test
    void create() {
        List<StorageRack> storageRackListBefore = storageRackDao.getStorageRacksList(maxLimit, offset);
        assertFalse(storageRackListBefore.isEmpty());

        boolean condition = storageRackDao.create(storageRack);

        assertTrue(condition);
        List<StorageRack> storageRackListAfter = storageRackDao.getStorageRacksList(maxLimit, offset);
        assertFalse(storageRackListAfter.isEmpty());
        assertTrue(storageRackListAfter.size() > storageRackListBefore.size());

    }

    @Test
    void remove() {
        List<StorageRack> storageRackListBefore = storageRackDao.getStorageRacksList(maxLimit, offset);
        assertFalse(storageRackListBefore.isEmpty());

        boolean condition = storageRackDao.remove(storageRackId);

        assertTrue(condition);
        List<StorageRack> storageRackListAfter = storageRackDao.getStorageRacksList(maxLimit, offset);
        assertFalse(storageRackListAfter.isEmpty());
        assertTrue(storageRackListBefore.size() > storageRackListAfter.size());
    }

    @Test
    void update() {
        Optional<StorageRack> storageRackBefore = storageRackDao.getStorageRackById(storageRackId);
        assertTrue(storageRackBefore.isPresent());

        String oldName = storageRackBefore.get().getName();
        StorageRack newStorageRack = storageRackBefore.get();
        newStorageRack.setName(newStorageRackName);
        storageRackDao.update(newStorageRack);

        Optional<StorageRack> storageRackAfter = storageRackDao.getStorageRackById(storageRackId);
        assertTrue(storageRackAfter.isPresent());

        assertEquals(storageRackAfter.get().getName(), newStorageRackName);
        assertNotEquals(storageRackAfter.get().getName(), oldName);

    }

    @Test
    void getStorageRackById() {
        Optional<StorageRack> storageRackOptional = storageRackDao.getStorageRackById(storageRackId);
        assertTrue(storageRackOptional.isPresent());
    }

    @Test
    void getStorageRacksList() {
        List<StorageRack> storageRackList = storageRackDao.getStorageRacksList(limit, offset);

        assertFalse(storageRackList.isEmpty());
    }
}