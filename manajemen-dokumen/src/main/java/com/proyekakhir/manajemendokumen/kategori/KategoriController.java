package com.proyekakhir.manajemendokumen.kategori;

import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/kategori")
public class KategoriController extends CrudController<Kategori, KategoriDto, KategoriService, KategoriRepository> {

    protected KategoriController(KategoriService crudService) {
        super(crudService);
    }
}
