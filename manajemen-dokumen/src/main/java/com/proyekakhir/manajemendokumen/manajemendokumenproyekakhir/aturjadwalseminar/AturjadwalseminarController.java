package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.aturjadwalseminar;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/aturJadwalSeminar")
public class AturjadwalseminarController extends UserTaskController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, AturjadwalseminarVariable, AturjadwalseminarService, ManajemenDokumenProyekAkhirRepository> {
    public AturjadwalseminarController(AturjadwalseminarService aturJadwalSeminarService) {
        super(aturJadwalSeminarService, "aturJadwalSeminar");
    }
}
