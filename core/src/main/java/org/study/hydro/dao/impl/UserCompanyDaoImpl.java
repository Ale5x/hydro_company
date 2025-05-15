package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CriteriaQueryHelper;
import org.study.hydro.dao.UserCompanyDao;
import org.study.hydro.entity.UserCompany;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * The type Role repository implements methods of the CompanyDao interface.
 * The class is annotated with as a repository, which qualifies it to be automatically created by component-scanning.
 *
 * @author Aliaksandr Pishchala
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class UserCompanyDaoImpl extends CriteriaQueryHelper<UserCompany> implements UserCompanyDao {

    private final static String COMPANY_ID = "userCompanyId";
    private final static String COMPANY_NAME = "name";

    @Override
    public int save(UserCompany userCompany) {
        Session session = getCurrentSession();
        session.save(userCompany);
        session.flush();

        return userCompany.getUserCompanyId();
    }

    @Override
    public boolean update(UserCompany userCompany) {
        Session session = getCurrentSession();
        session.saveOrUpdate(userCompany);
        return true;
    }

    @Override
    public List<UserCompany> companies(int limit, int offset) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<UserCompany> criteriaQuery = getCriteriaQuery(criteriaBuilder, UserCompany.class);
        Root<UserCompany> companyRoot = getRoot(criteriaQuery, UserCompany.class);
        criteriaQuery.select(companyRoot);
        return session.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public List<UserCompany> companiesByName(String name) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<UserCompany> criteriaQuery = getCriteriaQuery(criteriaBuilder, UserCompany.class);
        Root<UserCompany> companyRoot = getRoot(criteriaQuery, UserCompany.class);

        Predicate[] predicate  = new Predicate[1];
        predicate[0] = criteriaBuilder.like(companyRoot.get(COMPANY_NAME), createSearchCriteria(name));

        criteriaQuery.select(companyRoot).where(criteriaBuilder.or(predicate)).distinct(true)
                .orderBy(criteriaBuilder.desc(companyRoot.get(COMPANY_NAME)));

        Query<UserCompany> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Optional<UserCompany> companyById(Integer id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<UserCompany> criteriaQuery = getCriteriaQuery(criteriaBuilder, UserCompany.class);
        Root<UserCompany> companyRoot = getRoot(criteriaQuery, UserCompany.class);
        criteriaQuery.select(companyRoot).where(criteriaBuilder.equal(companyRoot.get(COMPANY_ID), id));

        return session.createQuery(criteriaQuery).stream().findFirst();
    }
}
