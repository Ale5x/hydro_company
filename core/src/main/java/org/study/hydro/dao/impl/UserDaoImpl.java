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

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    private static final String USER_ID = "userId";

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

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private CriteriaBuilder getCriteriaBuilder(Session session) {
        return session.getCriteriaBuilder();
    }

    private CriteriaQuery<User> getCriteriaQuery(CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.createQuery(User.class);
    }

    private Root<User> getUserRoot(CriteriaQuery<User> criteriaQuery) {
        return criteriaQuery.from(User.class);
    }
}
