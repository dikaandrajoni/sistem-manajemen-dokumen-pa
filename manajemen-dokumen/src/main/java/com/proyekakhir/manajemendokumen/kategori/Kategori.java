package com.proyekakhir.manajemendokumen.kategori;

import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Kategori extends CrudEntity {
    private String name;
    private String templateType;
    private String templateFileName;
    private String templateMinioId;
    private String templateMinioPath;
}
