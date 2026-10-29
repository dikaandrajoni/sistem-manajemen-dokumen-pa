package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.submitproposal;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirVariable;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingService;
import com.proyekakhir.manajemendokumen.position.Position;
import com.proyekakhir.manajemendokumen.position.PositionRepository;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import com.proyekakhir.manajemendokumen.user.User;
import com.proyekakhir.manajemendokumen.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class SubmitproposalService extends BpmnManajemenService<BimbinganProposalProyekAkhir, SubmitproposalDto, BimbinganProposalProyekAkhirVariable, BimbinganProposalProyekAkhirRepository> {

    @Autowired
    ManajemenDokumenProyekAkhirRepository proyekAkhirRepository;

    @Autowired
    DraftingService draftingService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PositionRepository positionRepository;

    protected SubmitproposalService(BimbinganProposalProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, BimbinganProposalProyekAkhirVariable variable, SubmitproposalDto dto, String remarks) {
        Optional<BimbinganProposalProyekAkhir> bimbinganProposalProyekAkhir = this.get(id);
        if (bimbinganProposalProyekAkhir.isPresent()){
            BimbinganProposalProyekAkhir bimbingan = bimbinganProposalProyekAkhir.get();
            dto.setBimbinganKe(1);
            if (variable == null)
                variable = new BimbinganProposalProyekAkhirVariable();
            this.setVariable(bimbingan.getManajemenDokumenProyekAkhir(), variable);
            return super.submit(taskDefinitionKey, id, decision, variable, dto, remarks);
        } else {
            throw new BadRequestException("Data tidak ditemukan!");
        }
    }

    public void setVariable(ManajemenDokumenProyekAkhir proyekAkhir, BimbinganProposalProyekAkhirVariable variable) {
        variable.setDiploma(proyekAkhir.getProgramStudi().getDiploma());
        variable.setRequestor(proyekAkhir.getRequestorEmail());
        variable.setKaprodi(proyekAkhir.getProgramStudi().getKaprodiEmail());
        List<User> koordinatorPA = userRepository.findByRole("Koordinator Proyek Akhir");
        if (!koordinatorPA.isEmpty()){
            variable.setKoordinatorPA(koordinatorPA.get(0).getEmail());
        } else {
            Position position = positionRepository.findByNameEquals("Koordinator Proyek Akhir");
            if (position != null){
                variable.setKoordinatorPA(position.getEmailUser());
            } else {
                throw new BadRequestException("Data Koordinator Proyek Akhir Masih Kosong!");
            }
        }
        if (proyekAkhir.getIsApproved() != null){
            variable.setIsApproved(proyekAkhir.getIsApproved());
        }
        List<User> timKBK = userRepository.findByRole("Tim KBK");
        if (timKBK != null && !timKBK.isEmpty()){
            List<String> email = timKBK.stream().map(User::getEmail).collect(Collectors.toList());
            variable.setTimKBK(email);
        } else {
            List<Position> positions = positionRepository.findAll();
            positions = positions.stream().filter(position -> position.getName().toLowerCase().contains("tim kbk")).collect(Collectors.toList());
            if (!positions.isEmpty()){
                variable.setTimKBK(positions.stream().map(Position::getEmailUser).collect(Collectors.toList()));
            } else {
                throw new BadRequestException("Data tim kbk masih kosong!");
            }
        }
        List<String> reviewers = new ArrayList<>();
        if (proyekAkhir.getPembimbing1() != null){
            reviewers.add(proyekAkhir.getPembimbing1().getEmail());
        }
        if (proyekAkhir.getPembimbing2() != null){
            reviewers.add(proyekAkhir.getPembimbing2().getEmail());
        }
        if (proyekAkhir.getPenguji() != null){
            variable.setPenguji(proyekAkhir.getPenguji().getEmail());
        }

        if (!CollectionUtils.isEmpty(reviewers)){
            variable.setReviewers(reviewers);
        }
    }
}
