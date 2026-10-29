package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.core.entity.BpmnEntity;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.core.service.email.EmailService;
import com.proyekakhir.manajemendokumen.core.util.HttpUtil;
import com.proyekakhir.manajemendokumen.core.util.JsonUtil;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class BimbinganProposalProyekAkhirService extends BpmnManajemenService<BimbinganProposalProyekAkhir, BimbinganProposalProyekAkhirDto, BimbinganProposalProyekAkhirVariable, BimbinganProposalProyekAkhirRepository> {

    @Autowired
    EmailService emailService;

    @Autowired
    BimbinganProposalProyekAkhirRepository proposalProyekAkhirRepository;

    protected BimbinganProposalProyekAkhirService(BimbinganProposalProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    public List getStats(String baseUrl, String processDefKey) {
        try {
            byte[] response = HttpUtil.get(baseUrl + "/engine-rest/process-definition/key/"+processDefKey+"/statistics" , null);
            return JsonUtil.deserialize(new String(response),List.class);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).info(e.getMessage());
            return  null;
        }
    }

    public List<String> getBimbinganProposalProcess(){
        List<String> processInstanceId = this.getCamundaProcessService().getTaskByAssignee(this.getProcessDefinitionKey(), this.getCurrentUser())
                .stream().map(Task::getProcessInstanceId).collect(Collectors.toList());
        return processInstanceId;
    }

}
