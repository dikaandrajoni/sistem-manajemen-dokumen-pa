package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitmbkmkaprodi;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.inputhasilseminar.InputhasilseminarService;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingVariable;
import com.proyekakhir.manajemendokumen.position.Position;
import com.proyekakhir.manajemendokumen.position.PositionRepository;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiRepository;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import com.proyekakhir.manajemendokumen.user.User;
import com.proyekakhir.manajemendokumen.user.UserRepository;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class SubmitMbkmKaprodiService extends BpmnManajemenService<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, ManajemenDokumenProyekAkhirRepository> {

    @Autowired
    InputhasilseminarService inputhasilseminarService;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    UserRepository userRepository;

    protected SubmitMbkmKaprodiService(ManajemenDokumenProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    @Transactional
    public boolean submit(String taskDefinitionKey, UUID id, String decision, DraftingVariable variable, ManajemenDokumenProyekAkhirDto dto, String remarks) {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            if (variable == null){
                variable = new DraftingVariable();
            }
            List<Task> taskList = camundaProcessService.getTaskByProcessInstanceId(this.getProcessDefinitionKey(), proyekAkhir.getProcessInstanceId());
            if (taskList.stream().anyMatch(task -> task.getAssignee().equals(this.getCurrentUser()))) {
                boolean isSubmit = super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
                inputhasilseminarService.createBimbinganLaporan(proyekAkhir, variable);
                return isSubmit;
            }
            else
                return false;

        } else {
            throw new NotFoundException("Data tidak dapat ditemukan");
        }
    }

}
