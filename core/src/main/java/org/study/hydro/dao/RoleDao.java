package org.study.hydro.dao;

import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;

import java.util.Optional;

/**
 * Interface {@link RoleDao} provides operation with data of database table 'roles'.
 *
 * @author Aliaksandr Pishchala
 */
public interface RoleDao {

    /**
     * The method returns specified role by name.
     *
     * @param name the Role's name.
     *
     * @return the specified Optional Role by name.
     */
    Optional<Role> findRole(ERole name);
}
