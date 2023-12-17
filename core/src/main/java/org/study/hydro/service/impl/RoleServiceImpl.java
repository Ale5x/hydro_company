package org.study.hydro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.study.hydro.dao.RoleDao;
import org.study.hydro.entity.ERole;
import org.study.hydro.entity.Role;
import org.study.hydro.service.RoleService;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Optional<Role> findRole(ERole role) {
        return roleDao.findRole(role);
    }
}
