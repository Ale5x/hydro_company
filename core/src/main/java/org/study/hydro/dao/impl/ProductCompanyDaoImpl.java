package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CriteriaQueryHelper;
import org.study.hydro.dao.ProductCompanyDao;
import org.study.hydro.entity.ProductCompany;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(rollbackFor = Exception.class)
public class ProductCompanyDaoImpl extends CriteriaQueryHelper<ProductCompany> implements ProductCompanyDao {

    private final static String PRODUCT_COMPANY_ID = "productCompanyId";
    private final static String PRODUCT_COMPANY_NAME = "name";

    private final static String DELETE_PRODUCT_COMPANY_QUERY = String.format("DELETE ProductCompany WHERE id =: %s",
            PRODUCT_COMPANY_ID);

    @Override
    public boolean create(ProductCompany productCompany) {
        Session session = getCurrentSession();
        session.save(productCompany);
        session.flush();
        return productCompany.getProductCompanyId() > 0;
    }

    @Override
    public Optional<ProductCompany> getById(Integer id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductCompany> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductCompany.class);
        Root<ProductCompany> productCompanyRoot = getRoot(criteriaQuery, ProductCompany.class);

        criteriaQuery.select(productCompanyRoot)
                .where(criteriaBuilder.equal(productCompanyRoot.get(PRODUCT_COMPANY_ID), id));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public List<ProductCompany> getProductCompanies(int offset, int limit) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductCompany> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductCompany.class);
        Root<ProductCompany> productCompanyRoot = getRoot(criteriaQuery, ProductCompany.class);

        criteriaQuery.select(productCompanyRoot);
        return session.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public boolean update(ProductCompany productCompany) {
        Session session = getCurrentSession();

        session.saveOrUpdate(productCompany);
        return true;
    }

    @Override
    public List<ProductCompany> getProductCompaniesByName(String name) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<ProductCompany> criteriaQuery = getCriteriaQuery(criteriaBuilder, ProductCompany.class);
        Root<ProductCompany> productCompanyRoot = getRoot(criteriaQuery, ProductCompany.class);

        Predicate [] predicates = createPredicates(List.of(PRODUCT_COMPANY_NAME), criteriaBuilder, productCompanyRoot, name);

        criteriaQuery.select(productCompanyRoot)
                .where(criteriaBuilder.or(predicates))
                .orderBy(criteriaBuilder.desc(productCompanyRoot.get(PRODUCT_COMPANY_NAME)));

        return session.createQuery(criteriaQuery).getResultList();
    }
}
