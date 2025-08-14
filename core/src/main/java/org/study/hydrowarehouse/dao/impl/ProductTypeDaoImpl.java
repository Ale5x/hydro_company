package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.ProductTypeDao;
import org.study.hydrowarehouse.entity.ProductType;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ProductTypeDao} that provides database access operations
 * for {@link ProductType} entities.
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
public class ProductTypeDaoImpl extends CriteriaQueryHelper<ProductType> implements ProductTypeDao {

    private final static String PRODUCT_TYPE_ID = "productTypeId";
    private final static String PRODUCT_TYPE_NAME = "name";

    private final static String DELETE_PRODUCT_TYPE_QUERY = String.format("DELETE ProductType WHERE id =: %s",
            PRODUCT_TYPE_ID);

    @Override
    public int create(ProductType productType) {
        Session session = getCurrentSession();
        session.save(productType);
        session.flush();
        return productType.getProductTypeId();
    }

    @Override
    public boolean update(ProductType productType) {
        Session session = getCurrentSession();
        session.saveOrUpdate(productType);
        return true;
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
    public Optional<ProductType> getProductTypeById(Integer id) {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductType> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductType.class);
        Root<ProductType> productTypeRoot = getRoot(criteriaQuery, ProductType.class);

        criteriaQuery.select(productTypeRoot).where(criteriaBuilder.equal(productTypeRoot.get(PRODUCT_TYPE_ID), id));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public List<ProductType> getProductTypeByName(String name) {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductType> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductType.class);
        Root<ProductType> productTypeRoot = getRoot(criteriaQuery, ProductType.class);

        criteriaQuery.select(productTypeRoot)
                .where(criteriaBuilder.like(
                        criteriaBuilder.lower(productTypeRoot.get(PRODUCT_TYPE_NAME)),
                        createSearchCriteria(name).toLowerCase()
                ));

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public boolean remove(Integer id) {
        Session session = getCurrentSession();
        return session.createQuery(DELETE_PRODUCT_TYPE_QUERY)
                .setParameter(PRODUCT_TYPE_ID, id)
                .executeUpdate() > 0;
    }
}