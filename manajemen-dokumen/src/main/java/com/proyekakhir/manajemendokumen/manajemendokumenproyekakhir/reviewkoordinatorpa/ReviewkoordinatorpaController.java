package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.reviewkoordinatorpa;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/reviewKoordinatorPA")
public class ReviewkoordinatorpaController extends UserTaskController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, ReviewkoordinatorpaVariable, ReviewkoordinatorpaService, ManajemenDokumenProyekAkhirRepository> {
    public ReviewkoordinatorpaController(ReviewkoordinatorpaService reviewKoordinatorPAService) {
        super(reviewKoordinatorPAService, "reviewKoordinatorPA");
    }
}
