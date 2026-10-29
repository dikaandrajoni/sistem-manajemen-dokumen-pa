package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir;

import com.proyekakhir.manajemendokumen.core.controller.BpmnController;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.services.StorageService;
import io.minio.errors.*;
import lombok.SneakyThrows;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.task.Task;
import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir")
public class ManajemenDokumenProyekAkhirController extends BpmnController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, ManajemenDokumenProyekAkhirVariable, ManajemenDokumenProyekAkhirService, ManajemenDokumenProyekAkhirRepository> {

    @Autowired
    StorageService storageService;

    @Autowired
    ManajemenDokumenProyekAkhirService manajemenDokumenProyekAkhirService;

    public ManajemenDokumenProyekAkhirController(ManajemenDokumenProyekAkhirService manajemendokumenproyekakhirService) {
        super(manajemendokumenproyekakhirService);
    }

    @Override
    @SneakyThrows
    public ResponseEntity<Object> start(@RequestParam(required = false, defaultValue = "false") boolean withVariable, ManajemenDokumenProyekAkhirVariable variable, @Valid @RequestBody ManajemenDokumenProyekAkhirDto dto) {
        if (withVariable) {
            return success(this.bpmnService.start(variable, dto));
        } else {
            return success(this.bpmnService.start(null, dto));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id){
        Optional<ManajemenDokumenProyekAkhir> manajemenDokumenProyekAkhir = this.bpmnService.get(id);
        if (manajemenDokumenProyekAkhir.isPresent()){
            ManajemenDokumenProyekAkhir dokumenProyekAkhir = manajemenDokumenProyekAkhir.get();
            dokumenProyekAkhir.setDeleteBy(this.bpmnService.getCurrentUser());
            dokumenProyekAkhir.setDeleteDate(new Date());
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            try {
                runtimeService.deleteProcessInstance(dokumenProyekAkhir.getProcessInstanceId(), dokumenProyekAkhir.getId().toString());
            } catch (Exception e){
                e.printStackTrace();
            }
            this.bpmnService.update(dokumenProyekAkhir);
        } else {
            throw new NotFoundException("Data "+id+" tidak ditemukan");
        }
        return success("Data "+id+" berhasil dihapus");
    }

    @PostMapping("/{id}/uploadProposal")
    public ResponseEntity<Object> uploadProposal(@PathVariable UUID id, @RequestParam MultipartFile file) throws IOException {
        return success(manajemenDokumenProyekAkhirService.uploadProposal(id, file));
    }

    @GetMapping("/{id}/downloadProposal")
    public ResponseEntity<Resource> downloadProposal(@PathVariable UUID id){
        Optional<ManajemenDokumenProyekAkhir> manajemenDokumenProyekAkhir = this.bpmnService.get(id);
        if (manajemenDokumenProyekAkhir.isPresent()){
            ManajemenDokumenProyekAkhir dokumenProyekAkhir = manajemenDokumenProyekAkhir.get();
            ByteArrayResource resource = storageService.download(dokumenProyekAkhir.getProposalMinioPath());

            HttpHeaders header = new HttpHeaders();
            String name = dokumenProyekAkhir.getProposalFileName().replaceAll(" ", "_");

            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+"Proposal_Proyek_Akhir-"+dokumenProyekAkhir.getRequestorCode()+".pdf");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");
            header.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Authorization,Content-Type, Content-Range, Content-Disposition, Content-Description,Origin, X-Requested-With, sessionId");

            return ResponseEntity.ok()
                    .headers(header)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            throw new NotFoundException("Data "+id+" tidak ditemukan");
        }
    }

