package com.proyekakhir.manajemendokumen.bimbinganproyekakhir.submitlaporan;

import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/BimbinganProyekAkhir/submitLaporan")
public class SubmitdokumenController extends UserTaskController<BimbinganProyekAkhir, SubmitdokumenDto, SubmitdokumenVariable, SubmitdokumenService, BimbinganProyekAkhirRepository> {
    public SubmitdokumenController(SubmitdokumenService submitDokumenService) {
        super(submitDokumenService, "submitLaporan");
    }
}
