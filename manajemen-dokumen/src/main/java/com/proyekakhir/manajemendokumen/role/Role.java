package com.proyekakhir.manajemendokumen.role;

import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Role extends CrudEntity {
    private String name;
    private String description;

}
