package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.aturjadwalsidang;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.core.service.email.EmailService;
import com.proyekakhir.manajemendokumen.dosen.DosenRepository;
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

import javax.ws.rs.NotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class AturjadwalsidangService extends BpmnManajemenService<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, ManajemenDokumenProyekAkhirRepository> {

    @Autowired
    EmailService emailService;

    @Autowired
    DraftingService draftingService;

    @Autowired
    DosenRepository dosenRepository;

    protected AturjadwalsidangService(ManajemenDokumenProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    protected void sendSubmitNotification(String taskDefinitionKey, String decision, ManajemenDokumenProyekAkhir entity, DraftingVariable variable, ManajemenDokumenProyekAkhirDto dto) {
        Map<String, Object> content = new HashMap<>();
        content.put("nama", entity.getRequestorName());
        content.put("header", "Sidang Proyek Akhir");
        content.put("bp", entity.getRequestorCode());
        content.put("judul", entity.getJudulProyekAkhir());
        content.put("pembimbing1", entity.getPembimbing1().getName());
        content.put("pembimbing2", entity.getPembimbing2().getName());
        content.put("penguji", entity.getPenguji().getName());
        content.put("tempat", entity.getLokasiSidang());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String jadwal = formatter.format(dto.getJadwalSidangProyekAkhir());
        content.put("jadwal", jadwal);

        emailService.sendWithHtml("info@gmail.com", entity.getRequestorEmail(), "Notifikasi Jadwal Sidang Proyek Akhir", "Jadwal.html", content);

    }

    @Override
    public boolean submit(String taskDefinitionKey, UUID id, String decision, DraftingVariable variable, ManajemenDokumenProyekAkhirDto dto, String remarks) {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            if (variable == null)
                variable = new DraftingVariable();
            if (dto.getPenguji() == null || dto.getPenguji().getId() == null){
                throw new BadRequestException("Silahkan atur penguji terlebih dahulu");
            }
            List<Task> taskList = camundaProcessService.getTaskByProcessInstanceId(this.getProcessDefinitionKey(), proyekAkhir.getProcessInstanceId());
            if (taskList.stream().anyMatch(task -> task.getAssignee().equals(this.getCurrentUser()))) {
                if (dto.getPenguji() != null && dto.getPenguji().getId() != null){
                    proyekAkhir.setPenguji(dosenRepository.getById(dto.getPenguji().getId()));
                    this.jpaRepository.save(proyekAkhir);
                }
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
