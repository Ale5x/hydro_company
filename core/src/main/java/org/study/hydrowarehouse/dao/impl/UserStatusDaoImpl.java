package org.study.hydrowarehouse.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.CriteriaQueryHelper;
import org.study.hydrowarehouse.dao.UserStatusDao;
import org.study.hydrowarehouse.entity.UserStatus;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserStatusDao} that provides database access operations
 * for {@link UserStatus} entities.
 * <p>
 * This class extends {@link CriteriaQueryHelper} to reuse common JPA Criteria API functionality.
 * It is annotated with {@link org.springframework.stereotype.Repository} to mark it as a persistence component,
 * and with {@link org.springframework.transaction.annotation.Transactional} to enable transaction management.
 * </p>
 *
 * <p>All operations are transactional with rollback for any {@link Exception}.</p>
 *
 * @author Aliaksandr Pishchala
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class UserStatusDaoImpl extends CriteriaQueryHelper<UserStatus> implements UserStatusDao {

    private final static String FIND_STATUS_QUERY = "FROM UserStatus WHERE status = :status";
    private final static String FIND_STATUS_BY_ID_QUERY = "FROM UserStatus WHERE userStatusId = :userStatusId";
    private final static String USER_STATUS = "status";
    private final static String USER_STATUS_ID = "userStatusId";


    @Override
    public List<UserStatus> findAll() {
        Session session = getCurrentSession();
        return session.createQuery("FROM UserStatus", UserStatus.class).list();
    }

    @Override
    public Optional<UserStatus> findByStatus(String status) {
        Session session = getCurrentSession();
        UserStatus userStatus = session.createQuery(
                        FIND_STATUS_QUERY, UserStatus.class)
                .setParameter(USER_STATUS, status)
                .uniqueResult();

        return Optional.ofNullable(userStatus);
    }

    @Override
    public Optional<UserStatus> findById(Long statusId) {
        Session session = getCurrentSession();
        UserStatus userStatus = session.createQuery(
                        FIND_STATUS_BY_ID_QUERY, UserStatus.class)
                .setParameter(USER_STATUS_ID, statusId)
                .uniqueResult();

        return Optional.ofNullable(userStatus);
    }
}
