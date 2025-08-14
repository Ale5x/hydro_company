package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.ProductDao;
import org.study.hydrowarehouse.entity.*;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ProductDao} that provides database access operations
 * for {@link Product} entities.
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
public class ProductDaoImpl extends CriteriaQueryHelper<Product> implements ProductDao {

    private final static String PRODUCT_SKUS = "productSkus";

    private final static String STATUS = "status";

    private final static String PRODUCT_ID = "productId";

    private final static String PRODUCT_TYPE_ID = "productTypeId";

    private final static String COMPANY_ID = "productCompanyId";

    private final static String PRODUCT_PRESSURE = "pressure";

    private final static String PRODUCT_FLOW_RATE = "flowRate";

    private final static String STORAGE_RACK_NAME = "storageRackName";

    private final static String DELETE_PRODUCT_QUERY = String.format("DELETE Product WHERE id =: %s", PRODUCT_ID);

    private final static String GET_PRODUCTS_BY_TYPE_ID_QUERY = "SELECT DISTINCT p FROM Product p " +
                    "JOIN p.productSkus ps JOIN ps.status s WHERE p.productType.productTypeId = :productTypeId " +
                    "AND s.status = :status ORDER BY p.productId";

    private final static String GET_PRODUCTS_BY_PRESSURE_QUERY = "SELECT DISTINCT p FROM Product p JOIN p.productSkus ps " +
            "JOIN ps.status s WHERE p.pressure <= :pressure AND s.status = :status ORDER BY p.productId";

    private final static String GET_PRODUCTS_BY_FLOW_RATE_QUERY = "SELECT DISTINCT p FROM Product p JOIN p.productSkus ps " +
            "JOIN ps.status s WHERE p.flowRate <= :flowRate AND s.status = :status ORDER BY p.productId";

    private final static String GET_PRODUCTS_BY_COMPANY_ID_QUERY = "SELECT DISTINCT p FROM Product p " +
            "JOIN p.productSkus ps JOIN ps.status s WHERE p.productCompany.productCompanyId = :productCompanyId " +
            "AND s.status = :status ORDER BY p.productId";

    private final static String GET_PRODUCTS_BY_STORAGE_RACK_NAME_QUERY = "SELECT DISTINCT p FROM Product p " +
                    "JOIN p.productSkus ps JOIN ps.status s JOIN ps.shelf sh JOIN sh.storageRack sr " +
                    "WHERE sr.name = :storageRackName AND s.status = :status ORDER BY p.productId";

    @Override
    public int create(Product product) {
        Session session = getCurrentSession();
        session.save(product);
        session.flush();
        return product.getProductId();
    }

    @Override
    public boolean update(Product product) {
        Session session = getCurrentSession();
        session.saveOrUpdate(product);
        return true;
    }

    @Override
    public boolean remove(Integer id) {
        Session session = getCurrentSession();
        return session.createQuery(DELETE_PRODUCT_QUERY)
                .setParameter(PRODUCT_ID, id)
                .executeUpdate() > 0;
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<Product> criteriaQuery = getCriteriaQuery(criteriaBuilder, Product.class);
        Root<Product> productRoot = getRoot(criteriaQuery, Product.class);

        criteriaQuery.select(productRoot)
                .where(criteriaBuilder.equal(productRoot.get(PRODUCT_ID), id));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public List<Product> getProductsList(int limit, int offset, String status) {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<Product> criteriaQuery = getCriteriaQuery(criteriaBuilder, Product.class);
        Root<Product> productRoot = getRoot(criteriaQuery, Product.class);

        Join<Product, ProductSku> skuJoin = productRoot.join(PRODUCT_SKUS);

        Join<ProductSku, ProductSkuStatus> statusJoin = skuJoin.join(STATUS);

        Predicate statusPredicate = criteriaBuilder.equal(statusJoin.get(STATUS), status);
        criteriaQuery.select(productRoot)
                .distinct(true)
                .where(statusPredicate)
                .orderBy(criteriaBuilder.asc(productRoot.get(PRODUCT_ID)));

        return session.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByFlowRate(int limit, int offset, Integer flowRate, String status) {
        Session session = getCurrentSession();
        return session.createQuery(GET_PRODUCTS_BY_FLOW_RATE_QUERY)
                .setParameter(PRODUCT_FLOW_RATE, flowRate)
                .setParameter(STATUS, status)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByPressure(int limit, int offset, Integer pressure, String status) {
        Session session = getCurrentSession();

        return session.createQuery(GET_PRODUCTS_BY_PRESSURE_QUERY)
                .setParameter(PRODUCT_PRESSURE, pressure)
                .setParameter(STATUS, status)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByTypeId(int limit, int offset, Integer productTypeId, String status) {
        Session session = getCurrentSession();

        return session.createQuery(GET_PRODUCTS_BY_TYPE_ID_QUERY)
                .setParameter(PRODUCT_TYPE_ID, productTypeId)
                .setParameter(STATUS, status)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByCompanyId(int limit, int offset, Integer productCompanyId, String status) {
        Session session = getCurrentSession();
        return session.createQuery(GET_PRODUCTS_BY_COMPANY_ID_QUERY)
                .setParameter(COMPANY_ID, productCompanyId)
                .setParameter(STATUS, status)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByStorageRackName(int limit, int offset, String storageRackName, String status){
        Session session = getCurrentSession();

        return session.createQuery(GET_PRODUCTS_BY_STORAGE_RACK_NAME_QUERY)
                .setParameter(STORAGE_RACK_NAME, storageRackName)
                .setParameter(STATUS, status)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
