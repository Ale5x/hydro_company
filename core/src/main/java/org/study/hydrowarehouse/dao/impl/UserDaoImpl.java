package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.UserDao;
import org.study.hydrowarehouse.entity.ERole;
import org.study.hydrowarehouse.entity.Role;
import org.study.hydrowarehouse.entity.User;
import org.study.hydrowarehouse.entity.UserStatus;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
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
    private static final String COUNTRY_ID = "countryId";
    private static final String USER_EMAIL = "email";
    private static final String FIND_USERS_BY_ROLE_QUERY = "FROM User u WHERE u.role.name = :roleName";
    private static final String FIND_USERS_BY_COUNTRY_QUERY = "SELECT u FROM User u JOIN u.userCompany uc " +
            "JOIN uc.country c WHERE c.countryId = :countryId ORDER BY u.userId ASC";

    @Override
    public int save(User user) {
        Session session = getCurrentSession();

        session.persist(user);
        session.flush();
        return user.getUserId();
    }

    @Override
    public boolean update(User user) {
        Session session = getCurrentSession();
        session.update(user);
        return true;
    }

    @Override
    public List<User> users(int limit, int offset) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder(session);
        CriteriaQuery<User> criteriaQuery = getCriteriaQuery(criteriaBuilder, User.class);
        Root<User> userRoot = getRoot(criteriaQuery, User.class);
        criteriaQuery.select(userRoot)
                .orderBy(criteriaBuilder.asc(userRoot.get(USER_ID)));
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

    @Override
    public List<User> findByStatus(String status, int limit, int offset) {
        Session session = getCurrentSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);

        Join<User, UserStatus> statusJoin = userRoot.join("status");

        criteriaQuery.select(userRoot)
                .where(criteriaBuilder.equal(statusJoin.get("status"), status))
                .orderBy(criteriaBuilder.asc(userRoot.get("userId")));

        return session.createQuery(criteriaQuery)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public List<User> findByCountry(int countryId, int limit, int offset) {
        Session session = getCurrentSession();

        return session.createQuery(FIND_USERS_BY_COUNTRY_QUERY, User.class)
                .setParameter(COUNTRY_ID, countryId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<User> findAllByRole(ERole roleName, int limit, int offset) {
        Session session = getCurrentSession();
        return session.createQuery(FIND_USERS_BY_ROLE_QUERY, User.class)
                .setParameter("roleName", roleName)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
    }
}
