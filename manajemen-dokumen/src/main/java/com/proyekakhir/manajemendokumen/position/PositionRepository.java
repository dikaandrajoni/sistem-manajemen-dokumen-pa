package com.proyekakhir.manajemendokumen.position;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<Position> {
    Position findByNameEquals(String name);
}
