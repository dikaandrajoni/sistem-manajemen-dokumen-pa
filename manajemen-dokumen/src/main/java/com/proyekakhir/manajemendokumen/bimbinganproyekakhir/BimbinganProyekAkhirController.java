package com.proyekakhir.manajemendokumen.bimbinganproyekakhir;

import com.proyekakhir.manajemendokumen.core.controller.BpmnController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bpmn/BimbinganProyekAkhir")
public class BimbinganProyekAkhirController extends BpmnController<BimbinganProyekAkhir, BimbinganProyekAkhirDto, BimbinganProyekAkhirVariable, BimbinganProyekAkhirService, BimbinganProyekAkhirRepository> {
    public BimbinganProyekAkhirController(BimbinganProyekAkhirService bimbinganproyekakhirService) {
        super(bimbinganproyekakhirService);
    }
}
