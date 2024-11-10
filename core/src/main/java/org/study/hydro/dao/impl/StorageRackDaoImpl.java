package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CriteriaQueryHelper;
import org.study.hydro.dao.StorageRackDao;
import org.study.hydro.entity.StorageRack;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class StorageRackDaoImpl extends CriteriaQueryHelper<StorageRack> implements StorageRackDao {

    private final static String STORAGE_RACK_ID = "storageRackId";
    private final static String STORAGE_RACK_NAME = "name";
    private final static String DELETE_STORAGE_RUCK_BY_ID = String.format("DELETE StorageRack WHERE id =: %s",
            STORAGE_RACK_ID);


    @Override
    public boolean create(StorageRack storageRack) {
        Session session = getCurrentSession();
        session.save(storageRack);

        session.flush();
        return storageRack.getStorageRackId() > 0;
    }



    @Override
    public boolean remove(int id) {
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
    public Optional<StorageRack> getStorageRackById(int id) {
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
