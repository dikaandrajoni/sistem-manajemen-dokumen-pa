package com.proyekakhir.manajemendokumen.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface CrudRepository<T> extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {
}
