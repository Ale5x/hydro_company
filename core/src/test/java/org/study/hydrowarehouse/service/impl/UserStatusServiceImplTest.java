package org.study.hydrowarehouse.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.study.hydrowarehouse.dao.UserStatusDao;
import org.study.hydrowarehouse.entity.Dto.UserStatusDto;
import org.study.hydrowarehouse.entity.UserStatus;
import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserStatusServiceImplTest {

    @Mock
    private UserStatusDao userStatusDao;

    @InjectMocks
    private UserStatusServiceImpl userStatusService;

    @Test
    void findAll_shouldReturnListOfUserStatusDto() throws CoreException {
        UserStatus userStatus = new UserStatus(1L, "ACTIVE");
        List<UserStatus> statuses = List.of(userStatus);

        Mockito.when(userStatusDao.findAll()).thenReturn(statuses);

        List<UserStatusDto> result = userStatusService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACTIVE", result.get(0).getStatus());
    }

    @Test
    void findByStatus_shouldReturnUserStatusDto() throws CoreException {
        String status = "BLOCKED";
        UserStatus userStatus = new UserStatus(2L, status);

        Mockito.when(userStatusDao.findByStatus(status)).thenReturn(Optional.of(userStatus));

        Optional<UserStatusDto> result = userStatusService.findByStatus(status);

        assertTrue(result.isPresent());
        assertEquals(status, result.get().getStatus());
    }

    @Test
    void findById_shouldReturnUserStatusDto() throws CoreException {
        Long id = 3L;
        UserStatus userStatus = new UserStatus(id, "INACTIVE");

        Mockito.when(userStatusDao.findById(id)).thenReturn(Optional.of(userStatus));

        Optional<UserStatusDto> result = userStatusService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getUserStatusIdDto());
        assertEquals("INACTIVE", result.get().getStatus());
    }

    @Test
    void findByStatus_shouldReturnEmptyOptionalIfNotFound() throws CoreException {
        String status = "UNKNOWN";

        Mockito.when(userStatusDao.findByStatus(status)).thenReturn(Optional.empty());

        Optional<UserStatusDto> result = userStatusService.findByStatus(status);

        assertFalse(result.isPresent());
    }

    @Test
    void findById_shouldThrowCoreExceptionWhenDaoFails() {
        Long id = 999L;

        Mockito.when(userStatusDao.findById(id)).thenThrow(new CoreException());

        assertThrows(CoreException.class, () -> userStatusService.findById(id));
    }
}