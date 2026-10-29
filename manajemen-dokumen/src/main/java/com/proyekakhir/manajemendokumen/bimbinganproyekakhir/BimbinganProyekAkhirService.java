package com.proyekakhir.manajemendokumen.bimbinganproyekakhir;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.core.service.email.EmailService;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BimbinganProyekAkhirService extends BpmnManajemenService<BimbinganProyekAkhir, BimbinganProyekAkhirDto, BimbinganProyekAkhirVariable, BimbinganProyekAkhirRepository> {

    @Autowired
    EmailService emailService;

    protected BimbinganProyekAkhirService(BimbinganProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    public List<String> getBimbinganLaporanProcess(){
        List<String> processInstanceId = this.getCamundaProcessService().getTaskByAssignee(this.getProcessDefinitionKey(), this.getCurrentUser())
                .stream().map(Task::getProcessInstanceId).collect(Collectors.toList());
        return processInstanceId;
    }
}
