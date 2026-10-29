package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.reviewproposal;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirService;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ReviewproposalService extends BpmnManajemenService<BimbinganProposalProyekAkhir, ReviewproposalDto, ReviewproposalVariable, BimbinganProposalProyekAkhirRepository> {

    @Autowired
    ManajemenDokumenProyekAkhirRepository proyekAkhirRepository;

    protected ReviewproposalService(BimbinganProposalProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, ReviewproposalVariable variable, ReviewproposalDto dto, String remarks) {
        Optional<BimbinganProposalProyekAkhir> bimbinganProposalProyekAkhir = this.get(id);
        if (bimbinganProposalProyekAkhir.isPresent()){
            BimbinganProposalProyekAkhir proyekAkhir = bimbinganProposalProyekAkhir.get();
            List<Task> taskList = camundaProcessService.getTaskByProcessInstanceId(this.getProcessDefinitionKey(), proyekAkhir.getProcessInstanceId());
            if (taskList.stream().anyMatch(task -> task.getAssignee().equals(this.getCurrentUser()))) {
                if (proyekAkhir.getManajemenDokumenProyekAkhir().getPembimbing1().getEmail().equals(this.getCurrentUser())){
                    proyekAkhir.setCatatanP1(dto.getCatatan());
                    if (decision.equals("approved")){
                        proyekAkhir.setPembimbing1IsApproved(true);
                    }
                } else {
                    proyekAkhir.setCatatanP2(dto.getCatatan());
                    if (decision.equals("approved")){
                        proyekAkhir.setPembimbing2IsApproved(true);
                    }
                }
                remarks = dto.getCatatan();
                setProposalIsApproved(proyekAkhir);
                return super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
            } else {
                return false;
            }
        } else {
            throw new BadRequestException("Data tidak ditemukan!");
        }

    }

    private void setProposalIsApproved(BimbinganProposalProyekAkhir proyekAkhir) {
        if (proyekAkhir.getPembimbing1IsApproved() && proyekAkhir.getPembimbing2IsApproved()){
            proyekAkhir.getManajemenDokumenProyekAkhir().setProposalIsApproved(true);
            proyekAkhirRepository.save(proyekAkhir.getManajemenDokumenProyekAkhir());
        }
    }

}
