package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.configuration.DevelopmentConfig;
import org.study.hydrowarehouse.dao.ShelfDao;
import org.study.hydrowarehouse.entity.Shelf;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("development")
@ContextConfiguration(classes = DevelopmentConfig.class)
class ShelfDaoImplTest {

    @Autowired
    private ShelfDao shelfDao;

    @Autowired
    private SessionFactory sessionFactory;

    private Shelf newShelf = new Shelf("New shelf");

    @Test
    void create() {
        List<Shelf> shelfListBefore = shelfDao.getAllShelf();
        assertTrue(shelfListBefore.size() > 0);
        assertFalse(shelfListBefore.isEmpty());

        int newShelfId = shelfDao.create(newShelf);

        assertTrue(newShelfId > 0);

        List<Shelf> shelfListAfter = shelfDao.getAllShelf();
        assertTrue(shelfListAfter.size() > 0);

        assertTrue(shelfListAfter.size() > shelfListBefore.size());
    }

    @Test
    @Transactional
    void update() {
        Shelf shelf = new Shelf();
        shelf.setName("Original Name");

        Session session = sessionFactory.getCurrentSession();
        session.save(shelf);
        session.flush();
        Integer id = shelf.getShelfId();

        shelf.setName("Updated Name");
        boolean result = shelfDao.update(shelf);

        assertTrue(result);

        Shelf updatedShelf = session.get(Shelf.class, id);
        assertEquals("Updated Name", updatedShelf.getName());
    }


    @Test
    void findById() {
        int shelfId = 1;
        Optional<Shelf> shelf = shelfDao.findById(shelfId);
        assertTrue(shelf.isPresent());
    }

    @Test
    void findAll() {
        List<Shelf> shelfList = shelfDao.getAllShelf();
        assertTrue(shelfList.size() > 0);
        assertFalse(shelfList.isEmpty());
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