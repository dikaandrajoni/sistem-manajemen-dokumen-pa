package com.proyekakhir.manajemendokumen.bimbinganproyekakhir.revisi;

import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirRepository;
import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhirVariable;
import com.proyekakhir.manajemendokumen.core.controller.UserTaskController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/BimbinganProyekAkhir/revisiLaporan")
public class RevisiController extends UserTaskController<BimbinganProyekAkhir, RevisiDto, BimbinganProyekAkhirVariable, RevisiService, BimbinganProyekAkhirRepository> {
    public RevisiController(RevisiService revisiService) {
        super(revisiService, "revisiLaporan");
    }
}
