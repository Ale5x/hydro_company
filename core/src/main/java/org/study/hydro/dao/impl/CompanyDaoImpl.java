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

/**
 * The type Role repository implements methods of the CompanyDao interface.
 * The class is annotated with as a repository, which qualifies it to be automatically created by component-scanning.
 *
 * @author Aliaksandr Pishchala
 */
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

    /**
     * The method creates the session instance from the currentSession.
     *
     * @return the session instance.
     */
    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * The method creates the CriteriaQuery of the Company instance from the criteriaBuilder instance.
     * @param criteriaBuilder is the criteriaBuilder instance.
     * @return the criteriaQuery instance.
     */
    private CriteriaQuery<Company> createQuery(CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.createQuery(Company.class);
    }

    /**
     * The method creates the CriteriaBuilder instance from the session.
     * @param session is the session instance.
     *
     * @return the CriteriaBuilder instance.
     */
    private CriteriaBuilder createCriteriaBuilder(Session session) {
        return session.getCriteriaBuilder();
    }

    /**
     * The method creates the Root of the Company instance from the criteriaQuery instance.
     * @param criteriaQuery is the criteriaQuery instance.
     * @return the Root of the Company instance.
     */
    private Root<Company> getRootCompany(CriteriaQuery<Company> criteriaQuery) {
        return criteriaQuery.from(Company.class);
    }

    /**
     * The method creates the criteria for searching in the company table.
     * @param name is the name any company.
     * @return String criteria type.
     */
    private String createSearchCriteria(String name) {
        return PERCENT_CHAR + name + PERCENT_CHAR;
    }
}
