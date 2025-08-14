package org.study.hydrowarehouse.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.study.hydrowarehouse.configuration.DevelopmentConfig;
import org.study.hydrowarehouse.dao.UserStatusDao;
import org.study.hydrowarehouse.entity.UserStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DevelopmentConfig.class)
@ActiveProfiles("development")
@TestPropertySource(locations = "classpath:application-development.properties")
class UserStatusDaoImplTest {

    @Autowired
    private UserStatusDao userStatusDao;

    @Test
    void findAll() {
        List<UserStatus> statuses = userStatusDao.findAll();
        assertFalse(statuses.isEmpty());
    }

    @Test
    void findByStatus() {
        Optional<UserStatus> userStatus = userStatusDao.findByStatus("active".toUpperCase());
        assertTrue(userStatus.isPresent());
    }

    @Test
    void findByStatusWhenStatusIsNotExist() {
        Optional<UserStatus> userStatus = userStatusDao.findByStatus("live");
        assertTrue(userStatus.isEmpty());
    }

    @Test
    void findById() {
        Optional<UserStatus> userStatus = userStatusDao.findById(1l);
        assertTrue(userStatus.isPresent());
    }

    @Test
    void findByIdWhenIdIsNotExist() {
        Optional<UserStatus> userStatus = userStatusDao.findById(55l);
        assertTrue(userStatus.isEmpty());
    }
}