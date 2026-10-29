package com.proyekakhir.manajemendokumen.bimbinganproyekakhir.revisi;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirVariable;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.revisiproposal.RevisiproposalDto;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirVariable;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RevisiService extends BpmnManajemenService<BimbinganProyekAkhir, RevisiDto, BimbinganProyekAkhirVariable, BimbinganProyekAkhirRepository> {

    protected RevisiService(BimbinganProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, BimbinganProyekAkhirVariable variable, RevisiDto dto, String remarks) {
        Optional<BimbinganProyekAkhir> optional = this.get(id);
        if (optional.isPresent()){
            BimbinganProyekAkhir bimbinganProyekAkhir = optional.get();
            dto.setBimbinganKe(bimbinganProyekAkhir.getBimbinganKe()+1);
            if (variable == null){
                variable = new BimbinganProyekAkhirVariable();
                setReviewers(variable, bimbinganProyekAkhir);
            }
            return super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
        } else {
            throw new BadRequestException("Data tidak ditemukan!");
        }

    }

    private void setReviewers(BimbinganProyekAkhirVariable variable, BimbinganProyekAkhir bimbinganProyekAkhir) {
        List<String> reviewers = new ArrayList<>();
        if (Boolean.FALSE.equals(bimbinganProyekAkhir.getPembimbing1IsApproved())){
            reviewers.add(bimbinganProyekAkhir.getManajemenDokumenProyekAkhir().getPembimbing1().getEmail());
        }
        if (Boolean.FALSE.equals(bimbinganProyekAkhir.getPembimbing2IsApproved())){
            reviewers.add(bimbinganProyekAkhir.getManajemenDokumenProyekAkhir().getPembimbing2().getEmail());
        }
        variable.setReviewers(reviewers);
    }
}
