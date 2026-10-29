package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ManajemenDokumenProyekAkhirRepository extends CrudRepository<ManajemenDokumenProyekAkhir> {
    @Query(value = "select p from ManajemenDokumenProyekAkhir p " +
            "where p.requestorEmail=:email or p.pembimbing1.email=:email or p.pembimbing1.email=:email" +
            " or p.penguji.email=:email or p.programStudi.kaprodiEmail=:email")
    Page<ManajemenDokumenProyekAkhir> findDashboard(String email, Pageable pageable);

    @Query(value = "select p from ManajemenDokumenProyekAkhir p " +
            "where p.requestorEmail=:email or p.pembimbing1.email=:email or p.pembimbing1.email=:email" +
            " or p.penguji.email=:email or p.programStudi.kaprodiEmail=:email")
    List<ManajemenDokumenProyekAkhir> findDashboardCount(String email);

    List<ManajemenDokumenProyekAkhir> findAllByIdIn(List<UUID> ids);

    List<ManajemenDokumenProyekAkhir> findAllByRequestorEmail(String user);

}
