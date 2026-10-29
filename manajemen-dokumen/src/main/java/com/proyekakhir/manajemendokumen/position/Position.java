package com.proyekakhir.manajemendokumen.position;

import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Position extends CrudEntity {
    private String name;
    private String emailUser;
    private String namaUser;
    private String nipUser;

}
