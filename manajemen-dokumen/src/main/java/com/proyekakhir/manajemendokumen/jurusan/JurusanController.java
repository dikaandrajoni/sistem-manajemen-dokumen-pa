package com.proyekakhir.manajemendokumen.jurusan;

import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/jurusan")
public class JurusanController extends CrudController<Jurusan, JurusanDto, JurusanService, JurusanRepository> {

    protected JurusanController(JurusanService crudService) {
        super(crudService);
    }
}
