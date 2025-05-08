package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CountryDao;
import org.study.hydro.dao.CriteriaQueryHelper;
import org.study.hydro.entity.Country;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
@Transactional(rollbackFor = Exception.class)
public class CountryDaoImpl extends CriteriaQueryHelper<Country> implements CountryDao {

    private final static String COUNTRY_NAME = "name";
    private final static String COUNTRY_ID = "countryId";

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
    public Optional countryById(int id) {
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
}
