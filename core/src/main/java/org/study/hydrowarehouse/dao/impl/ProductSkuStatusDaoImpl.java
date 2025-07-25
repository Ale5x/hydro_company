package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.ProductSkuStatusDao;
import org.study.hydrowarehouse.entity.ProductSkuStatus;
import org.study.hydrowarehouse.exception.CoreException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link ProductSkuStatusDao} interface providing
 * data access operations for {@link ProductSkuStatus} entities.
 * <p>
 * This class leverages Hibernate Criteria API (via {@link CriteriaQueryHelper})
 * to build and execute type-safe queries.
 * All methods execute within a transactional context, rolling back
 * the transaction for any {@link Exception}.
 * </p>
 *
 * <p><b>Annotations:</b></p>
 * <ul>
 *   <li>{@link org.springframework.stereotype.Repository} — marks this class as a Spring Data repository.</li>
 *   <li>{@link org.springframework.transaction.annotation.Transactional} — defines transaction boundaries with rollback on all Exceptions.</li>
 * </ul>
 *
 * @author Aliaksandr Pishchala
 * @see org.study.hydrowarehouse.dao.ProductSkuStatusDao
 *
 */
@Repository
@Transactional (rollbackFor = Exception.class)
public class ProductSkuStatusDaoImpl extends CriteriaQueryHelper<ProductSkuStatus> implements ProductSkuStatusDao {

    private static final String PRODUCT_SKU_STATUS_ID = "id";
    private static final String PRODUCT_SKU_STATUS_STATUS = "status";
    private static final String DELETE_PRODUCT_SKU_STATUS_QUERY =
            "DELETE FROM ProductSkuStatus pss WHERE pss.productSkuStatusId = :id";
    private static final String FIND_PRODUCT_SKU_STATUS_BY_STATUS_QUERY =
            "FROM ProductSkuStatus pss WHERE pss.status = :status";

    @Override
    public int save(ProductSkuStatus skuStatus) throws CoreException {
        Session session = getCurrentSession();
        session.persist(skuStatus);
        session.flush();
        return skuStatus.getProductSkuStatusId();
    }

    @Override
    public boolean update(ProductSkuStatus skuStatus) throws CoreException {
        Session session = getCurrentSession();
        session.update(skuStatus);
        return true;
    }

    @Override
    public boolean remove(Integer id) throws CoreException {
        Session session = getCurrentSession();

        return session.createQuery(DELETE_PRODUCT_SKU_STATUS_QUERY)
                        .setParameter(PRODUCT_SKU_STATUS_ID, id)
                        .executeUpdate() > 0;
    }

    @Override
    public List<ProductSkuStatus> findAll() {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductSkuStatus> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductSkuStatus.class);
        Root<ProductSkuStatus> skuStatusRoot = getRoot(criteriaQuery, ProductSkuStatus.class);

        criteriaQuery.select(skuStatusRoot);
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<ProductSkuStatus> findByStatus(String status) {
        Session session = getCurrentSession();

        return session.createQuery(FIND_PRODUCT_SKU_STATUS_BY_STATUS_QUERY, ProductSkuStatus.class)
                .setParameter(PRODUCT_SKU_STATUS_STATUS, status).uniqueResultOptional();
    }

    @Override
    public Optional<ProductSkuStatus> findById(Integer statusId) {
        Session session = getCurrentSession();
        ProductSkuStatus result = session.get(ProductSkuStatus.class, statusId);
        return Optional.ofNullable(result);
    }
}
