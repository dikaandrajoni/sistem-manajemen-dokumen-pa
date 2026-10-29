package com.proyekakhir.manajemendokumen.menu;

import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/menu")
public class MenuController extends CrudController<Menu, MenuDto, MenuService, MenuRepository> {

    protected MenuController(MenuService crudService) {
        super(crudService);
    }
}
