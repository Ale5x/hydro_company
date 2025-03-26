package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CriteriaQueryHelper;
import org.study.hydro.dao.ShelfDao;
import org.study.hydro.entity.Shelf;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(rollbackFor = Exception.class)
public class ShelfDaoImpl extends CriteriaQueryHelper<Shelf> implements ShelfDao {

    private final static String SHELF_ID = "shelfId";
    private final static String SHELF_NAME= "name";

    private final static String DELETE_SHELF_QUERY = String.format("DELETE Shelf WHERE id =: %s", SHELF_ID);


    @Override
    public Optional<Shelf> findById(int id) {
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
    public boolean update(Shelf shelf) {
        Session session = getCurrentSession();
        session.update(shelf);
        return true;
    }

    @Override
    public boolean remove(int id) {
        Session session = getCurrentSession();
        int rows = session.createQuery(DELETE_SHELF_QUERY)
                .setParameter(SHELF_ID, id)
                .executeUpdate();
        return rows > 0;
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