package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CompanyDao;
import org.study.hydro.entity.Company;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CompanyDaoImpl implements CompanyDao {

    @Autowired
    private SessionFactory sessionFactory;

    private final static char PERCENT_CHAR = '%';
    private final static String COMPANY_ID = "companyId";
    private final static String COMPANY_NAME = "name";


    @Override
    public int save(Company company) {
        Session session = getCurrentSession();
        session.save(company);
        session.flush();

        return company.getCompanyId();
    }

    @Override
    public List<Company> companies(int limit, int offset) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = createCriteriaBuilder(session);
        CriteriaQuery<Company> criteriaQuery = createQuery(criteriaBuilder);
        Root<Company> companyRoot = getRootCompany(criteriaQuery);
        criteriaQuery.select(companyRoot);
        return session.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public List<Company> companiesByName(String name) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = createCriteriaBuilder(session);
        CriteriaQuery<Company> criteriaQuery = createQuery(criteriaBuilder);
        Root<Company> companyRoot = getRootCompany(criteriaQuery);

        Predicate[] predicate  = new Predicate[1];
        predicate[0] = criteriaBuilder.like(companyRoot.get(COMPANY_NAME), createSearchCriteria(name));

        criteriaQuery.select(companyRoot).where(criteriaBuilder.or(predicate)).distinct(true)
                .orderBy(criteriaBuilder.desc(companyRoot.get(COMPANY_NAME)));

        Query<Company> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Optional<Company> companyById(int id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = createCriteriaBuilder(session);
        CriteriaQuery<Company> criteriaQuery = createQuery(criteriaBuilder);
        Root<Company> companyRoot = getRootCompany(criteriaQuery);
        criteriaQuery.select(companyRoot).where(criteriaBuilder.equal(companyRoot.get(COMPANY_ID), id));

        return session.createQuery(criteriaQuery).stream().findFirst();
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private CriteriaQuery<Company> createQuery(CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.createQuery(Company.class);
    }

    private CriteriaBuilder createCriteriaBuilder(Session session) {
        return session.getCriteriaBuilder();
    }

    private Root<Company> getRootCompany(CriteriaQuery<Company> criteriaQuery) {
        return criteriaQuery.from(Company.class);
    }

    private String createSearchCriteria(String name) {
        return PERCENT_CHAR + name + PERCENT_CHAR;
    }
}
