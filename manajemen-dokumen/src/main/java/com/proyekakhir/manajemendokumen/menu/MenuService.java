package com.proyekakhir.manajemendokumen.menu;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import org.springframework.stereotype.Service;

@Service
public class MenuService extends CrudManajemenService<Menu, MenuDto, MenuRepository> {

    protected MenuService(MenuRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }
}
