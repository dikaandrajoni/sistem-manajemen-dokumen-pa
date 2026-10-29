package com.proyekakhir.manajemendokumen.bimbinganproyekakhir;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyekakhir.manajemendokumen.core.entity.BpmnEntity;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class BimbinganProyekAkhir extends BpmnEntity {
    @OneToOne
    @JsonIgnore
    private ManajemenDokumenProyekAkhir manajemenDokumenProyekAkhir;
    @Column(columnDefinition = "boolean default false")
    private Boolean pembimbing1IsApproved;
    @Column(columnDefinition = "boolean default false")
    private Boolean pembimbing2IsApproved;
    @Type(type = "text")
    private String catatanP1;
    @Type(type = "text")
    private String catatanP2;
    private Integer bimbinganKe;
}
