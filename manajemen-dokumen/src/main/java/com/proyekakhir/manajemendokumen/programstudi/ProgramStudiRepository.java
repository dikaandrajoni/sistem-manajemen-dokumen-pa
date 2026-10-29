package com.proyekakhir.manajemendokumen.programstudi;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramStudiRepository extends CrudRepository<ProgramStudi> {
    ProgramStudi findByName(String programStudi);
}
