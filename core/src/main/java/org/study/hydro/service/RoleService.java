package org.study.hydro.service;

import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findRole(ERole role);
}
