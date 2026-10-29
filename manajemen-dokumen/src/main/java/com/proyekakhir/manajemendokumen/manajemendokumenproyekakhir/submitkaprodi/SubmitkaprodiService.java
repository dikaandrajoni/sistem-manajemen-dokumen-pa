package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitkaprodi;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirService;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirService;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingService;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingVariable;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.inputhasilseminar.InputhasilseminarService;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class SubmitkaprodiService extends BpmnManajemenService<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, ManajemenDokumenProyekAkhirRepository> {

    @Autowired
    DraftingService draftingService;

    @Autowired
    ManajemenDokumenProyekAkhirService proyekAkhirService;

    @Autowired
    InputhasilseminarService inputhasilseminarService;

    @Autowired
    BimbinganProposalProyekAkhirRepository bimbinganProposalProyekAkhirRepository;

    @Autowired
    BimbinganProposalProyekAkhirService bimbinganProposalProyekAkhirService;

    protected SubmitkaprodiService(ManajemenDokumenProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, DraftingVariable variable, ManajemenDokumenProyekAkhirDto dto, String remarks) {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            if (variable == null)
                variable = new DraftingVariable();

            draftingService.setVariable(proyekAkhir, variable);
            dto.setHasilReviewKaprodi(decision);
            remarks = dto.getCatatan();
            dto.setCatatanKaprodi(dto.getCatatan());
            List<Task> taskList = camundaProcessService.getTaskByProcessInstanceId(this.getProcessDefinitionKey(), proyekAkhir.getProcessInstanceId());
            if (taskList.stream().anyMatch(task -> task.getAssignee().equals(this.getCurrentUser()))) {
                boolean isSubmit = super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
                if (Boolean.TRUE.equals(proyekAkhir.getIsApproved())){
                    proyekAkhir = proyekAkhirService.setPembimbing(proyekAkhir, dto);
                    createSubProcess(proyekAkhir, variable);
                }
                return isSubmit;
            }
            else
                return false;
        } else {
            throw  new NotFoundException("Data tidak dapat ditemukan");
        }
    }

    private void createSubProcess(ManajemenDokumenProyekAkhir proyekAkhir, DraftingVariable variable) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(proyekAkhir.getProcessInstanceId())
                .singleResult();

        if (proyekAkhir.getProgramStudi().getDiploma().equals("D4")){
            runtimeService.setVariables(processInstance.getId(), variable.asMap());

            ProcessInstance processInstanceChild = runtimeService.
                    createProcessInstanceQuery().
                    superProcessInstanceId(proyekAkhir.getProcessInstanceId())
                    .singleResult();

            BimbinganProposalProyekAkhir bimbinganProposalProyekAkhir = new BimbinganProposalProyekAkhir();
            bimbinganProposalProyekAkhir.setManajemenDokumenProyekAkhir(proyekAkhir);
            bimbinganProposalProyekAkhir.setProcessInstanceId(processInstanceChild.getProcessInstanceId());
            bimbinganProposalProyekAkhir.setCreatedBy(this.getCurrentUser());
            bimbinganProposalProyekAkhir.setCreatedDate(new Date());
            bimbinganProposalProyekAkhir.setPembimbing1IsApproved(false);
            bimbinganProposalProyekAkhir.setPembimbing2IsApproved(false);
            bimbinganProposalProyekAkhirRepository.save(bimbinganProposalProyekAkhir);
            List<Task> afterTasks = this.camundaProcessService.getTaskByProcessInstanceId(
                    bimbinganProposalProyekAkhirService.getProcessDefinitionKey(),
                    processInstanceChild.getProcessInstanceId());

            activityLogService.addLog(
                    bimbinganProposalProyekAkhirService.getGenericClassName(0),
                    bimbinganProposalProyekAkhir.getId(), currentUser.getUserId(), null,
                    "START",
                    afterTasks!=null && !afterTasks.isEmpty() ? afterTasks.get(0).getTaskDefinitionKey(): null);
        } else {
            inputhasilseminarService.createBimbinganLaporan(proyekAkhir, variable);
        }
    }
}

