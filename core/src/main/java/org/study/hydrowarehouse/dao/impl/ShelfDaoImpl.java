package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.ShelfDao;
import org.study.hydrowarehouse.entity.Shelf;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ShelfDao} that provides database access operations
 * for {@link Shelf} entities.
 * <p>
 * This class extends {@link CriteriaQueryHelper} to reuse common JPA Criteria API functionality.
 * It is annotated with {@link org.springframework.stereotype.Repository} to mark it as a persistence component,
 * and with {@link org.springframework.transaction.annotation.Transactional} to enable transaction management.
 * </p>
 *
 * <p>All operations are transactional with rollback for any {@link Exception}.</p>
 *
 * @author Aliaksandr Pishchala
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class ShelfDaoImpl extends CriteriaQueryHelper<Shelf> implements ShelfDao {

    private final static String SHELF_ID = "shelfId";
    private final static String SHELF_NAME= "name";

    private final static String DELETE_SHELF_QUERY = String.format("DELETE Shelf WHERE id =: %s", SHELF_ID);


    @Override
    public int create(Shelf shelf) {
        Session session = getCurrentSession();
        session.save(shelf);
        session.flush();
        return shelf.getShelfId();
    }

    @Override
    public boolean update(Shelf shelf) {
        Session session = getCurrentSession();
        session.update(shelf);
        return true;
    }

    @Override
    public boolean remove(Integer id) {
        Session session = getCurrentSession();
        int rows = session.createQuery(DELETE_SHELF_QUERY)
                .setParameter(SHELF_ID, id)
                .executeUpdate();
        return rows > 0;
    }

    @Override
    public Optional<Shelf> findById(Integer id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<Shelf> criteriaQuery = getCriteriaQuery(criteriaBuilder, Shelf.class);
        Root<Shelf> shelfRoot = getRoot(criteriaQuery, Shelf.class);

        criteriaQuery.select(shelfRoot).where(criteriaBuilder.equal(shelfRoot.get(SHELF_ID), id));

        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public Optional<Shelf> findByName(String name) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<Shelf> criteriaQuery = getCriteriaQuery(criteriaBuilder, Shelf.class);
        Root<Shelf> shelfRoot = getRoot(criteriaQuery, Shelf.class);

        criteriaQuery.select(shelfRoot).where(criteriaBuilder.equal(shelfRoot.get(SHELF_NAME), name));

        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public List<Shelf> getAllShelf() {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<Shelf> criteriaQuery = getCriteriaQuery(criteriaBuilder, Shelf.class);
        Root<Shelf> shelfRoot = getRoot(criteriaQuery, Shelf.class);
        criteriaQuery.select(shelfRoot);

        return session.createQuery(criteriaQuery).getResultList();
    }
}