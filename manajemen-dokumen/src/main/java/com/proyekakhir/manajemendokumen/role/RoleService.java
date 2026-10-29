package com.proyekakhir.manajemendokumen.role;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import org.springframework.stereotype.Service;


@Service
public class RoleService extends CrudManajemenService<Role, RoleDto, RoleRepository> {

    protected RoleService(RoleRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }
}
