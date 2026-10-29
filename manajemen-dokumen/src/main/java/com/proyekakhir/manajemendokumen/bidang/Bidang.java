package com.proyekakhir.manajemendokumen.bidang;

import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import com.proyekakhir.manajemendokumen.position.Position;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
public class Bidang extends CrudEntity {
    private String name;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Position timKBK;

}
