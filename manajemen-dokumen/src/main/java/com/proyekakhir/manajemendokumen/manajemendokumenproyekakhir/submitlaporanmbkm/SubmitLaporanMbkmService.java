package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitlaporanmbkm;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.position.Position;
import com.proyekakhir.manajemendokumen.position.PositionRepository;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiRepository;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import com.proyekakhir.manajemendokumen.user.User;
import com.proyekakhir.manajemendokumen.user.UserRepository;
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
public class SubmitLaporanMbkmService extends BpmnManajemenService<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, SubmitLaporanMbkmVariable, ManajemenDokumenProyekAkhirRepository> {

    @Autowired
    private ProgramStudiRepository programStudiRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    UserRepository userRepository;

    protected SubmitLaporanMbkmService(ManajemenDokumenProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    @Transactional
    public boolean submit(String taskDefinitionKey, UUID id, String decision, SubmitLaporanMbkmVariable variable, ManajemenDokumenProyekAkhirDto dto, String remarks) {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            cleanData(proyekAkhir, dto);
            if (variable == null){
                variable = new SubmitLaporanMbkmVariable();
            }
            setVariable(proyekAkhir, variable);
            variable.setDecision("Submitted");
            return super.submit(taskDefinitionKey, id, variable.getDecision(), variable, dto, remarks);
        } else {
            throw new NotFoundException("Data tidak dapat ditemukan");
        }
    }

    public void setVariable(ManajemenDokumenProyekAkhir proyekAkhir, SubmitLaporanMbkmVariable variable) {
        variable.setIsMbkm(proyekAkhir.getIsMbkm());
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
            variable.setTimKbk(email);
        } else {
            List<Position> positions = positionRepository.findAll();
            positions = positions.stream().filter(position -> position.getName().toLowerCase().contains("tim kbk")).collect(Collectors.toList());
            if (!positions.isEmpty()){
                variable.setTimKbk(positions.stream().map(Position::getEmailUser).collect(Collectors.toList()));
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

    @Override
    public ManajemenDokumenProyekAkhir draft(ManajemenDokumenProyekAkhir entity, ManajemenDokumenProyekAkhirDto dto) throws AlurKerjaException {
        cleanData(entity, dto);
        return super.draft(entity, dto);
    }

    @Transactional
    public void cleanData(ManajemenDokumenProyekAkhir entity, ManajemenDokumenProyekAkhirDto dto) {
        ManajemenDokumenProyekAkhir temp = dto.fromDto();
        if (temp.getProgramStudi() != null){
            ProgramStudi programStudi = programStudiRepository.getById(temp.getProgramStudi().getId());
            if (programStudi != null){
                entity.setProgramStudi(programStudi);
            } else {
                throw new NotFoundException("Program Studi tidak dapat ditemukan");
            }
        }
    }
}
