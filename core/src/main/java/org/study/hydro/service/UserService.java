package org.study.hydro.service;

import org.study.hydro.entity.Dto.UserDto;
import org.study.hydro.exception.CoreException;

import java.util.List;
import java.util.Optional;

public interface UserService {

    boolean create(UserDto user) throws CoreException;

    List<UserDto> findAll(int limit, int offset);

    Optional<UserDto> findUserById(int id);
}
