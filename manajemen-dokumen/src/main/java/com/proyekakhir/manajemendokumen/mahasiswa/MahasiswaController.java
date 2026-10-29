package com.proyekakhir.manajemendokumen.mahasiswa;

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
@RequestMapping("/api/v1/mahasiswa")
public class MahasiswaController extends CrudController<Mahasiswa, MahasiswaDto, MahasiswaService, MahasiswaRepository> {

    @Autowired
    MahasiswaService mahasiswaService;

    protected MahasiswaController(MahasiswaService crudService) {
        super(crudService);
    }

    @PostMapping("/{id}/createAkun")
    public ResponseEntity<Object> createAkun(@PathVariable UUID id) throws AlurKerjaException {
        return success(mahasiswaService.generateUser(id));
    }
}
