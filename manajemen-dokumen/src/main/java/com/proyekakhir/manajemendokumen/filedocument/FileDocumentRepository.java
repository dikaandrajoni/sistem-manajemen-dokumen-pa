package com.proyekakhir.manajemendokumen.filedocument;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDocumentRepository extends CrudRepository<FileDocument> {
    FileDocument findByFileId(String fileId);
    List<FileDocument> findAllByFileId(String fileId);
    FileDocument findByTypeEquals(String type);
    List<FileDocument> findAllByManajemenDokumenProyekAkhir(ManajemenDokumenProyekAkhir proyekAkhir);
}
