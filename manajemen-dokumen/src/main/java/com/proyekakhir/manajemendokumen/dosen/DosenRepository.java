package com.proyekakhir.manajemendokumen.dosen;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DosenRepository extends CrudRepository<Dosen> {
    Dosen findByEmail(String email);
}
