package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir;

import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhirDto;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirDto;
import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.dosen.Dosen;
import com.proyekakhir.manajemendokumen.dosen.DosenDto;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Getter
@Setter
public class ManajemenDokumenProyekAkhirDto extends BaseDto<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto> {
    private String status;
    private String requestorCode;
    private String requestorName;
    private String requestorEmail;
    private Integer tahun;
    private ProgramStudiDto programStudi;
    private String judulProyekAkhir;
    private String bidangTopikPA;
    private Boolean isMbkm;
    private String proposalFileName;
    private String proposalMinioId;
    private String proposalMinioPath;
    private DosenDto pembimbing1;
    private DosenDto pembimbing2;
    private DosenDto penguji;
    private String laporanFileName;
    private String laporanMinioId;
    private String laporanMinioPath;
    private Date jadwalSeminarProposal;
    private String lokasiSeminar;
    private Date jadwalSidangProyekAkhir;
    private String lokasiSidang;
    private Date createdDate;
    @Type(type = "text")
    private String catatan;
    private Boolean isApproved;
    private Boolean proposalIsApproved;
    private Boolean laporanIsApproved;
    @Type(type = "text")
    private String deskripsi;
    private String hasilSeminar;
    private String hasilSidang;
    private String hasilReviewKaprodi;
    private String hasilReviewKbk;
    private BimbinganProposalProyekAkhirDto bimbinganProposalProyekAkhir;
    private BimbinganProyekAkhirDto bimbinganProyekAkhir;
    private Integer progress;
    @Type(type = "text")
    private String catatanKaprodi;
    @Type(type = "text")
    private String catatanKbk;
    @Type(type = "text")
    private String catatanPengujiSeminar;
    @Type(type = "text")
    private String catatanPengujiSidang;

    @Override
    public ManajemenDokumenProyekAkhirDto toDto(ManajemenDokumenProyekAkhir entity) {
        ManajemenDokumenProyekAkhirDto dto = super.toDto(entity);
        if (entity.getProgramStudi() != null){
            ProgramStudiDto programStudiDto = new ProgramStudiDto();
            programStudiDto.toDto(entity.getProgramStudi());
            dto.setProgramStudi(programStudiDto);
        }
        if (entity.getPembimbing1() != null){
            DosenDto dosenDto = new DosenDto();
            dosenDto.toDto(entity.getPembimbing1());
            dto.setPembimbing1(dosenDto);
        }
        if (entity.getPembimbing2() != null){
            DosenDto dosenDto = new DosenDto();
            dosenDto.toDto(entity.getPembimbing2());
            dto.setPembimbing2(dosenDto);
        }
        if (entity.getPenguji() != null){
            DosenDto dosenDto = new DosenDto();
            dosenDto.toDto(entity.getPenguji());
            dto.setPenguji(dosenDto);
        }
        return dto;
    }

    @Override
    public ManajemenDokumenProyekAkhir fromDto() {
        ManajemenDokumenProyekAkhir proyekAkhir = new ManajemenDokumenProyekAkhir();
        BeanUtils.copyProperties(this, proyekAkhir, getNullPropertyNames(this));
        if (!ObjectUtils.isEmpty(programStudi) && !ObjectUtils.isEmpty(programStudi.getId())){
            ProgramStudi prodi = new ProgramStudi();
            prodi.setId(this.programStudi.getId());
            proyekAkhir.setProgramStudi(prodi);
        }
        if (!ObjectUtils.isEmpty(pembimbing1) && !ObjectUtils.isEmpty(pembimbing1.getId())){
            Dosen dosen = new Dosen();
            dosen.setId(this.pembimbing1.getId());
            proyekAkhir.setPembimbing1(dosen);
        }
        if (!ObjectUtils.isEmpty(pembimbing2) && !ObjectUtils.isEmpty(pembimbing2.getId())){
            Dosen dosen = new Dosen();
            dosen.setId(this.pembimbing2.getId());
            proyekAkhir.setPembimbing2(dosen);
        }
        if (!ObjectUtils.isEmpty(penguji) && !ObjectUtils.isEmpty(penguji.getId())){
            Dosen dosen = new Dosen();
            dosen.setId(this.penguji.getId());
            proyekAkhir.setPenguji(dosen);
        }
        return proyekAkhir;
    }
}
