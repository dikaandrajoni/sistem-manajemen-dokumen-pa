package com.proyekakhir.manajemendokumen.bidang;

import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/bidang")
public class BidangController extends CrudController<Bidang, BidangDto, BidangService, BidangRepository> {

    protected BidangController(BidangService crudService) {
        super(crudService);
    }
}
