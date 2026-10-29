package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirService;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirService;
import com.proyekakhir.manajemendokumen.constant.ApplicationEnum;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.entity.BpmnEntity;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.core.service.ActivityLogService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import com.proyekakhir.manajemendokumen.core.service.email.EmailService;
import com.proyekakhir.manajemendokumen.dosen.Dosen;
import com.proyekakhir.manajemendokumen.dosen.DosenRepository;
import com.proyekakhir.manajemendokumen.filedocument.FileDocument;
import com.proyekakhir.manajemendokumen.filedocument.FileDocumentRepository;
import com.proyekakhir.manajemendokumen.position.Position;
import com.proyekakhir.manajemendokumen.position.PositionRepository;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiRepository;
import com.proyekakhir.manajemendokumen.services.BpmnManajemenService;
import com.proyekakhir.manajemendokumen.services.StorageService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.BadUserRequestException;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ManajemenDokumenProyekAkhirService extends BpmnManajemenService<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, ManajemenDokumenProyekAkhirVariable, ManajemenDokumenProyekAkhirRepository> {

    @Autowired
    EmailService emailService;

    @Autowired
    StorageService storageService;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    FileDocumentRepository fileDocumentRepository;

    @Autowired
    DosenRepository dosenRepository;

    @Autowired
    ProgramStudiRepository programStudiRepository;

    @Autowired
    BimbinganProposalProyekAkhirService bimbinganProposalProyekAkhirService;

    @Autowired
    BimbinganProposalProyekAkhirRepository proposalProyekAkhirRepository;

    @Autowired
    BimbinganProyekAkhirService bimbinganProyekAkhirService;

    @Autowired
    BimbinganProyekAkhirRepository laporanProyekAkhirRepository;

    @Autowired
    ManajemenDokumenProyekAkhirRepository proyekAkhirRepository;


    protected ManajemenDokumenProyekAkhirService(ManajemenDokumenProyekAkhirRepository simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService, CurrentUser currentUser) {
        super(simpleJpaRepository, camundaProcessService, activityLogService, currentUser);
    }

    @Override
    public Map<String, Object> start(ManajemenDokumenProyekAkhirVariable variable, ManajemenDokumenProyekAkhirDto dto, String remarks) throws AlurKerjaException {
        variable.setRequestor(this.getCurrentUser());
        variable.setIsMbkm(dto.getIsMbkm());
        return super.start(variable, dto, remarks);
    }

    @Override
    public ManajemenDokumenProyekAkhir createFromDto(ManajemenDokumenProyekAkhirDto dto) {
        List<ManajemenDokumenProyekAkhir> manajemenDokumenProyekAkhirs = proyekAkhirRepository.findAllByRequestorEmail(this.getCurrentUser());
//        if (manajemenDokumenProyekAkhirs.size() == 3){
//            throw new BadUserRequestException("Maksimal pengajuan proyek akhir hanya boleh 3 kali");
//        }
        ProgramStudi prodi = null;
        if (dto.getProgramStudi() != null) {
            if (dto.getProgramStudi().getId() != null) {
                Optional<ProgramStudi> prodiOptional = programStudiRepository.findById(dto.getProgramStudi().getId());
                if (prodiOptional.isPresent()) {
                    prodi = prodiOptional.get();
                } else {
                    throw new BadUserRequestException("Program studi tidak dapat ditemukan");
                }
            } else {
                throw new BadUserRequestException("Program studi must be not empty");
            }
        }
        if (ObjectUtils.isEmpty(dto.getJudulProyekAkhir())){
            throw new BadUserRequestException("Judul proyek akhir tidak boleh kosong");
        }

        if (ObjectUtils.isEmpty(dto.getDeskripsi())){
            throw new BadUserRequestException("Deskripsi tidak boleh kosong");
        }

        if (ObjectUtils.isEmpty(dto.getBidangTopikPA())){
            throw new BadUserRequestException("Topik proyek akhir tidak boleh kosong");
        }

        Dosen pembimbing1 = null;
        if (dto.getPembimbing1() != null && dto.getPembimbing1().getId() != null) {
            Dosen temp = dosenRepository.getById(dto.getPembimbing1().getId());
            if (temp != null){
                pembimbing1 = temp;
            } else {
                throw new BadUserRequestException("Data dosen tidak dapat ditemukan");
            }
        } else {
            throw new BadUserRequestException("Silahkan pilih rekomendasi dosen pembimbing");
        }

        ManajemenDokumenProyekAkhir proyekAkhir = dto.fromDto();
        proyekAkhir.setProgramStudi(prodi);
        proyekAkhir.setPembimbing1(pembimbing1);
        proyekAkhir.setCreatedBy(this.getCurrentUser());
        proyekAkhir.setCreatedDate(new Date());
        return this.jpaRepository.save(proyekAkhir);
    }

    @Override
    public Specification<ManajemenDokumenProyekAkhir> instantiateSpecification(ManajemenDokumenProyekAkhirDto dto) {
        return new ManajemenDokumenSpecifications(dto);
    }

    @Override
    public Specification<ManajemenDokumenProyekAkhir> instantiateSpecification(List<String> processInstanceIds, ManajemenDokumenProyekAkhirDto dto) {
        return new ManajemenDokumenSpecifications(processInstanceIds, dto);
    }

    public ManajemenDokumenProyekAkhir uploadProposal(UUID id, MultipartFile file) throws IOException {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            StorageDto storageDto = null;
            File fo = new File(Calendar.getInstance().getTimeInMillis()+"");
            try (FileOutputStream f = new FileOutputStream(fo)){
                WordprocessingMLPackage template = WordprocessingMLPackage.load(file.getInputStream());
                Docx4J.toPDF(template, f);
                f.flush();
                f.close();

                ByteArrayResource newResource = new ByteArrayResource(Files.toByteArray(fo));
                storageDto = storageService.upload(newResource.getByteArray(), ApplicationEnum.minioFolder.DOCUMENT.name(), ".pdf");

            } catch (Exception ex) {
                try {
                    storageDto = storageService.upload(file.getBytes(), ApplicationEnum.minioFolder.DOCUMENT.name(), ".pdf");
                } catch (IOException e) {
                    Logger.getLogger(getClass().getName()).info(e.getMessage());
                }
            } finally {
                fo.delete();
            }

            if (storageDto != null){
                proyekAkhir.setProposalFileName(file.getOriginalFilename());
                proyekAkhir.setProposalMinioId(storageDto.getIdentifier());
                proyekAkhir.setProposalMinioPath(storageDto.getFilePath());
                this.update(proyekAkhir);

                FileDocument fileDocument = new FileDocument();
                fileDocument.setManajemenDokumenProyekAkhir(proyekAkhir);
                fileDocument.setExtension(".pdf");
                fileDocument.setFileId(storageDto.getIdentifier());
                fileDocument.setFilePath(storageDto.getFilePath());
                fileDocument.setCreatedDate(new Date());
                fileDocumentRepository.save(fileDocument);
            }
            return proyekAkhir;
        } else {
            throw new NotFoundException("Data dengan id "+id+" tidak ditemukan");
        }
    }

    public ManajemenDokumenProyekAkhir uploadLaporan(UUID id, MultipartFile file) throws IOException {
        Optional<ManajemenDokumenProyekAkhir> proyekAkhirOptional = this.get(id);
        if (proyekAkhirOptional.isPresent()){
            ManajemenDokumenProyekAkhir proyekAkhir = proyekAkhirOptional.get();
            StorageDto storageDto = storageService.upload(file.getBytes(), ApplicationEnum.minioFolder.DOCUMENT.name(), ".pdf");
            if (storageDto != null){
                proyekAkhir.setLaporanFileName(file.getOriginalFilename());
                proyekAkhir.setLaporanMinioId(storageDto.getIdentifier());
                proyekAkhir.setLaporanMinioPath(storageDto.getFilePath());
                this.update(proyekAkhir);

                FileDocument fileDocument = new FileDocument();
                fileDocument.setManajemenDokumenProyekAkhir(proyekAkhir);
                fileDocument.setExtension(".pdf");
                fileDocument.setFileId(storageDto.getIdentifier());
                fileDocument.setFilePath(storageDto.getFilePath());
                fileDocument.setCreatedDate(new Date());
                fileDocumentRepository.save(fileDocument);
            }
            return proyekAkhir;
        } else {
            throw new NotFoundException("Data dengan id "+id+" tidak ditemukan");
        }
    }

    @Override
    public Page<ManajemenDokumenProyekAkhirDto> findAllAsDto(List<String> processInstanceId, ManajemenDokumenProyekAkhirDto dto, Pageable pageable) {
        Page<ManajemenDokumenProyekAkhir> entities = this.findAll(processInstanceId, dto, pageable);
        List<BpmnEntity> bpmnEntities = entities.getContent().stream().map(e -> (BpmnEntity) e).collect(Collectors.toList());
        Map<UUID, Map<String, Object>> latestTasks = this.getLatestTasks(bpmnEntities);
        List<ManajemenDokumenProyekAkhirDto> dtos = new ArrayList<>();
        for(ManajemenDokumenProyekAkhir entity : entities.getContent()){
            ManajemenDokumenProyekAkhirDto baseDto = this.instantiateDto();
            entity.setTasks(latestTasks.get(entity.getId()));
            if (!entity.getTasks().containsKey("ENDED")){
                String status = StringUtils.substringBetween(entity.getTasks().keySet().toString(), "[", "]");
                entity.setStatus(status);
            } else {
                entity.setStatus(entity.getTasks().get("ENDED").toString());
            }
            setProgress(entity, dto);
            dtos.add(baseDto.toDto(entity));
        }

        return new PageImpl(dtos, pageable, dtos.size());
    }

    private void setProgress(ManajemenDokumenProyekAkhir entity, ManajemenDokumenProyekAkhirDto dto) {
        switch (entity.getStatus()){
            case "reviewKaprodi":
                dto.setProgress(10);
                break;
            case "reviewKoordinatorPA":
            case "submitMbkmKaprodi":
                dto.setProgress(15);
                break;
            case "reviewTimKBK":
                dto.setProgress(30);
                break;
            case "submitKoordinatorPA":
                dto.setProgress(35);
                break;
            case "submitKaprodi":
                dto.setProgress(40);
                break;
            case "revisiJudul":
                dto.setProgress(5);
                break;
            case "submitProposal":
            case "revisiProposal":
                dto.setProgress(45);
                break;
            case "reviewProposal":
            case "daftarSeminar":
                dto.setProgress(50);
                break;
            case "aturJadwalSeminar":
                dto.setProgress(60);
                break;
            case "inputHasilSeminar":
                dto.setProgress(65);
                break;
            case "submitLaporan":
            case "revisiLaporan":
                dto.setProgress(70);
                break;
            case "reviewPembimbing":
                dto.setProgress(75);
                break;
            case "daftarSidang":
                dto.setProgress(80);
                break;
            case "aturJadwalSidang":
                dto.setProgress(85);
                break;
            case "inputHasilSidang":
                dto.setProgress(90);
                break;
            default:
                dto.setProgress(100);
                break;

        }
    }

    @Override
    public Page<ManajemenDokumenProyekAkhir> findAll(List<String> processInstanceIds, ManajemenDokumenProyekAkhirDto dto, Pageable pageable) {
        Page<ManajemenDokumenProyekAkhir> data = this.jpaRepository.findAll(this.instantiateSpecification(processInstanceIds, dto), pageable);
        List<ManajemenDokumenProyekAkhir> entities = data.getContent();
        List<BpmnEntity> bpmnEntities = entities.stream().map(e -> (BpmnEntity) e).collect(Collectors.toList());
        Map<UUID, Map<String, Object>> tasks = this.getLatestTasks(bpmnEntities);

        for(ManajemenDokumenProyekAkhir entity : entities){
            entity.setTasks(tasks.get(entity.getId()));
            if (!entity.getTasks().containsKey("ENDED")){
                String status = StringUtils.substringBetween(entity.getTasks().keySet().toString(), "[", "]");
                entity.setStatus(status);
            }else {
                entity.setStatus(entity.getTasks().get("ENDED").toString());
            }
        }
        Page<ManajemenDokumenProyekAkhir> newData = new PageImpl<>(entities, pageable, data.getTotalElements());
        return newData;
    }

    @Override
    public Page<ManajemenDokumenProyekAkhir> findAll(ManajemenDokumenProyekAkhirDto keyword, Pageable pageable) {
        Page<ManajemenDokumenProyekAkhir> data = this.jpaRepository.findAll(this.instantiateSpecification(keyword), pageable);
        List<ManajemenDokumenProyekAkhir> entities = data.getContent();
        List<BpmnEntity> bpmnEntities = entities.stream().map(e -> (BpmnEntity) e).collect(Collectors.toList());
        Map<UUID, Map<String, Object>> tasks = this.getLatestTasks(bpmnEntities);
        for(ManajemenDokumenProyekAkhir entity : entities){
            entity.setTasks(tasks.get(entity.getId()));
            if (!entity.getTasks().containsKey("ENDED")){
                String status = StringUtils.substringBetween(entity.getTasks().keySet().toString(), "[", "]");
                entity.setStatus(status);
            }else {
                entity.setStatus(entity.getTasks().get("ENDED").toString());
            }
        }
        return new PageImpl(entities, pageable, data.getTotalElements());
    }

    @Override
    public Page<ManajemenDokumenProyekAkhirDto> findAllAsDto(ManajemenDokumenProyekAkhirDto keyword, Pageable pageable) {
        Page<ManajemenDokumenProyekAkhir> entities = this.jpaRepository.findAll(this.instantiateSpecification(keyword), pageable);
        List<BpmnEntity> bpmnEntities = entities.getContent().stream().map(e -> (BpmnEntity) e).collect(Collectors.toList());
        Map<UUID, Map<String, Object>> tasks = this.getLatestTasks(bpmnEntities);

        List<ManajemenDokumenProyekAkhirDto> dtos = new ArrayList<>();
        for(ManajemenDokumenProyekAkhir entity : entities){
            entity.setTasks(tasks.get(entity.getId()));
            if (!entity.getTasks().containsKey("ENDED")){
                String status = StringUtils.substringBetween(entity.getTasks().keySet().toString(), "[", "]");
                entity.setStatus(status);
            }else {
                entity.setStatus(entity.getTasks().get("ENDED").toString());
            }
            ManajemenDokumenProyekAkhirDto dto = this.instantiateDto();
            setProgress(entity, dto);
            dtos.add(dto.toDto(entity));
        }
        return new PageImpl(dtos, pageable, entities.getTotalElements());
    }

    public Page<ManajemenDokumenProyekAkhir> todo(List<String> processInstanceId, ManajemenDokumenProyekAkhirDto dto, Pageable pageable) {
        Page<ManajemenDokumenProyekAkhir> dtoPage;

        if (this.getRole().equals("admin")){
            dtoPage = this.findAll(dto, pageable);
        } else {
            dtoPage = this.findAll(processInstanceId, dto, pageable);
        }
        List<String> proposal = bimbinganProposalProyekAkhirService.getBimbinganProposalProcess();
        List<String> laporan = bimbinganProyekAkhirService.getBimbinganLaporanProcess();
        List<ManajemenDokumenProyekAkhir> listFinal = new ArrayList<>();
        if (!dtoPage.getContent().isEmpty()){
            listFinal.addAll(dtoPage.getContent());
        }
        Page<ManajemenDokumenProyekAkhir> data = this.findAll(dto, pageable);
        List<ManajemenDokumenProyekAkhir> list = data.getContent();
        if (!proposal.isEmpty()){
            List<ManajemenDokumenProyekAkhir> temp = list.stream().filter(d -> d.getBimbinganProposalProyekAkhir() != null &&
                    proposal.contains(d.getBimbinganProposalProyekAkhir().getProcessInstanceId())).collect(Collectors.toList());
            listFinal.addAll(temp);
        }

        if (!laporan.isEmpty()){
            List<ManajemenDokumenProyekAkhir> temp = list.stream().filter(d -> d.getBimbinganProyekAkhir() != null &&
                    laporan.contains(d.getBimbinganProyekAkhir().getProcessInstanceId())).collect(Collectors.toList());
            listFinal.addAll(temp);
        }

        listFinal.stream().distinct().collect(Collectors.toList());
        dtoPage = new PageImpl<>(listFinal, pageable, listFinal.size());

        return dtoPage;
    }

    public Page<ManajemenDokumenProyekAkhirDto> dashboard(ManajemenDokumenProyekAkhirDto dto, Pageable pageable) {
        Page<ManajemenDokumenProyekAkhirDto> dtoPage = this.findAllAsDto(dto, pageable);
        String user = this.getCurrentUser();
        List<Position> positions = positionRepository.findAll();
        List<String> timKbk = positions.stream().filter(position -> position.getName().toLowerCase().contains("tim kbk")).collect(Collectors.toList()).stream().map(Position::getEmailUser).collect(Collectors.toList());
        List<String> koordinator = positions.stream().filter(position -> position.getName().toLowerCase().contains("koordinator proyek akhir")).collect(Collectors.toList()).stream().map(Position::getEmailUser).collect(Collectors.toList());

        if (!this.getRole().equals("admin") && !timKbk.contains(user) && !koordinator.contains(user)){
            List<ManajemenDokumenProyekAkhirDto> temp = dtoPage.getContent().stream().filter(data -> data.getRequestorEmail().equals(user) || data.getProgramStudi().getKaprodiEmail().equals(user) ||
                    (data.getPembimbing1() != null && data.getPembimbing1().getEmail().equals(user)) || (data.getPembimbing2() != null && data.getPembimbing2().getEmail().equals(user)) ||
                    (data.getPenguji() != null && data.getPenguji().getEmail().equals(user))).collect(Collectors.toList());
            dtoPage = new PageImpl<>(temp, pageable, dtoPage.getTotalElements());
        }

        return dtoPage;
    }

    public Map<String, Long> dashboardCount() {
        Page<ManajemenDokumenProyekAkhirDto> pages = dashboard(new ManajemenDokumenProyekAkhirDto(), Pageable.unpaged());
        List<ManajemenDokumenProyekAkhirDto> listData = pages.getContent();
        Map<String, Long> mapCount = new HashMap<>();
        long terimaKaprodi = 0;
        long terimaKbk = 0;
        long tolakKaprodi = 0;
        long tolakKbk = 0;
        if (!listData.isEmpty()){
            terimaKaprodi = listData.stream().filter(dto -> dto.getHasilReviewKaprodi() != null && dto.getHasilReviewKaprodi().equals("diterima")).count();
            tolakKaprodi = listData.stream().filter(dto -> dto.getHasilReviewKaprodi() != null && dto.getHasilReviewKaprodi().equals("ditolak")).count();
            terimaKbk = listData.stream().filter(dto -> dto.getHasilReviewKbk() != null && dto.getHasilReviewKbk().equals("diterima")).count();
            tolakKbk = listData.stream().filter(dto -> dto.getHasilReviewKbk() != null && dto.getHasilReviewKbk().equals("ditolak")).count();
        }
        mapCount.put("terimaKaprodi", terimaKaprodi);
        mapCount.put("terimaKbk", terimaKbk);
        mapCount.put("tolakKaprodi", tolakKaprodi);
        mapCount.put("tolakKbk", tolakKbk);
        return mapCount;
    }

    public ManajemenDokumenProyekAkhir setPembimbing(ManajemenDokumenProyekAkhir proyekAkhir, ManajemenDokumenProyekAkhirDto dto) {
        if (dto.getPembimbing1() != null && dto.getPembimbing1().getId() != null){
            proyekAkhir.setPembimbing1(dosenRepository.getById(dto.getPembimbing1().getId()));
        }
        if (dto.getPembimbing2() != null && dto.getPembimbing2().getId() != null){
            proyekAkhir.setPembimbing2(dosenRepository.getById(dto.getPembimbing2().getId()));
        }
        return this.jpaRepository.save(proyekAkhir);
    }

    public String[] getBussinessKey(){
        ManajemenDokumenProyekAkhirDto dto = new ManajemenDokumenProyekAkhirDto();
        dto.setRequestorEmail(this.getCurrentUser());
        Page<ManajemenDokumenProyekAkhir> list = this.findAll(dto, Pageable.unpaged());
        if (!list.getContent().isEmpty()){
            List<ManajemenDokumenProyekAkhir> data = list.getContent();
            return Arrays.copyOf(data.stream().map(proyekAkhir -> proyekAkhir.getId().toString()).toArray(), data.size(), String[].class);
        } else {
            return null;
        }

    }
}
