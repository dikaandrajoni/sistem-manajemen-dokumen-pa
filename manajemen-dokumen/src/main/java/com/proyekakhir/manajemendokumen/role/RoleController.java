package com.proyekakhir.manajemendokumen.role;

import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/role")
public class RoleController extends CrudController<Role, RoleDto, RoleService, RoleRepository> {

    protected RoleController(RoleService crudService) {
        super(crudService);
    }
}