    @PostMapping("/{id}/uploadLaporan")
    public ResponseEntity<Object> uploadLaporan(@PathVariable UUID id, @RequestParam MultipartFile file) throws IOException {
        return success(manajemenDokumenProyekAkhirService.uploadLaporan(id, file));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Object> dashboard(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") int limit,
                                              @RequestParam(required = false) String sort,
                                              @RequestParam(required = false, defaultValue = "true") boolean asc,
                                              ManajemenDokumenProyekAkhirDto dto) {
        Pageable pageable = pageFromRequest(page, limit, sort, asc);
        return success(manajemenDokumenProyekAkhirService.dashboard(dto, pageable));
    }

    @GetMapping("/dashboard-count")
    public ResponseEntity<Object> dashboardCount() {
        return success(manajemenDokumenProyekAkhirService.dashboardCount());
    }

    @GetMapping("/todo")
    public ResponseEntity<Object> todo(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") int limit,
                                              @RequestParam(required = false) String sort,
                                              @RequestParam(required = false, defaultValue = "true") boolean asc,
                                              ManajemenDokumenProyekAkhirDto dto) {
        Pageable pageable = pageFromRequest(page, limit, sort, asc);
        List<String> processInstanceId = this.bpmnService.getCamundaProcessService().getTaskByAssignee(this.bpmnService.getProcessDefinitionKey(), this.bpmnService.getCurrentUser())
                .stream().map(Task::getProcessInstanceId).collect(Collectors.toList());
        return success(this.manajemenDokumenProyekAkhirService.todo(processInstanceId, dto, pageable));
    }

    @GetMapping("/{id}/getLinkProposal")
    public ResponseEntity<Object> getLinkProposal(@PathVariable UUID id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Optional<ManajemenDokumenProyekAkhir> manajemenDokumenProyekAkhir = this.bpmnService.get(id);
        if (manajemenDokumenProyekAkhir.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = manajemenDokumenProyekAkhir.get();
            if (proyekAkhir.getProposalMinioPath() != null){
                return success(storageService.getDownloadUrl(proyekAkhir.getProposalMinioPath()));
            } else {
                throw new BadRequestException("File tidak ada");
            }
        } else {
            throw new NotFoundException("Data tidak ditemukan");
        }
    }

    @GetMapping("/{id}/getLinkLaporan")
    public ResponseEntity<Object> getLinkLaporan(@PathVariable UUID id) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Optional<ManajemenDokumenProyekAkhir> manajemenDokumenProyekAkhir = this.bpmnService.get(id);
        if (manajemenDokumenProyekAkhir.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = manajemenDokumenProyekAkhir.get();
            if (proyekAkhir.getLaporanMinioPath() != null){
                return success(storageService.getDownloadUrl(proyekAkhir.getLaporanMinioPath()));
            } else {
                throw new BadRequestException("File tidak ada");
            }
        } else {
            throw new NotFoundException("Data tidak ditemukan");
        }
    }

    @Override
    public ResponseEntity<Object> listProcess(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") int limit,
                                              @RequestParam(required = false) String sort,
                                              @RequestParam(required = false, defaultValue = "true") boolean asc,
                                              @RequestParam(required = false) String taskDefinitionKey,
                                              ManajemenDokumenProyekAkhirDto dto) {
        if (!this.bpmnService.getRole().equals("admin")){
            dto.setRequestorEmail(this.bpmnService.getCurrentUser());
        }
       return super.listProcess(page, limit, sort, asc, taskDefinitionKey, dto);
    }

    @GetMapping("/hasilReviewKaprodi")
    public ResponseEntity<Object> hasilReviewKaprodi(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") int limit,
                                              @RequestParam(required = false) String sort,
                                              @RequestParam(required = false, defaultValue = "true") boolean asc,
                                              ManajemenDokumenProyekAkhirDto dto) {
        Pageable pageable = pageFromRequest(page, limit, sort, asc);
        Page<ManajemenDokumenProyekAkhirDto> dtoPage = this.manajemenDokumenProyekAkhirService.dashboard(dto, pageable);
        dtoPage.getContent().stream().filter(data -> data.getHasilReviewKaprodi() != null);
        return success(dtoPage);
    }

    @Override
    public ResponseEntity<Object> statistic(String[] businessKeys) {
        if (!this.bpmnService.getRole().equals("admin") && manajemenDokumenProyekAkhirService.getBussinessKey() != null){
            businessKeys = manajemenDokumenProyekAkhirService.getBussinessKey();
        }
        return super.statistic(businessKeys);
    }
}
