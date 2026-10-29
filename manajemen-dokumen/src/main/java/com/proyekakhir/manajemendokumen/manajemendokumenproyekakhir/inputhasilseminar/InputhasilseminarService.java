package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.inputhasilseminar;

import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirService;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingService;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingVariable;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class InputhasilseminarService extends BpmnManajemenService<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, ManajemenDokumenProyekAkhirRepository> {

    @Autowired
    BimbinganProyekAkhirRepository proyekAkhirRepository;

    @Autowired
    BimbinganProyekAkhirService proyekAkhirService;

    @Autowired
    DraftingService draftingService;

    protected InputhasilseminarService(ManajemenDokumenProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, DraftingVariable variable, ManajemenDokumenProyekAkhirDto dto, String remarks) {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            if (ObjectUtils.isEmpty(dto.getHasilSeminar())){
                throw new BadRequestException("Silahkan input hasil seminar");
            }
            dto.setCatatanPengujiSeminar(dto.getCatatan());
            List<Task> taskList = camundaProcessService.getTaskByProcessInstanceId(this.getProcessDefinitionKey(), proyekAkhir.getProcessInstanceId());
            if (taskList.stream().anyMatch(task -> task.getAssignee().equals(this.getCurrentUser()))) {
                variable = new DraftingVariable();
                variable.setHasilSeminar(dto.getHasilSeminar());
                draftingService.setVariable(proyekAkhir, variable);
                remarks = dto.getCatatan();
                boolean submit = super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
                if (submit && dto.getHasilSeminar().equals("lulus")){
                    createBimbinganLaporan(proyekAkhir, variable);
                }
                return submit;
            }else
                return false;
        } else {
            throw  new NotFoundException("Data tidak dapat ditemukan");
        }
    }

    public void createBimbinganLaporan(ManajemenDokumenProyekAkhir proyekAkhir, DraftingVariable variable) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(proyekAkhir.getProcessInstanceId())
                .singleResult();

        runtimeService.setVariables(processInstance.getId(), variable.asMap());

        ProcessInstance processInstanceChild = runtimeService.
                createProcessInstanceQuery().
                superProcessInstanceId(proyekAkhir.getProcessInstanceId())
                .singleResult();

        BimbinganProyekAkhir bimbinganProyekAkhir = new BimbinganProyekAkhir();
        bimbinganProyekAkhir.setManajemenDokumenProyekAkhir(proyekAkhir);
        bimbinganProyekAkhir.setProcessInstanceId(processInstanceChild.getProcessInstanceId());
        bimbinganProyekAkhir.setCreatedBy(this.getCurrentUser());
        bimbinganProyekAkhir.setCreatedDate(new Date());
        bimbinganProyekAkhir.setPembimbing1IsApproved(false);
        bimbinganProyekAkhir.setPembimbing2IsApproved(false);
        proyekAkhirRepository.save(bimbinganProyekAkhir);
        List<Task> afterTasks = this.camundaProcessService.getTaskByProcessInstanceId(
                proyekAkhirService.getProcessDefinitionKey(),
                processInstanceChild.getProcessInstanceId());

        activityLogService.addLog(
                proyekAkhirService.getGenericClassName(0),
                bimbinganProyekAkhir.getId(), currentUser.getUserId(), null,
                "START",
                afterTasks!=null && !afterTasks.isEmpty() ? afterTasks.get(0).getTaskDefinitionKey(): null);

    }
}
