package com.proyekakhir.manajemendokumen.menu;

import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Menu extends CrudEntity {
    private String name;
    private String url;
}
