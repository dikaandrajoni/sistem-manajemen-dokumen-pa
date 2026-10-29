package com.proyekakhir.manajemendokumen.services;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.BpmnService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import org.springframework.beans.factory.annotation.Autowired;

public class BpmnManajemenService<E, D, V, R> extends BpmnService<E, D, V, R> {
    @Autowired
    protected CurrentUser currentUser;

    protected BpmnManajemenService(R simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService);
        this.currentUser = currentUser;
    }

    @Override
    protected void sendStartNotification(E entity, V variable, D dto) {

    }

    @Override
    protected void sendSubmitNotification(String taskDefinitionKey, String decision, E entity, V variable, D dto) {

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
