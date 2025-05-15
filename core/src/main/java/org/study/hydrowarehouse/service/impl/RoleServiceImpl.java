package org.study.hydrowarehouse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.hydrowarehouse.dao.RoleDao;
import org.study.hydrowarehouse.entity.ERole;
import org.study.hydrowarehouse.entity.Role;
import org.study.hydrowarehouse.service.RoleService;

import java.util.Optional;

/**
 * The class {@link RoleServiceImpl} implements methods of the RoleService interface.
 * The class is annotated as a service, which qualifies it to be automatically created by component-scanning.
 *
 * @author Aliaksandr Pishchala
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Optional<Role> findRole(ERole role) {
        return roleDao.findRole(role);
    }
}
