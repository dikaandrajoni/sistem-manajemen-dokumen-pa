package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.revisijudul;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingVariable;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class RevisijudulService extends BpmnManajemenService<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, ManajemenDokumenProyekAkhirRepository> {

    protected RevisijudulService(ManajemenDokumenProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, DraftingVariable variable, ManajemenDokumenProyekAkhirDto dto, String remarks) {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            cleanUpData(proyekAkhir, dto);
            this.jpaRepository.save(proyekAkhir);
            List<Task> taskList = camundaProcessService.getTaskByProcessInstanceId(this.getProcessDefinitionKey(), proyekAkhir.getProcessInstanceId());
            if (taskList.stream().anyMatch(task -> task.getAssignee().equals(this.getCurrentUser())))
                return super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
            else
                return false;
        } else {
            throw  new NotFoundException("Data tidak dapat ditemukan");
        }
    }

    private void cleanUpData(ManajemenDokumenProyekAkhir proyekAkhir, ManajemenDokumenProyekAkhirDto dto) {
        dto.copyFromDto(proyekAkhir);
        proyekAkhir.setUpdatedDate(new Date());
        proyekAkhir.setUpdatedBy(this.getCurrentUser());
    }

    @Override
    public ManajemenDokumenProyekAkhir draft(ManajemenDokumenProyekAkhir entity, ManajemenDokumenProyekAkhirDto dto) throws AlurKerjaException {
        return super.draft(entity, dto);
    }
}
