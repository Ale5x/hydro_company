package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CountryDao;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.entity.Country;
import org.study.hydrowarehouse.exception.CoreException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Implementation of {@link CountryDao} that provides database access operations for {@link Country} entities.
 * <p>
 * This class extends {@link CriteriaQueryHelper} to leverage reusable JPA Criteria API functionality.
 * It is annotated with {@link org.springframework.stereotype.Repository} to indicate a persistence component
 * and {@link org.springframework.transaction.annotation.Transactional} to ensure transaction management.
 * </p>
 *
 * <p>All operations are transactional with rollback for any {@link Exception}.</p>
 *
 * @author Aliaksandr Pishchala
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class CountryDaoImpl extends CriteriaQueryHelper<Country> implements CountryDao {

    private final static String COUNTRY_NAME = "name";
    private final static String COUNTRY_ID = "countryId";
    private final static String PRODUCT_ID = "productId";

    private final static String COUNTRIES_BY_PRODUCT_ID_QUERY = "SELECT DISTINCT c FROM ProductSku ps " +
            "JOIN ps.country c " +
            "WHERE ps.product.productId = :productId";

    @Override
    public Set<Country> countries() {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<Country> criteriaQuery = getCriteriaQuery(criteriaBuilder, Country.class);
        Root<Country> countryRoot = getRoot(criteriaQuery, Country.class);

        criteriaQuery.select(countryRoot);

        return new HashSet<>(session.createQuery(criteriaQuery).getResultList());
    }

    @Override
    public Optional countryById(Integer id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery criteriaQuery = getCriteriaQuery(criteriaBuilder, Country.class);
        Root<Country> countryRoot = getRoot(criteriaQuery, Country.class);

        criteriaQuery.select(countryRoot)
                .where(criteriaBuilder.equal(countryRoot.get(COUNTRY_ID), id));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public Set<Country> countriesByName(String name) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<Country> criteriaQuery = getCriteriaQuery(criteriaBuilder, Country.class);
        Root<Country> countryRoot = getRoot(criteriaQuery, Country.class);

        Predicate[] predicates = createPredicates(List.of(COUNTRY_NAME), criteriaBuilder, countryRoot, name);
        criteriaQuery.select(countryRoot)
                .where(criteriaBuilder.or(predicates));
        return new HashSet<>(session.createQuery(criteriaQuery).getResultList());
    }

    @Override
    public Set<Country> countriesByProduct(Integer productId){
        Session session = getCurrentSession();
        return new HashSet<>(session.createQuery(COUNTRIES_BY_PRODUCT_ID_QUERY, Country.class)
                .setParameter(PRODUCT_ID, productId)
                .getResultList());
    }
}
