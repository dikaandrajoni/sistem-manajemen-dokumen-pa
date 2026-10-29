package com.proyekakhir.manajemendokumen.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import com.proyekakhir.manajemendokumen.menu.Menu;
import com.proyekakhir.manajemendokumen.position.Position;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends CrudEntity {
    private String name;
    private String username;
    private String email;
    private String role;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Position position;
    @JsonIgnore
    private String password;
    private String signatureId;
    private String signaturePath;
    private String signatureName;
    private String posisi;
    @Transient
    private List<Menu> menu;
}
