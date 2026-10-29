package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitkaprodi;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.submitpraproposal.DraftingVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/submitKaprodi")
public class SubmitkaprodiController extends UserTaskController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, DraftingVariable, SubmitkaprodiService, ManajemenDokumenProyekAkhirRepository> {
    public SubmitkaprodiController(SubmitkaprodiService submitKaprodiService) {
        super(submitKaprodiService, "submitKaprodi");
    }
}
