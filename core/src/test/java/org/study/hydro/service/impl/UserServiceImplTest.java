package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.UserCompany;
import org.study.hydro.entity.Dto.UserCompanyDto;
import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;
import org.study.hydro.entity.User;
import org.study.hydro.service.UserCompanyService;
import org.study.hydro.service.RoleService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private UserCompanyService userCompanyService;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto = new UserDto("Joy", "Smith", "joy@Email.com", "1234567", "path");

    private List<User> userList = new ArrayList<>();

    private Role role = new Role(1, ERole.MANAGER);
    private UserCompany userCompany = new UserCompany(1, "Google", "CA, street 1");
    private UserCompanyDto userCompanyDto = new UserCompanyDto(1, "Google", "CA, street 1");

    private int expectedTrueTest = 1;
    private int expectedWrongTest = 0;
    private int limit = 10;
    private int offset = 10;

    @BeforeEach
    void setUp() {

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUserId(i);
            user.setLastName("Last name -> " + i);
            user.setFirstName("First name -> " + i);
            user.setPassword("Password");
            user.setPathPhoto("photo");
            user.setRole(role);
            user.setRegistration(LocalDateTime.now());
            user.setUserCompany(userCompany);

            userList.add(user);
        }
    }

    @Test
    void createTrueTest() {
        when(userDao.save(any(User.class))).thenReturn(expectedTrueTest);
//        when(roleService.findRole(ERole.MANAGER)).thenReturn(Optional.ofNullable(role));
        when(userCompanyService.findById(userCompany.getUserCompanyId())).thenReturn(Optional.ofNullable(userCompanyDto));
        when(roleService.findRole(ERole.USER)).thenReturn(Optional.ofNullable(role));
        userDto.setUserCompanyDto(userCompanyDto);
        boolean condition = userService.create(userDto);

        assertTrue(condition);
    }

    @Test
    void createWrongTest() {
        when(userDao.save(any(User.class))).thenReturn(expectedWrongTest);
        when(roleService.findRole(ERole.USER)).thenReturn(Optional.ofNullable(role));
        when(userCompanyService.findById(userCompany.getUserCompanyId())).thenReturn(Optional.ofNullable(userCompanyDto));
        UserDto user = new UserDto();
        user.setUserCompanyDto(userCompanyDto);
        boolean condition = userService.create(user);

        assertFalse(condition);
    }

    @Test
    void findAll() {
        when(userDao.users(limit, offset)).thenReturn(userList);

        List<UserDto> userDtoList = userService.findAll(limit, offset);
        assertFalse(userDtoList.isEmpty());
        assertTrue(userDtoList.size() > 0);
    }

    @Test
    void findUserById() {
        int userId = 1;
        when(userDao.getUserById(userId)).thenReturn(userList
                .stream()
                .filter(item -> item.getUserId() == userId)
                .findFirst());

        Optional<UserDto> userDto = userService.findUserById(userId);
        assertTrue(userDto.isPresent());
    }

    @Test
    void findUserByIdWrongTest() {
        int userId = 1;
        when(userDao.getUserById(userId)).thenReturn(Optional.empty());
        Optional<UserDto> userDto = userService.findUserById(userId);
        assertFalse(userDto.isPresent());
    }
}