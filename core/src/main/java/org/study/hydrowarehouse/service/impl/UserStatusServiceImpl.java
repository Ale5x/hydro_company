package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.UserStatusDao;
import org.study.hydrowarehouse.entity.Dto.UserStatusDto;
import org.study.hydrowarehouse.entity.UserStatus;
import org.study.hydrowarehouse.exception.CoreException;
import org.study.hydrowarehouse.mapping.EntityMapper;
import org.study.hydrowarehouse.service.UserStatusService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing {@link UserStatus} entities.
 * <p>
 * Provides business logic and transactional operations for creating, updating, retrieving, and deleting user statuses.
 * Extends {@link EntityMapper} to handle mapping between {@link UserStatusDto} and {@link UserStatus} entities.
 * </p>
 *
 * @see EntityMapper
 * @see UserStatusService
 * @see UserStatus
 * @see UserStatusDto
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class UserStatusServiceImpl extends EntityMapper<UserStatusDto, UserStatus> implements UserStatusService {

    private final UserStatusDao userStatusDao;

    @Autowired
    public UserStatusServiceImpl(UserStatusDao userStatusDao) {
        this.userStatusDao = userStatusDao;
    }

    @Override
    public List<UserStatusDto> findAll() throws CoreException {
        return mapToListObjectsDto(userStatusDao.findAll());
    }

    @Override
    public Optional<UserStatusDto> findByStatus(String status) throws CoreException {
        Optional<UserStatus> userStatus = userStatusDao.findByStatus(status.toUpperCase());
        if (userStatus.isEmpty()) {
            //logger
            return Optional.empty();
        }
        return userStatus.map(this::mapToObjectDto);
    }

    @Override
    public Optional<UserStatusDto> findById(Long statusId) throws CoreException {
        Optional<UserStatus> userStatus = userStatusDao.findById(statusId);
        if (userStatus.isEmpty()) {
            //logger
            return Optional.empty();
        }
        return userStatus.map(this::mapToObjectDto);
    }

    @Override
    public List<UserStatusDto> mapToListObjectsDto(List<UserStatus> objectsList) {
        if (objectsList == null) return null;
        List<UserStatusDto> userStatusDtoList = new ArrayList<>();

        for (UserStatus userStatus : objectsList) {

            userStatusDtoList.add(mapToObjectDto(userStatus));
        }
        return userStatusDtoList;
    }

    @Override
    public UserStatusDto mapToObjectDto(UserStatus object) {
        if (object == null) return null;
        UserStatusDto userStatusDto = new UserStatusDto();
        userStatusDto.setUserStatusIdDto(object.getUserStatusId());
        userStatusDto.setStatus(object.getStatus());

        return userStatusDto;
    }
}
