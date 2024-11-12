package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CriteriaQueryHelper;
import org.study.hydro.dao.ProductTypeDao;
import org.study.hydro.entity.ProductType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(rollbackFor = Exception.class)
public class ProductTypeDaoImpl extends CriteriaQueryHelper<ProductType> implements ProductTypeDao {

    private final static String PRODUCT_TYPE_ID = "productTypeId";
    private final static String PRODUCT_TYPE_NAME = "name";

    private final static String DELETE_PRODUCT_TYPE_QUERY = String.format("DELETE ProductType WHERE id =: %s",
            PRODUCT_TYPE_ID);

    @Override
    public boolean create(ProductType productType) {
        Session session = getCurrentSession();
        session.save(productType);
        session.flush();
        return productType.getProductTypeId() > 0;
    }

    @Override
    public List<ProductType> getProductTypes() {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductType> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductType.class);
        Root<ProductType> productTypeRoot = getRoot(criteriaQuery, ProductType.class);

        criteriaQuery.select(productTypeRoot);

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<ProductType> getProductTypeById(int id) {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductType> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductType.class);
        Root<ProductType> productTypeRoot = getRoot(criteriaQuery, ProductType.class);

        criteriaQuery.select(productTypeRoot).where(criteriaBuilder.equal(productTypeRoot.get(PRODUCT_TYPE_ID), id));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public Optional<ProductType> getProductTypeByName(String name) {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductType> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductType.class);
        Root<ProductType> productTypeRoot = getRoot(criteriaQuery, ProductType.class);

        criteriaQuery.select(productTypeRoot).where(criteriaBuilder.equal(productTypeRoot.get(PRODUCT_TYPE_NAME), name));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public boolean update(ProductType productType) {
        Session session = getCurrentSession();
        session.saveOrUpdate(productType);
        return true;
    }

    @Override
    public boolean remove(int id) {
        Session session = getCurrentSession();
        return session.createQuery(DELETE_PRODUCT_TYPE_QUERY)
                .setParameter(PRODUCT_TYPE_ID, id)
                .executeUpdate() > 0;
    }
}