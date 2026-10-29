package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitmbkmkaprodi;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/submitMbkmKaprodi")
public class SubmitMbkmKaprodiController extends UserTaskController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, SubmitMbkmKaprodiService, ManajemenDokumenProyekAkhirRepository> {
    public SubmitMbkmKaprodiController(SubmitMbkmKaprodiService draftingService) {
        super(draftingService, "submitMbkmKaprodi");
    }

}
