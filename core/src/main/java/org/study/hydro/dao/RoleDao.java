package org.study.hydro.dao;

import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;

import java.util.Optional;

public interface RoleDao {

    Optional<Role> findRole(ERole name);

}
