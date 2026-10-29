package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.reviewkaprodi;

import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/ManajemenDokumenProyekAkhir/reviewKaprodi")
public class ReviewkaprodiController extends UserTaskController<ManajemenDokumenProyekAkhir, ManajemenDokumenProyekAkhirDto, ReviewkaprodiVariable, ReviewkaprodiService, ManajemenDokumenProyekAkhirRepository> {
    public ReviewkaprodiController(ReviewkaprodiService reviewKaprodiService) {
        super(reviewKaprodiService, "reviewKaprodi");
    }
}
