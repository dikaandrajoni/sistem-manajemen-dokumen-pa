package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.daftarseminar;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/daftarSeminar")
public class DaftarseminarController extends UserTaskController<ManajemenDokumenProyekAkhir, DaftarseminarDto, DaftarseminarVariable, DaftarseminarService, ManajemenDokumenProyekAkhirRepository> {
    public DaftarseminarController(DaftarseminarService daftarSeminarService) {
        super(daftarSeminarService, "daftarSeminar");
    }
}
