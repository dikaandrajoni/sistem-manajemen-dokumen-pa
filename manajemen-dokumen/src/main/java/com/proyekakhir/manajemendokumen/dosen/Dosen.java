package com.proyekakhir.manajemendokumen.dosen;

import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class Dosen extends CrudEntity {
    private String nip;
    private String name;
    private String email;
    private String jenisKelamin;
    private String position;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ProgramStudi programStudi;

}
