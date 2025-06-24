package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.ProductDao;
import org.study.hydrowarehouse.entity.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(rollbackFor = Exception.class)
public class ProductDaoImpl extends CriteriaQueryHelper<Product> implements ProductDao {

    private final static String PRODUCT_ID = "productId";
    private final static String PRODUCT_TYPE_ID = "id_products_type";
    private final static String COMPANY_ID = "productCompanyId";
    private final static String PRODUCT_PRESSURE = "pressure";
    private final static String PRODUCT_FLOW_RATE = "flowRate";
    private final static String STORAGE_RACK_NAME = "name";
    private final static String DELETE_PRODUCT_QUERY = String.format("DELETE Product WHERE id =: %s", PRODUCT_ID);
    private final static String GET_PRODUCTS_BY_TYPE_ID_QUERY = "SELECT p FROM Product p WHERE p.productType.productTypeId = " +
            ":productTypeId GROUP BY p.productId";
    private final static String GET_PRODUCTS_BY_PRESSURE_QUERY = "SELECT p FROM Product p WHERE p.pressure <= :" +
            "pressure GROUP BY p.productId";
    private final static String GET_PRODUCTS_BY_FLOW_RATE_QUERY = "SELECT p FROM Product p WHERE " +
            "p.flowRate <= :flowRate GROUP BY p.productId";
    private final static String GET_PRODUCTS_BY_COMPANY_ID_QUERY = "SELECT p FROM Product p WHERE " +
            "p.productCompany.productCompanyId = :companyId GROUP BY p.productId";
//    private final static String GET_PRODUCTS_BY_STORAGE_RACK_QUERY = "SELECT p FROM Product p LEFT JOIN p.productType pt" +
//            " LEFT JOIN p.productCompany pc LEFT JOIN p.productConnection pCon LEFT JOIN p.countryProduct c" +
//            " LEFT JOIN p.picturePath pic LEFT JOIN p.storageRackList sr" +
//            " WHERE sr.name =: " + STORAGE_RACK_NAME + " order by p.productId";

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
    public List<Product> getProductsList(int limit, int offset) {
        Session session = getCurrentSession();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<Product> criteriaQuery = getCriteriaQuery(criteriaBuilder, Product.class);
        Root<Product> productRoot = getRoot(criteriaQuery, Product.class);

        criteriaQuery.select(productRoot).orderBy(criteriaBuilder.asc(productRoot.get(PRODUCT_ID)));
        return session.createQuery(criteriaQuery)
                      .setMaxResults(limit)
                      .setFirstResult(offset)
                      .getResultList();
    }

    @Override
    public List<Product> getProductsByFlowRate(int limit, int offset, Integer flowRate) {
        Session session = getCurrentSession();
        return session.createQuery(GET_PRODUCTS_BY_FLOW_RATE_QUERY)
                .setParameter(PRODUCT_FLOW_RATE, flowRate)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByPressure(int limit, int offset, Integer pressure) {
        Session session = getCurrentSession();

        return session.createQuery(GET_PRODUCTS_BY_PRESSURE_QUERY)
                .setParameter(PRODUCT_PRESSURE, pressure)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByTypeId(int limit, int offset, Integer productTypeId) {
        Session session = getCurrentSession();

        return session.createQuery(GET_PRODUCTS_BY_TYPE_ID_QUERY)
                .setParameter(PRODUCT_TYPE_ID, productTypeId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByCompanyId(int limit, int offset, Integer productCompanyId) {
        Session session = getCurrentSession();
        return session.createQuery(GET_PRODUCTS_BY_COMPANY_ID_QUERY)
                .setParameter(COMPANY_ID, productCompanyId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Product> getProductsByStorageRackName(int limit, int offset, String storageRackName){
        Session session = getCurrentSession();

        return session.createQuery("GET_PRODUCTS_BY_STORAGE_RACK_QUERY")
                .setParameter(STORAGE_RACK_NAME, storageRackName)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
