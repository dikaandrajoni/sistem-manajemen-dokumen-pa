package com.proyekakhir.manajemendokumen.jurusan;

import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Jurusan extends CrudEntity {
    private String name;
    private String kode;
    private String kajurId;
    private String kajurNip;
    private String kajurName;
    private String kajurEmail;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "jurusan_id")
    private List<ProgramStudi> programStudi;
}
