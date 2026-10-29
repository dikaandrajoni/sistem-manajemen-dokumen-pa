package com.proyekakhir.manajemendokumen.position;

import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/position")
public class PositionController extends CrudController<Position, PositionDto, PositionService, PositionRepository> {

    @Autowired
    PositionService positionService;

    protected PositionController(PositionService crudService) {
        super(crudService);
    }

    @GetMapping("/timkbk")
    public ResponseEntity<Object> getTimKbk(){
        return success(positionService.getTimKBK());
    }
}
