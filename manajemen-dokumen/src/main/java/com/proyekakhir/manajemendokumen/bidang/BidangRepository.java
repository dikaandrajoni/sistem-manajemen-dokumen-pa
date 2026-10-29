package com.proyekakhir.manajemendokumen.bidang;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidangRepository extends CrudRepository<Bidang> {
    Bidang findByNameEquals(String name);
}
