package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitkoordinatorpa;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/submitKoordinatorPA")
public class SubmitkoordinatorpaController extends UserTaskController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, SubmitkoordinatorpaService, ManajemenDokumenProyekAkhirRepository> {
    public SubmitkoordinatorpaController(SubmitkoordinatorpaService submitKoordinatorPAService) {
        super(submitKoordinatorPAService, "submitKoordinatorPA");
    }
}
