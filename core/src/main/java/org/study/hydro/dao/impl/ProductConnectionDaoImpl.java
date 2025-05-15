package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CriteriaQueryHelper;
import org.study.hydro.dao.ProductConnectionDao;
import org.study.hydro.entity.ProductConnection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(rollbackFor = Exception.class)
public class ProductConnectionDaoImpl extends CriteriaQueryHelper<ProductConnection> implements ProductConnectionDao {

    private final static String PRODUCT_CONNECTION_ID = "productConnectionId";

    @Override
    public int save(ProductConnection productConnection) {
        Session session = getCurrentSession();

        session.save(productConnection);
        session.flush();
        return productConnection.getProductConnectionId();
    }

    @Override
    public boolean updateProductConnection(ProductConnection productConnection) {
        Session session = getCurrentSession();

        session.update(productConnection);

        return true;
    }

    @Override
    public Optional<ProductConnection> getProductConnectionById(Integer id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductConnection> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductConnection.class);
        Root<ProductConnection> productConnectionRoot = getRoot(criteriaQuery, ProductConnection.class);

        criteriaQuery.select(productConnectionRoot)
                .where(criteriaBuilder.equal(productConnectionRoot.get(PRODUCT_CONNECTION_ID), id));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public List<ProductConnection> getListProductsConnection() {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductConnection> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductConnection.class);
        Root<ProductConnection> criteriaQueryRoot = getRoot(criteriaQuery, ProductConnection.class);

        criteriaQuery.select(criteriaQueryRoot);

        return session.createQuery(criteriaQuery).getResultList();
    }
}
