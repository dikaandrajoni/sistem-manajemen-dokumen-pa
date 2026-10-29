package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitlaporanmbkm;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/submitLaporanMbkm")
public class SubmitLaporanMbkmController extends UserTaskController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, SubmitLaporanMbkmVariable, SubmitLaporanMbkmService, ManajemenDokumenProyekAkhirRepository> {
    public SubmitLaporanMbkmController(SubmitLaporanMbkmService draftingService) {
        super(draftingService, "submitLaporanMbkm");
    }
}
