package com.proyekakhir.manajemendokumen.programstudi;

import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@Setter
public class ProgramStudi extends CrudEntity {
    private String kode;
    private String diploma;
    private String name;
    private UUID kaprodiId;
    private String kaprodiName;
    private String kaprodiEmail;
    private String kaprodiNip;
}
