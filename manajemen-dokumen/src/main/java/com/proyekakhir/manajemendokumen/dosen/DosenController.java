package com.proyekakhir.manajemendokumen.dosen;

import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/dosen")
public class DosenController extends CrudController<Dosen, DosenDto, DosenService, DosenRepository> {

    @Autowired
    DosenService dosenService;

    protected DosenController(DosenService crudService) {
        super(crudService);
    }

    @PostMapping("/{id}/generateUser")
    public ResponseEntity<Object> generateUser(@PathVariable UUID id) throws AlurKerjaException {
        return success(dosenService.generateUser(id));
    }

}
