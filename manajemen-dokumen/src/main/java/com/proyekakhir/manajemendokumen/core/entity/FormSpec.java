package com.proyekakhir.manajemendokumen.core.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class FormSpec {
    private String name;
    private String label;
    private Boolean required = false;
    private String type;
    private String[] constraints;
    private HashMap<String, Object> metadata;
    public FormSpec(){

    }

    public FormSpec(String name, String label, Boolean required, String type) {
        this.name = name;
        this.label = label;
        this.required = required;
        this.type = type;
    }

    public FormSpec(String name, String label, Boolean required, String type, String url) {
        this.name = name;
        this.label = label;
        this.required = required;
        this.type = type;
    }
}
