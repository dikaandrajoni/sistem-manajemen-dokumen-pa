package com.proyekakhir.manajemendokumen.core.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.util.UUID;

public class CrudRepositoryImp<T> extends SimpleJpaRepository<T, UUID> implements CrudRepository<T> {
    public CrudRepositoryImp(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }
}
