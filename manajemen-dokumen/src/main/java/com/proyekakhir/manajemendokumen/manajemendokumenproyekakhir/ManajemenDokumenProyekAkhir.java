package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir.BimbinganProposalProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.core.entity.BpmnEntity;
import com.proyekakhir.manajemendokumen.dosen.Dosen;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class ManajemenDokumenProyekAkhir extends BpmnEntity {
    private String requestorCode;
    private String requestorName;
    private String requestorEmail;
    private String judulProyekAkhir;
    private Integer tahun;
    private Boolean isMbkm;
    private String bidangTopikPA;
    private String proposalFileName;
    private String proposalMinioId;
    private String proposalMinioPath;
    private Boolean proposalIsApproved;
    private Boolean isApproved;
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dosen pembimbing1;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Dosen pembimbing2;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Dosen penguji;
    private String laporanFileName;
    private String laporanMinioId;
    private String laporanMinioPath;
    private Boolean laporanIsApproved;
    private String hasilReviewKaprodi;
    private String hasilReviewKbk;
    private String hasilSeminar;
    private String hasilSidang;
    private Date jadwalSeminarProposal;
    private String lokasiSeminar;
    private Date jadwalSidangProyekAkhir;
    private String lokasiSidang;
    private Date deleteDate;
    private String deleteBy;
    private String status;
    @Type(type = "text")
    private String catatan;
    @Type(type = "text")
    private String catatanKaprodi;
    @Type(type = "text")
    private String catatanKbk;
    @Type(type = "text")
    private String catatanPengujiSeminar;
    @Type(type = "text")
    private String catatanPengujiSidang;

    @Type(type = "text")
    private String deskripsi;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private ProgramStudi programStudi;

    @OneToOne(mappedBy = "manajemenDokumenProyekAkhir")
    private BimbinganProyekAkhir bimbinganProyekAkhir;

    @OneToOne(mappedBy = "manajemenDokumenProyekAkhir")
    private BimbinganProposalProyekAkhir bimbinganProposalProyekAkhir;

    @Override
    public BimbinganProposalProyekAkhir subProcessEntity() {
        return bimbinganProposalProyekAkhir;
    }

    @Override
    public BimbinganProyekAkhir secondarySubProcessEntity(){
        return bimbinganProyekAkhir;
    }
}
