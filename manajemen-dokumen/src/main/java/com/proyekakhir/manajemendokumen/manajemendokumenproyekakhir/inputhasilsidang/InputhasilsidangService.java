package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.inputhasilsidang;

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
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class InputhasilsidangService extends BpmnManajemenService<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, ManajemenDokumenProyekAkhirRepository> {

    @Autowired
    DraftingService draftingService;

    protected InputhasilsidangService(ManajemenDokumenProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }
    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, DraftingVariable variable, ManajemenDokumenProyekAkhirDto dto, String remarks) {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            if (ObjectUtils.isEmpty(dto.getHasilSidang())){
                throw new BadRequestException("Silahkan input hasil sidang");
            }
            dto.setCatatanPengujiSidang(dto.getCatatan());
            List<Task> taskList = camundaProcessService.getTaskByProcessInstanceId(this.getProcessDefinitionKey(), proyekAkhir.getProcessInstanceId());
            if (taskList.stream().anyMatch(task -> task.getAssignee().equals(this.getCurrentUser()))) {
                variable = new DraftingVariable();
                variable.setHasilSidang(dto.getHasilSidang());
                draftingService.setVariable(proyekAkhir, variable);
                remarks = dto.getCatatan();
                return super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
            } else
                return false;
        } else {
            throw  new NotFoundException("Data tidak dapat ditemukan");
        }
    }

}
