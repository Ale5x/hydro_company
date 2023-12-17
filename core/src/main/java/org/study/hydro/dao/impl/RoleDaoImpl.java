package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.dao.RoleDao;
import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private SessionFactory sessionFactory;

    private static final String GET_ROLE_QUERY = "SELECT * FROM roles WHERE name=";
    private static final String ROLE_ID = "id_roles";
    private static final String ROLE_NAME = "name";
    private static final String ROLE_ACUTE = "'";
    private static final String ROLE_SEMICOLON = ";";


    @Override
    public Optional<Role> findRole(ERole eRole) {
        Session session = getCurrentSession();
        List<Object[]> objects = session.createNativeQuery(createQuery(eRole))
                .addScalar(ROLE_ID, IntegerType.INSTANCE)
                .addScalar(ROLE_NAME, StringType.INSTANCE)
                .list();
        Role role = new Role();
        for (Object[] object : objects) {
            role.setRoleId((Integer) object[0]);
            role.setName(ERole.valueOf((String) object[1]));
        }
        if(role.getName() == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(role);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private String createQuery(ERole eRole) {
        return  new StringBuilder(GET_ROLE_QUERY)
                .append(ROLE_ACUTE)
                .append(eRole.name())
                .append(ROLE_ACUTE)
                .append(ROLE_SEMICOLON)
                .toString();
    }
}