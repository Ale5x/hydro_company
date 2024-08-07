package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * The type User repository implements methods of the UserDao interface.
 * The class is annotated with as a repository, which qualifies it to be automatically created by component-scanning.
 *
 * @author Aliaksandr Pishchala
 */
@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    private static final String USER_ID = "userId";
    private static final String USER_EMAIL = "email";

    @Override
    public int save(User user) {
        Session session = getCurrentSession();

        session.save(user);
        session.flush();
        return user.getUserId();
    }

    @Override
    public List<User> users(int limit, int offset) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<User> criteriaQuery = getCriteriaQuery(criteriaBuilder);
        Root<User> userRoot = getUserRoot(criteriaQuery);
        criteriaQuery.select(userRoot);
        return session.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public Optional<User> getUserById(int id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<User> criteriaQuery = getCriteriaQuery(criteriaBuilder);
        Root<User> userRoot = getUserRoot(criteriaQuery);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(USER_ID), id));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<User> criteriaQuery = getCriteriaQuery(criteriaBuilder);
        Root<User> userRoot = getUserRoot(criteriaQuery);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(USER_EMAIL), email));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
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
     * The method creates the CriteriaBuilder instance from the session.
     * @param session is the session instance.
     *
     * @return the CriteriaBuilder instance.
     */
    private CriteriaBuilder getCriteriaBuilder(Session session) {
        return session.getCriteriaBuilder();
    }

    /**
     * The method creates the CriteriaQuery of the User instance from the criteriaBuilder instance.
     * @param criteriaBuilder is the criteriaBuilder instance.
     * @return the criteriaQuery instance.
     */
    private CriteriaQuery<User> getCriteriaQuery(CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.createQuery(User.class);
    }

    /**
     * The method creates the Root of the User instance from the criteriaQuery instance.
     * @param criteriaQuery is the criteriaQuery instance.
     * @return the Root of the User instance.
     */
    private Root<User> getUserRoot(CriteriaQuery<User> criteriaQuery) {
        return criteriaQuery.from(User.class);
    }
}
