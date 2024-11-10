package org.study.hydro.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Class {@link CriteriaQueryHelper} helps provide work with the database. Creating a session, criteria,
 * creating definition criteria and building criteria.
 *
 * @param <T> is an entity class type that participates in requests to retrieve data from the database.
 */
@Component
public class CriteriaQueryHelper<T> {

    private final static char PERCENT_CHAR = '%';

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * The method creates the session instance from the SessionFactory.
     *
     * @return the session instance.
     */
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * The method creates the CriteriaBuilder instance from the session.
     * @param session is the session instance.
     *
     * @return the CriteriaBuilder instance.
     */
    protected CriteriaBuilder getCriteriaBuilder(Session session) {
        return session.getCriteriaBuilder();
    }

    /**
     * The method creates the CriteriaQuery of the <T> entity from the criteriaBuilder instance
     * that has the <T> type of the entity.
     * @param criteriaBuilder is the criteriaBuilder instance.
     * @param typeClass is class type of the entity.
     * @return the criteriaQuery instance type of the <T> entity.
     * @param <T> is the entity class type.
     */
    protected <T> CriteriaQuery<T> getCriteriaQuery(CriteriaBuilder criteriaBuilder, Class<T> typeClass) {
        return criteriaBuilder.createQuery(typeClass);
    }

    /**
     * The method creates the Root of the <T> entity from the criteriaQuery instance that has
     * the <T> type of the entity.
     * @param criteriaQuery is the criteriaQuery instance's type of the <T> entity.
     * @param typeClass is class type of the entity.
     * @return the Root instance type of the <T> entity.
     * @param <T> is the entity class type.
     */
    protected <T> Root<T> getRoot(CriteriaQuery<T> criteriaQuery, Class<T> typeClass) {
        return criteriaQuery.from(typeClass);
    }

    /**
     * The method builds the query criteria. It wraps the word into characters for SQL Query.
     * @param name The keyword.
     * @return The keyword in characters for SQL Query.
     */
    protected String createSearchCriteria(String name) {
        return PERCENT_CHAR + name + PERCENT_CHAR;
    }

    /**
     * The method creates predicates to search for data in the required columns.
     * @param criteriaList has the names of the columns in the table in which you need to search.
     * @param criteriaBuilder is the criteriaBuilder instance.
     * @param root is the root instance type of the <T> entity.
     * @param searchName is the name by which information is searched in the database
     * @return the array of the predicates.
     */
    protected Predicate[] createPredicates(List<String> criteriaList, CriteriaBuilder criteriaBuilder, Root<T> root, String searchName) {
        Predicate[] predicates = new Predicate[criteriaList.size()];
        System.out.println("predicates size is -> " + predicates.length + " | list size -> " + criteriaList.size());
        int count = 0;
        for (String criteria : criteriaList) {
            System.out.println("START FOR EACH");
            System.out.println("count -> " + count);
            predicates[count] = criteriaBuilder.like(root.get(criteria), createSearchCriteria(searchName));
            count++;
            System.out.println("COUNT -> " + count + " | Criteria -> " + criteria);
        }
        System.out.println("Predicates all " + predicates.toString());
        return predicates;
    }
}
