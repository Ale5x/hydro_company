package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.ProductSkuDao;
import org.study.hydrowarehouse.entity.Product;
import org.study.hydrowarehouse.entity.ProductSku;
import org.study.hydrowarehouse.entity.ProductSkuStatus;
import org.study.hydrowarehouse.exception.CoreException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * DAO implementation for {@link ProductSku} entity.
 * <p>
 * Provides methods for retrieving product SKUs by ID, code, status, and related product.
 * Uses {@link CriteriaQueryHelper} for query operations.
 * </p>
 *
 * <p><strong>Thread Safety:</strong> This class is thread-safe when used within the transactional context of Spring.</p>
 *
 * @author Aliaksandr Pishchala
 * @see ProductSkuDao
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class ProductSkuDaoImpl extends CriteriaQueryHelper<ProductSku> implements ProductSkuDao {

    private static final String PRODUCT_SKU_ID = "productSkuId";
    private static final String PRODUCT_ID = "product";
    private static final String PRODUCT_SKU_CODE = "code";
    private static final String PRODUCT_SKU_STATUS = "status";
    private final static String DELETE_PRODUCT_SKU_QUERY = String.format("DELETE Product WHERE id =: %s", PRODUCT_SKU_ID);


    @Override
    public int save(ProductSku productSku) throws CoreException {
        Session session = getCurrentSession();

        session.persist(productSku);
        session.flush();
        return productSku.getProductSkuId();
    }

    @Override
    public boolean update(ProductSku productSku) throws CoreException {
        Session session = getCurrentSession();
        session.update(productSku);
        return true;
    }

    @Override
    public boolean remove(Integer id) throws CoreException {
        Session session = getCurrentSession();
        return session.createQuery(DELETE_PRODUCT_SKU_QUERY)
                .setParameter(PRODUCT_SKU_ID, id)
                .executeUpdate() > 0;
    }

    @Override
    public Optional<ProductSku> findById(Integer id) throws CoreException {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductSku> criteriaQuery = getCriteriaQuery(criteriaBuilder,ProductSku.class);
        Root<ProductSku> productSkuRoot = getRoot(criteriaQuery, ProductSku.class);
        criteriaQuery.select(productSkuRoot).where(criteriaBuilder.equal(productSkuRoot.get(PRODUCT_SKU_ID), id));

        return Optional.ofNullable(session.createQuery(criteriaQuery)
                .setMaxResults(1)
                .uniqueResult());
    }

    @Override
    public List<ProductSku> findByCode(String code) throws CoreException {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductSku> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductSku.class);
        Root<ProductSku> countryRoot = getRoot(criteriaQuery, ProductSku.class);

        Predicate[] predicates = createPredicates(List.of(PRODUCT_SKU_CODE), criteriaBuilder, countryRoot, code);
        criteriaQuery.select(countryRoot)
                .where(criteriaBuilder.or(predicates));
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<ProductSku> findAllByStatus(int limit, int offset, ProductSkuStatus status) throws CoreException {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductSku> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductSku.class);
        Root<ProductSku> skuRoot = getRoot(criteriaQuery, ProductSku.class);

        Predicate statusPredicate = criteriaBuilder.equal(skuRoot.get(PRODUCT_SKU_STATUS), status);

        criteriaQuery.select(skuRoot)
                .where(statusPredicate);

        return session.createQuery(criteriaQuery)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<ProductSku> findAllByProduct(int limit, int offset, Product product) throws CoreException {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductSku> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductSku.class);
        Root<ProductSku> skuRoot = getRoot(criteriaQuery, ProductSku.class);

        Predicate productPredicate = criteriaBuilder.equal(skuRoot.get(PRODUCT_ID), product.getProductId());

        criteriaQuery.select(skuRoot)
                .where(criteriaBuilder.or(productPredicate));

        return session.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }
}
