package com.proyekakhir.manajemendokumen.bimbinganproyekakhir;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BimbinganProyekAkhirRepository extends CrudRepository<BimbinganProyekAkhir> {
    @Query(value = "select p.manajemenDokumenProyekAkhir.id from BimbinganProyekAkhir p where p.manajemenDokumenProyekAkhir.id is not null ")
    List<UUID> getLaporanProyekAkhirId();
}
