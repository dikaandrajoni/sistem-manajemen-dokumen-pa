package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BimbinganProposalProyekAkhirRepository extends CrudRepository<BimbinganProposalProyekAkhir> {

    @Query(value = "select p.manajemenDokumenProyekAkhir.id from BimbinganProposalProyekAkhir p where p.processInstanceId in :ids and p.manajemenDokumenProyekAkhir.id is not null ")
    List<UUID> getProposalProyekAkhirId(List<String> ids);
}
