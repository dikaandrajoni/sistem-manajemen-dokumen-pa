package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.revisiproposal;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirVariable;
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
public class RevisiproposalService extends BpmnManajemenService<BimbinganProposalProyekAkhir, RevisiproposalDto, BimbinganProposalProyekAkhirVariable, BimbinganProposalProyekAkhirRepository> {

    protected RevisiproposalService(BimbinganProposalProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, BimbinganProposalProyekAkhirVariable variable, RevisiproposalDto dto, String remarks) {
        Optional<BimbinganProposalProyekAkhir> optional = this.get(id);
        if (optional.isPresent()){
            BimbinganProposalProyekAkhir bimbinganProposalProyekAkhir = optional.get();
            dto.setBimbinganKe(bimbinganProposalProyekAkhir.getBimbinganKe()+1);
            if (variable == null){
                variable = new BimbinganProposalProyekAkhirVariable();
                setReviewers(variable, bimbinganProposalProyekAkhir);
            }
            return super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
        } else {
            throw new BadRequestException("Data tidak ditemukan!");
        }

    }

    private void setReviewers(BimbinganProposalProyekAkhirVariable variable, BimbinganProposalProyekAkhir bimbinganProposalProyekAkhir) {
        List<String> reviewers = new ArrayList<>();
        if (Boolean.FALSE.equals(bimbinganProposalProyekAkhir.getPembimbing1IsApproved())){
            reviewers.add(bimbinganProposalProyekAkhir.getManajemenDokumenProyekAkhir().getPembimbing1().getEmail());
        }
        if (Boolean.FALSE.equals(bimbinganProposalProyekAkhir.getPembimbing2IsApproved())){
            reviewers.add(bimbinganProposalProyekAkhir.getManajemenDokumenProyekAkhir().getPembimbing2().getEmail());
        }
        variable.setReviewers(reviewers);
    }
}
