package com.proyekakhir.manajemendokumen.services;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;

public class CrudManajemenService<E, D, R> extends CrudService<E, D, R> {

    @Autowired
    protected CurrentUser currentUser;

    protected CrudManajemenService(R simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository);
        this.currentUser = currentUser;
    }


    @Override
    public String getCurrentUser() {
        return currentUser.getUserId();
    }

    @Override
    public String getRole() {
        return currentUser.getRole();
    }
}
