package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.entity.ERole;
import org.study.hydrowarehouse.entity.Role;

import java.util.Optional;

/**
 * The interface {@link RoleService} User service contains methods for business logic with role.
 *
 * @author Aliaksandr Pishchala
 */
public interface RoleService {

    /**
     * The method returns specified all information about Role by role.
     * @param role is the user's role in this app.
     * @return the specified Optional RoleDto
     */
    Optional<Role> findRole(ERole role);
}
