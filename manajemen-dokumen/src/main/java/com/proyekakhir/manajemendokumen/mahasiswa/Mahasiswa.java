package com.proyekakhir.manajemendokumen.mahasiswa;

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
public class Mahasiswa extends CrudEntity {
    private String nim;
    private String name;
    private String email;
    private String kelas;
    private String jenisKelamin;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ProgramStudi programStudi;
}
