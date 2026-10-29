package com.proyekakhir.manajemendokumen.programstudi;

import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/program-studi")
public class ProgramStudiController extends CrudController<ProgramStudi, ProgramStudiDto, ProgramStudiService, ProgramStudiRepository> {

    @Autowired
    ProgramStudiService programStudiService;

    protected ProgramStudiController(ProgramStudiService crudService) {
        super(crudService);
    }

    @PostMapping("/{id}/setKaprodi")
    public ResponseEntity<Object> setKaprodi(@PathVariable UUID id, @Valid @RequestBody ProgramStudiDto dto){
        return success(programStudiService.setKaprodi(id, dto));
    }
}
