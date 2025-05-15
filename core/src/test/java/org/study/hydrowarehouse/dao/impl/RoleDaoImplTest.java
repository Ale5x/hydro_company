package org.study.hydrowarehouse.dao.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydrowarehouse.configuration.DevelopmentConfig;
import org.study.hydrowarehouse.dao.RoleDao;
import org.study.hydrowarehouse.entity.ERole;
import org.study.hydrowarehouse.entity.Role;

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