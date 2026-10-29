package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.reviewtimkbk;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirService;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingVariable;
import com.proyekakhir.manajemendokumen.position.Position;
import com.proyekakhir.manajemendokumen.position.PositionRepository;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ReviewtimkbkService extends BpmnManajemenService<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, ManajemenDokumenProyekAkhirRepository> {

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    ManajemenDokumenProyekAkhirService proyekAkhirService;

    protected ReviewtimkbkService(ManajemenDokumenProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, DraftingVariable variable, ManajemenDokumenProyekAkhirDto dto, String remarks) {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            List<Position> positions = positionRepository.findAll();
            remarks = dto.getCatatan();
            dto.setCatatanKbk(dto.getCatatan());
            List<String> timKbk = positions.stream().filter(position -> position.getName().toLowerCase().contains("tim kbk")).collect(Collectors.toList()).stream().map(Position::getEmailUser).collect(Collectors.toList());
            if (timKbk.contains(this.getCurrentUser())){
                if (decision.equals("diterima")) {
                    proyekAkhir.setIsApproved(true);
                } else {
                    proyekAkhir.setIsApproved(false);
                }
                dto.setHasilReviewKbk(decision);
//                List<Task> taskList = camundaProcessService.getTaskByProcessInstanceId(this.getProcessDefinitionKey(), proyekAkhir.getProcessInstanceId());
                return super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
            } else {
                throw new BadRequestException("Tidak dapat melakukan approve, karena bukan tim kbk");
            }

        } else {
            throw  new NotFoundException("Data tidak dapat ditemukan");
        }
    }
}
