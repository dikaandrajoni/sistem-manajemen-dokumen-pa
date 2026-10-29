package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/submitPraProposal")
public class DraftingController extends UserTaskController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, DraftingService, ManajemenDokumenProyekAkhirRepository> {
    public DraftingController(DraftingService draftingService) {
        super(draftingService, "submitPraProposal");
    }

}
