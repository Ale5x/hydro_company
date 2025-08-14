package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.StorageRackDao;
import org.study.hydrowarehouse.entity.StorageRack;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link StorageRackDao} that provides database access operations
 * for {@link StorageRack} entities.
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
public class StorageRackDaoImpl extends CriteriaQueryHelper<StorageRack> implements StorageRackDao {

    private final static String STORAGE_RACK_ID = "rackId";
    private final static String STORAGE_RACK_NAME = "name";
    private final static String DELETE_STORAGE_RUCK_BY_ID = String.format("DELETE StorageRack WHERE id =: %s",
            STORAGE_RACK_ID);

    @Override
    public int create(StorageRack storageRack) {
        Session session = getCurrentSession();
        session.save(storageRack);

        session.flush();
        return storageRack.getRackId();
    }

    @Override
    public boolean remove(Integer id) {
        Session session = getCurrentSession();

        return session.createQuery(DELETE_STORAGE_RUCK_BY_ID)
                .setParameter(STORAGE_RACK_ID, id)
                .executeUpdate() > 0;
    }

    @Override
    public boolean update(StorageRack storageRack) {
        Session session = getCurrentSession();
        session.saveOrUpdate(storageRack);
        return true;
    }

    @Override
    public Optional<StorageRack> getStorageRackById(Integer id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<StorageRack> criteriaQuery = getCriteriaQuery(criteriaBuilder, StorageRack.class);
        Root<StorageRack> storageRackRoot = getRoot(criteriaQuery, StorageRack.class);

        criteriaQuery.select(storageRackRoot)
                .where(criteriaBuilder.equal(storageRackRoot.get(STORAGE_RACK_ID), id))
                .orderBy(criteriaBuilder.desc(storageRackRoot.get(STORAGE_RACK_NAME)));


        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public List<StorageRack> getStorageRacksList(int limit, int offset) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<StorageRack> criteriaQuery = getCriteriaQuery(criteriaBuilder, StorageRack.class);
        Root<StorageRack> storageRackRoot = getRoot(criteriaQuery, StorageRack.class);

        criteriaQuery.select(storageRackRoot).orderBy(criteriaBuilder.desc(storageRackRoot.get(STORAGE_RACK_NAME)));
        return session.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
    }
}