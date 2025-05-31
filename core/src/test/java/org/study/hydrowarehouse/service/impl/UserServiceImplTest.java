package org.study.hydrowarehouse.service.impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.study.hydrowarehouse.dao.UserDao;
import org.study.hydrowarehouse.entity.*;
import org.study.hydrowarehouse.entity.Dto.UserCompanyDto;
import org.study.hydrowarehouse.entity.Dto.UserDto;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.service.ServiceMediator;
import org.study.hydrowarehouse.service.RoleService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private ServiceMediator serviceMediator;

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
            user.setStatus(new UserStatus("ACTIVE"));

            userList.add(user);
        }
    }

    @Test
    void createTrueTest() {
        when(userDao.save(any(User.class))).thenReturn(expectedTrueTest);
        when(serviceMediator.findUserCompanyById(userCompany.getUserCompanyId())).thenReturn(Optional.ofNullable(userCompany));
        when(roleService.findRole(ERole.USER)).thenReturn(Optional.ofNullable(role));
        userDto.setUserCompanyDto(userCompanyDto);
        boolean condition = userService.create(userDto);

        assertTrue(condition);
    }

    @Test
    void testUpdate_successfulWithExistingCompany() throws CoreException {
        UserDto userDto = new UserDto();
        userDto.setUserDtoId(1);
        userDto.setFirstName("Updated FirstName");
        userDto.setLastName("Updated LastName");
        userDto.setEmail("updated@example.com");
        userDto.setPassword("newpassword");
        userDto.setPathPhoto("new/photo/path");

        UserCompanyDto userCompanyDto = new UserCompanyDto();
        userCompanyDto.setCompanyDtoId(2);  // Company exists
        userDto.setUserCompanyDto(userCompanyDto);

        User existingUser = new User();
        existingUser.setUserId(1);
        existingUser.setFirstName("Old FirstName");
        existingUser.setLastName("Old LastName");
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldpassword");
        existingUser.setPathPhoto("old/photo/path");
        existingUser.setUserCompany(new UserCompany(2, "Old Company", "Address"));

        UserCompany existingCompany = new UserCompany(2, "Existing Company", "Address");
        when(userDao.getUserById(1)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedpassword");
        when(userDao.update(existingUser)).thenReturn(true);

        boolean result = userService.update(userDto);

        assertTrue(result);
        assertEquals("Updated FirstName", existingUser.getFirstName());
        assertEquals("Updated LastName", existingUser.getLastName());
        assertEquals("updated@example.com", existingUser.getEmail());
        assertEquals("encodedpassword", existingUser.getPassword());
        assertEquals("new/photo/path", existingUser.getPathPhoto());
        verify(userDao).update(existingUser);
    }

    @Test
    void testUpdate_successfulWithNewCompany() throws CoreException {
        UserDto userDto = new UserDto();
        userDto.setUserDtoId(1);
        userDto.setFirstName("Updated FirstName");

        UserCompanyDto userCompanyDto = new UserCompanyDto();
        userCompanyDto.setCompanyDtoId(null);
        userCompanyDto.setName("New Company");
        userDto.setUserCompanyDto(userCompanyDto);

        User existingUser = new User();
        existingUser.setUserId(1);
        existingUser.setFirstName("Old FirstName");

        existingUser.setUserCompany(new UserCompany(1, "name", "adress"));

        when(userDao.getUserById(1)).thenReturn(Optional.of(existingUser));
        when(userDao.update(existingUser)).thenReturn(true);

        boolean result = userService.update(userDto);

        assertTrue(result);
        assertEquals("Updated FirstName", existingUser.getFirstName());
        assertNotNull(existingUser.getUserCompany());
        assertEquals("New Company", existingUser.getUserCompany().getName());
        verify(userDao).update(existingUser);
    }

    @Test
    void testUpdate_successfulWithChangeNewCompany() throws CoreException {
        UserDto userDto = new UserDto();
        userDto.setUserDtoId(1);

        UserCompanyDto newCompanyDto = new UserCompanyDto();
        newCompanyDto.setCompanyDtoId(2);
        newCompanyDto.setName("NewCompany");
        newCompanyDto.setAddress("New Address");
        userDto.setUserCompanyDto(newCompanyDto);

        User existingUser = new User();
        existingUser.setUserId(1);

        UserCompany oldCompany = new UserCompany();
        oldCompany.setUserCompanyId(1);
        oldCompany.setName("OldCompany");
        existingUser.setUserCompany(oldCompany);

        when(userDao.getUserById(1)).thenReturn(Optional.of(existingUser));

        UserCompany newCompany = new UserCompany();
        newCompany.setUserCompanyId(2);
        newCompany.setName("NewCompany");
        newCompany.setAddress("New Address");
        when(serviceMediator.findUserCompanyById(2)).thenReturn(Optional.of(newCompany));

        when(userDao.update(any(User.class))).thenReturn(true);

        boolean result = userService.update(userDto);

        assertTrue(result);
        assertEquals(2, existingUser.getUserCompany().getUserCompanyId());
        assertEquals("NewCompany", existingUser.getUserCompany().getName());

        verify(userDao).update(existingUser);
    }

    @Test
    void testUpdate_shouldHandleNullUserCompanyAndUserCompanyDto() throws CoreException {
        UserDto userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setUserDtoId(1);

        User existingUser = new User();
        existingUser.setUserId(1);
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setUserCompany(null);

        when(userDao.getUserById(1)).thenReturn(Optional.of(existingUser));
        when(userDao.update(any(User.class))).thenReturn(true); // Ожидаем успешное обновление

        boolean result = userService.update(userDto);

        assertTrue(result);

        verify(serviceMediator, never()).findUserCompanyById(any());

        verify(userDao, times(1)).update(existingUser);
    }

    @Test
    void testUpdate_userNotFound_throwsException() {
        UserDto userDto = new UserDto();
        userDto.setUserDtoId(404);

        when(userDao.getUserById(404)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () -> userService.update(userDto));

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void createWrongTest() {
        when(userDao.save(any(User.class))).thenReturn(expectedWrongTest);
        when(roleService.findRole(ERole.USER)).thenReturn(Optional.ofNullable(role));
        when(serviceMediator.findUserCompanyById(userCompany.getUserCompanyId())).thenReturn(Optional.ofNullable(userCompany));
        UserDto user = new UserDto();
        user.setUserCompanyDto(userCompanyDto);
        boolean condition = userService.create(user);

        assertFalse(condition);
    }

    @Test
    void findAll() {
        when(userDao.users(offset, limit)).thenReturn(userList);

        List<UserDto> userDtoList = userService.findAll(offset, limit);
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

    @Test
    void shouldReturnRoleWhenRoleExistsInDto() {
        UserDto dto = new UserDto();
        dto.setRole(Collections.singleton("admin"));

        Role expectedRole = new Role(ERole.ADMIN);

        Mockito.when(roleService.findRole(ERole.ADMIN)).thenReturn(Optional.of(expectedRole));

        Role result = userService.mapUserRole(dto);

        assertEquals(expectedRole, result);
    }


    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        UserDto dto = new UserDto();
        dto.setRole(Collections.singleton("user"));

        Mockito.when(roleService.findRole(ERole.USER)).thenReturn(Optional.empty());

        assertThrows(CoreException.class, () -> userService.mapUserRole(dto));
    }

    @Test
    void shouldReturnDefaultUserRoleWhenDtoHasNoRole(){
        UserDto dto = new UserDto();
        dto.setRole(null);

        Role result = userService.mapUserRole(dto);

        assertNotNull(result);
        assertEquals(ERole.USER, result.getName());
    }

    @Test
    void findAllByStatus_shouldReturnUserDtoList() throws CoreException {
        String status = "active";
        int offset = 0;
        int limit = 10;

        User user = new User();
        user.setUserId(1);
        user.setFirstName("john");
        user.setRole(new Role(ERole.USER));
        user.setStatus(new UserStatus(1L, "ACTIVE"));

        List<User> users = List.of(user);

        Mockito.when(userDao.findByStatus("ACTIVE", limit, offset)).thenReturn(users);

        List<UserDto> result = userService.findAllByStatus(status, offset, limit);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("john", result.get(0).getFirstName());
        assertEquals("ACTIVE", result.get(0).getStatus());
    }

    @Test
    void findAllByStatus_shouldThrowCoreExceptionWhenDaoFails() {
        int offset = 0;
        int limit = 5;

        when(userDao.findByStatus(anyString(), eq(limit), eq(offset)))
                .thenThrow(new CoreException());

        assertThrows(CoreException.class, () -> userService.findAllByStatus("blocked", offset, limit));
    }

    @Test
    void changeStatus_success() throws CoreException {
        Integer userId = 1;
        String newStatus = "ACTIVE";

        User user = new User();
        UserStatus status = new UserStatus();
        status.setStatus("ACTIVE");

        Mockito.when(userDao.getUserById(userId)).thenReturn(Optional.of(user));
        Mockito.when(serviceMediator.findByStatus(newStatus)).thenReturn(Optional.of(status));

        boolean result = userService.changeStatus(userId, newStatus);

        assertTrue(result);
        verify(userDao).update(user);
        assertEquals(status, user.getStatus());
    }

    @Test
    void changeStatus_userNotFound_shouldThrowException() {
        Integer userId = 1;
        String newStatus = "ACTIVE";

        Mockito.when(userDao.getUserById(userId)).thenReturn(Optional.empty());

        CoreException exception = assertThrows(CoreException.class, () ->
                userService.changeStatus(userId, newStatus)
        );

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userDao, never()).update(any());
    }

    @Test
    void changeStatus_statusNotFound_shouldThrowException() {
        Integer userId = 1;
        String newStatus = "UNKNOWN";

        User user = new User();
        Mockito.when(userDao.getUserById(userId)).thenReturn(Optional.of(user));
        Mockito.when(serviceMediator.findByStatus(newStatus)).thenReturn(Optional.empty());

        CoreException exception = assertThrows(CoreException.class, () ->
                userService.changeStatus(userId, newStatus)
        );

        assertTrue(exception.getMessage().contains("User Status"));
        verify(userDao, never()).update(any());
    }

    @Test
    void testFindAllByRole_ValidRole_ReturnsUserDtos() throws CoreException {
        String role = "ADMIN";
        int offset = 0;
        int limit = 10;

        Mockito.when(userDao.findAllByRole(ERole.ADMIN, limit, offset)).thenReturn(userList);

        List<UserDto> result = userService.findAllByRole(role, offset, limit);

        assertFalse(result.isEmpty());
        verify(userDao).findAllByRole(ERole.ADMIN, limit, offset);
    }

    @Test
    void testFindAllByRole_InvalidRole_ThrowsCoreException() {
        String invalidRole = "STUDENT";

        CoreException ex = assertThrows(CoreException.class,
                () -> userService.findAllByRole(invalidRole, 0, 10));

        assertTrue(ex.getMessage().contains("User Role"));
    }

    @Test
    void changeRole_WhenUserNotFound_ThrowsCoreException() {
        String role = ERole.ADMIN.name();
        when(userDao.getUserById(1)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () ->
                userService.changeRole(1, role));

        assertTrue(ex.getMessage().contains("User not found"));
        verify(userDao, never()).update(any());
    }

    @Test
    void changeRole_WhenRoleNotFound_ThrowsCoreException() throws CoreException {
        User user = new User();
        String role = ERole.USER.name();
        when(userDao.getUserById(1)).thenReturn(Optional.of(user));
        when(serviceMediator.findRole(ERole.USER)).thenReturn(Optional.empty());

        CoreException ex = assertThrows(CoreException.class, () ->
                userService.changeRole(1, role));

        assertTrue(ex.getMessage().contains("User Role"));
        verify(userDao, never()).update(any());
    }

    @Test
    void changeRole_WhenValidUserAndRole_UpdatesUserAndReturnsTrue() throws CoreException {
        User user = new User();
        Role role = new Role();
        String roleString = ERole.USER.name();

        when(userDao.getUserById(1)).thenReturn(Optional.of(user));
        when(serviceMediator.findRole(ERole.USER)).thenReturn(Optional.of(role));

        boolean result = userService.changeRole(1, roleString);

        assertTrue(result);
        assertEquals(role, user.getRole());
        verify(userDao).update(user);
    }
}