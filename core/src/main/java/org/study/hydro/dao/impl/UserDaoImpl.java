package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.CriteriaQueryHelper;
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
@Transactional(rollbackFor = Exception.class)
public class UserDaoImpl extends CriteriaQueryHelper<User> implements UserDao {

    private static final String USER_ID = "userId";
    private static final String USER_EMAIL = "email";

    @Override
    public int save(User user) {
        Session session = getCurrentSession();

        session.persist(user);
        session.flush();
        return user.getUserId();
    }

    @Override
    public List<User> users(int limit, int offset) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<User> criteriaQuery = getCriteriaQuery(criteriaBuilder, User.class);
        Root<User> userRoot = getRoot(criteriaQuery, User.class);
        criteriaQuery.select(userRoot);
        return session.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public Optional<User> getUserById(Integer id) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<User> criteriaQuery = getCriteriaQuery(criteriaBuilder,User.class);
        Root<User> userRoot = getRoot(criteriaQuery, User.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(USER_ID), id));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<User> criteriaQuery = getCriteriaQuery(criteriaBuilder, User.class);
        Root<User> userRoot = getRoot(criteriaQuery, User.class);
        criteriaQuery.select(userRoot).where(criteriaBuilder.equal(userRoot.get(USER_EMAIL), email));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }
}
