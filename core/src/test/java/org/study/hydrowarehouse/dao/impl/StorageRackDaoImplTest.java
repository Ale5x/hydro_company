package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.configuration.DevelopmentConfig;
import org.study.hydrowarehouse.dao.StorageRackDao;
import org.study.hydrowarehouse.entity.StorageRack;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class StorageRackDaoImplTest {

    @Autowired
    private StorageRackDao storageRackDao;

    @Autowired
    private SessionFactory sessionFactory;

    private int limit = 100;
    private int maxLimit = 100;
    private int offset = 1;
    private int storageRackId = 1;
    private String newStorageRackName = "new StorageRack";

    private StorageRack storageRack = new StorageRack(1, newStorageRackName);

    @BeforeEach
    void setUp() {
    }

    @Test
    void create() {
        List<StorageRack> storageRackListBefore = storageRackDao.getStorageRacksList(maxLimit, offset);
        assertFalse(storageRackListBefore.isEmpty());

        int condition = storageRackDao.create(storageRack);

        assertTrue(condition > 0);
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
    @Transactional
    void update() {
        StorageRack rack = new StorageRack();
        rack.setName("RACK-01");

        Session session = sessionFactory.getCurrentSession();
        session.save(rack);
        session.flush();
        Integer id = rack.getRackId();

        rack.setName("UPDATED-RACK");
        boolean result = storageRackDao.update(rack);

        assertTrue(result);

        StorageRack updated = session.get(StorageRack.class, id);
        assertEquals("UPDATED-RACK", updated.getName());
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