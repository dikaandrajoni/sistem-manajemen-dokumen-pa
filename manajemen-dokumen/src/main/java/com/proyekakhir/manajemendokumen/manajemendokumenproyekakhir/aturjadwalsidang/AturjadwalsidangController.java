package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.aturjadwalsidang;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/aturJadwalSidang")
public class AturjadwalsidangController extends UserTaskController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, AturjadwalsidangVariable, AturjadwalsidangService, ManajemenDokumenProyekAkhirRepository> {
    public AturjadwalsidangController(AturjadwalsidangService aturJadwalSidangService) {
        super(aturJadwalSidangService, "aturJadwalSidang");
    }
}
