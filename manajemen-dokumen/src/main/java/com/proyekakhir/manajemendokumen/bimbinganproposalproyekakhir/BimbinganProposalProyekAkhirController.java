package com.proyekakhir.manajemendokumen.bimbinganproposalproyekakhir;

import com.proyekakhir.manajemendokumen.core.controller.BpmnController;
import org.camunda.bpm.engine.task.Task;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bpmn/BimbinganProposalProyekAkhir")
public class BimbinganProposalProyekAkhirController extends BpmnController<BimbinganProposalProyekAkhir, BimbinganProposalProyekAkhirDto, BimbinganProposalProyekAkhirVariable, BimbinganProposalProyekAkhirService, BimbinganProposalProyekAkhirRepository> {


    public BimbinganProposalProyekAkhirController(BimbinganProposalProyekAkhirService bimbinganproposalproyekakhirService) {
        super(bimbinganproposalproyekakhirService);
    }

    @GetMapping("/todo")
    public ResponseEntity<Object> todo(@RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") int limit,
                                       @RequestParam(required = false) String sort,
                                       @RequestParam(required = false, defaultValue = "true") boolean asc,
                                       BimbinganProposalProyekAkhirDto dto) {
        Pageable pageable = pageFromRequest(page, limit, sort, asc);
        if (bpmnService.getRole().equals("admin")){
            return success(this.bpmnService.findAllAsDto(dto, pageable));
        } else {
            List<String> processInstanceId = this.bpmnService.getCamundaProcessService().getTaskByAssignee(this.bpmnService.getProcessDefinitionKey(), this.bpmnService.getCurrentUser())
                    .stream().map(Task::getProcessInstanceId).collect(Collectors.toList());
            return success(this.bpmnService.findAllAsDto(processInstanceId, dto, pageable));
        }
    }
}
