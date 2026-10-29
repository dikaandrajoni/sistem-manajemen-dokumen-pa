package com.proyekakhir.manajemendokumen.filedocument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class FileDocument extends CrudEntity {
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonIgnore
    private ManajemenDokumenProyekAkhir manajemenDokumenProyekAkhir;

    private String fileName;

    private String type;

    private String fileId;

    private String filePath;

    private String extension;
}
