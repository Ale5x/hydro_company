package org.study.hydro.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.Company;
import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;
import org.study.hydro.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DevelopmentConfig.class)
@ActiveProfiles("development")
class UserDaoImplTest {

    @Autowired
    private UserDao userDao;
    private Company company = null;

    private int offset = 1;
    private int limit = 10;


    @BeforeEach
    void setUp() {
        company = new Company(1, "Company 1", "USA");

    }

    @Test
    void save() {
        User user = new User("First name", "Last name", "password",
                "path", LocalDateTime.now(), new Role(2, ERole.ADMIN), company);
        int countBeforeOperation = userDao.users(limit, offset).size();
        int userId = userDao.save(user);
        int countAfterOperation = userDao.users(limit, offset).size();
        assertTrue(userId >= 1);
        assertTrue(countAfterOperation > countBeforeOperation);
    }

    @Test
    void users() {
        List<User> userList = userDao.users(limit, offset);
        assertFalse(userList.isEmpty());
    }

    @Test
    void getUserById() {
        int userId = 1;
        Optional<User> user = userDao.getUserById(userId);
        assertTrue(user.isPresent());
    }
}