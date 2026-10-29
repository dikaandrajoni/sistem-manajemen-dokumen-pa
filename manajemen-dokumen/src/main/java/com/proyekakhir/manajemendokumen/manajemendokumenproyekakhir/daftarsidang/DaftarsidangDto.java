package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.daftarsidang;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.dosen.DosenDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Date;

@Getter
@Setter
public class DaftarsidangDto extends BaseDto<ManajemenDokumenProyekAkhir, DaftarsidangDto> {
    private String status;
    private String requestorCode;
    private String requestorName;
    private String requestorEmail;
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
    private Date jadwalSidangProyekAkhir;
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
}
