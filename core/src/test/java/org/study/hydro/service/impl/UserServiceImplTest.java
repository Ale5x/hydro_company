package org.study.hydro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydro.dao.RoleDao;
import org.study.hydro.dao.UserDao;
import org.study.hydro.entity.Company;
import org.study.hydro.entity.Dto.CompanyDto;
import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;
import org.study.hydro.entity.User;
import org.study.hydro.service.CompanyService;

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
    private CompanyService companyService;

    @Mock
    private RoleDao roleService;

    @InjectMocks
    private UserServiceImpl userService;

    private List<User> userList = new ArrayList<>();

    private Role role = new Role(1, ERole.MANAGER);
    private Company company = new Company(1, "Google", "CA, street 1");
    private CompanyDto companyDto = new CompanyDto(1, "Google", "CA, street 1");

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
            user.setCompany(company);

            userList.add(user);
        }
    }

    @Test
    void createTrueTest() {
        when(userDao.save(any(User.class))).thenReturn(expectedTrueTest);
        when(roleService.findRole(ERole.MANAGER)).thenReturn(Optional.ofNullable(role));
        when(companyService.findById(company.getCompanyId())).thenReturn(Optional.ofNullable(companyDto));
        UserDto user = new UserDto();
        user.setCompanyDto(companyDto);
        boolean condition = userService.create(user);

        System.out.println("Condition -> " + condition);

        assertTrue(condition);
    }

    @Test
    void createWrongTest() {
        when(userDao.save(any(User.class))).thenReturn(expectedWrongTest);
        when(roleService.findRole(ERole.MANAGER)).thenReturn(Optional.ofNullable(role));
        when(companyService.findById(company.getCompanyId())).thenReturn(Optional.ofNullable(companyDto));
        UserDto user = new UserDto();
        user.setCompanyDto(companyDto);
        boolean condition = userService.create(user);

        System.out.println("Condition -> " + condition);

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
        System.out.println(userDto);
        assertTrue(userDto.isPresent());
    }

    @Test
    void findUserByIdWrongTest() {
        int userId = 1;
        when(userDao.getUserById(userId)).thenReturn(Optional.empty());
        Optional<UserDto> userDto = userService.findUserById(userId);
        System.out.println(userDto);
        assertFalse(userDto.isPresent());
    }
}