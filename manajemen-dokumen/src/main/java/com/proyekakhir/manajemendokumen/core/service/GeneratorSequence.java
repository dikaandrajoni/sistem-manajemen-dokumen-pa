package com.proyekakhir.manajemendokumen.core.service;

public interface GeneratorSequence {
    void create(String sequenceName);
    long get(String sequenceName);
}
