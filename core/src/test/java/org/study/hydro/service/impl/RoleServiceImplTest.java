package org.study.hydro.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydro.dao.RoleDao;
import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private RoleServiceImpl roleService;

    private ERole role = ERole.USER;
    private Role actualRole = new Role(1, ERole.USER);


    @Test
    void findRole() {
        when(roleDao.findRole(role)).thenReturn(Optional.of(actualRole));
        Optional<Role> expectedRole = roleService.findRole(ERole.USER);

        assertTrue(expectedRole.isPresent());
        assertEquals(expectedRole.get().getName(), actualRole.getName());
    }
}