package org.study.hydro.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydro.configuration.DevelopmentConfig;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.UserCompany;
import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;
import org.study.hydro.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DevelopmentConfig.class)
@ActiveProfiles("development")
@TestPropertySource(locations = "classpath:application-development.properties")
class UserDaoImplTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionFactory sessionFactory;

    private UserCompany userCompany = null;

    private int offset = 1;
    private int limit = 10;


    @BeforeEach
    void setUp() {
        userCompany = new UserCompany(1, "Company 1", "USA");
    }

    @Test
    void save() {
        User user = new User("First name", "Last name", "email@email.com","password",
                "path", LocalDateTime.now(), new Role(2, ERole.ADMIN), userCompany);
        int countBeforeOperation = userDao.users(limit, offset).size();
        int userId = userDao.save(user);
        int countAfterOperation = userDao.users(limit, offset).size();
        assertTrue(userId >= 1);
        assertTrue(countAfterOperation > countBeforeOperation);
    }

    @Test
    @Transactional
    void update() {
        User user = new User();
        user.setFirstName("OldFirst");
        user.setLastName("Last");
        user.setEmail("test@example.com");
        user.setPassword("pass");

        Session session = sessionFactory.getCurrentSession();
        session.save(user);
        session.flush();

        Integer id = user.getUserId();

        user.setFirstName("NewFirst");
        boolean updated = userDao.update(user);

        assertTrue(updated);

        User updatedUser = session.get(User.class, id);
        assertEquals("NewFirst", updatedUser.getFirstName());
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

    @Test
    void getUserByEmail() {
        int userId = 1;
        String email = userDao.getUserById(userId).orElseThrow().getEmail();
        assertNotNull(email);
        Optional<User> user = userDao.getUserByEmail(email);
        assertTrue(user.isPresent());
        assertEquals(email, user.orElseThrow().getEmail());
    }
}