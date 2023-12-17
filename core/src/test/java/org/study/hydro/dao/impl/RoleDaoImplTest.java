package org.study.hydro.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.RoleDao;
import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DevelopmentConfig.class)
@ActiveProfiles("development")
class RoleDaoImplTest {

    @Autowired
    private RoleDao roleDao;

    @Test
    void findRoleRightTest() {
        Optional<Role> role = roleDao.findRole(ERole.ADMIN);
        assertTrue(role.isPresent());
    }
}