package org.study.hydrowarehouse.dao;

import org.study.hydrowarehouse.entity.ERole;
import org.study.hydrowarehouse.entity.Role;

import java.util.Optional;

/**
 * Interface {@link RoleDao} provides operation with data of database table 'roles'.
 *
 * @author Aliaksandr Pishchala
 */
public interface RoleDao {

    /**
     * The method returns the specified role by name.
     *
     * @param name is the Role's name.
     *
     * @return the specified Optional Role by name.
     */
    Optional<Role> findRole(ERole name);
}
