package com.proyekakhir.manajemendokumen.core.controller;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.core.entity.BaseVariable;
import com.proyekakhir.manajemendokumen.core.entity.BpmnEntity;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.core.service.BpmnService;
import com.proyekakhir.manajemendokumen.core.service.CamundaProcessService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
public abstract class UserTaskController<E, D, V, S, R> extends BaseController{
    protected BpmnService<E, D, V, R> bpmnService;
    protected String taskDefinitionKey;
    @Autowired
    protected CamundaProcessService camundaProcessService;

    public UserTaskController(S userTaskService, String taskDefinitionKey) {
        this.bpmnService = (BpmnService<E, D, V, R>) userTaskService;
        this.taskDefinitionKey = taskDefinitionKey;
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listTask(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") int limit,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "true") boolean asc,
            D dto
    ) {
        Pageable pageable = pageFromRequest(page, limit, sort, asc);
        List<String> processInstanceId = this.bpmnService.getCamundaProcessService().getActiveTaskIds(this.bpmnService.getProcessDefinitionKey(), taskDefinitionKey);
        return success(this.bpmnService.findAll(processInstanceId, dto, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> detail(@PathVariable UUID id){
        Optional<E> optionalE = this.bpmnService.get(id);
        if (optionalE.isPresent()) {
            HashMap<String, Object> map = new HashMap<>();
            E entity = optionalE.get();
            BpmnEntity bpmnEntity = (BpmnEntity) entity;
            bpmnEntity.setTasks(this.bpmnService.getLatestTasks(entity));
            map.put("object", optionalE.get());
            map.put("logs", this.bpmnService.getDistinctLogs(optionalE.get()));
            return success(map);
        } else {
            return error(HttpStatus.NOT_FOUND, "Object not found");
        }
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Object> submit( @PathVariable UUID id, @RequestParam String decision, @RequestParam(required = false, defaultValue = "false") boolean withVariable, V variable, @Valid @RequestBody D dto) throws AlurKerjaException {
        if (withVariable) {
            return success(this.bpmnService.submit(taskDefinitionKey, id, decision, variable, dto));
        } else {
            return success(this.bpmnService.submit(taskDefinitionKey, id, decision, null, dto));
        }
    }

    @PostMapping("/bulk-submit")
    public ResponseEntity<Object> submitBulk( @RequestParam String id, @RequestParam String decision, @RequestParam(required = false, defaultValue = "false") boolean withVariable, V variable, @Valid @RequestBody List<D> dto) throws AlurKerjaException {
        if (!id.contains(",") || dto.size() <= 1){
            return error(HttpStatus.METHOD_NOT_ALLOWED, "Doesn't meet the requirements");
        }
        String[] ids = id.split(",");
        int i = 0;
        if (withVariable) {
            for (String Id:ids){
                if (!this.bpmnService.submit(taskDefinitionKey, UUID.fromString(Id), decision, variable, dto.get(i))){
                    return error(HttpStatus.NOT_FOUND, "Id "+id+" not found");
                }
                i++;
            }
        } else {
            for (String Id:ids){
                if (!this.bpmnService.submit(taskDefinitionKey, UUID.fromString(Id), decision, null, dto.get(i))){
                    return error(HttpStatus.NOT_FOUND, "Id "+id+" not found");
                }
                i++;
            }
        }
        return success(true);
    }

    @PostMapping("/{id}/submit/{taskId}")
    public ResponseEntity<Object> submitWithTaskId(@PathVariable String taskId, @PathVariable UUID id, @RequestParam String decision, @RequestParam(required = false, defaultValue = "false") boolean withVariable, V variable, @Valid @RequestBody D dto) {
        if (withVariable) {
            return success(this.bpmnService.submit(taskDefinitionKey, taskId, id, decision, variable, dto, null));
        } else {
            return success(this.bpmnService.submit(taskDefinitionKey, taskId, id, decision, null, dto, null));
        }
    }

    @PostMapping("/{id}/draft")
    public ResponseEntity<Object> draft(@PathVariable UUID id, @RequestBody D dto) throws AlurKerjaException {
        Optional<E> optionalE = this.bpmnService.get(id);
        if(optionalE.isPresent()){
            return success(this.bpmnService.draft(optionalE.get(), dto));
        }
        else {
            return error(HttpStatus.NOT_FOUND, "Object not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) throws AlurKerjaException {
        Optional<E> optionalE = this.bpmnService.get(id);
        if (optionalE.isPresent()){
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(id.toString()).singleResult();
            if (processInstance!= null){
                this.bpmnService.delete(optionalE.get());
                runtimeService.deleteProcessInstance(processInstance.getId(), "delete");
            }else {
                return error(HttpStatus.NOT_FOUND, "Process Instance not found");
            }

        }else {
            return error(HttpStatus.NOT_FOUND, "Object Not Found");
        }
        return success("success");
    }

    @GetMapping("/spec")
    public ResponseEntity<Object> specifications() throws ClassNotFoundException {
        HashMap<String, Object> result = new HashMap<>();
        BaseDto<E, D> baseDto = (BaseDto<E, D>) this.bpmnService.instantiateDto();
        BaseVariable baseVariable = (BaseVariable) this.bpmnService.instantiateVariable();
        result.put("dto", baseDto.specifications());
        result.put("variable", baseVariable.specifications());
        result.put("decision", this.bpmnService.availableDecision(taskDefinitionKey));
        return success(result);
    }

    @GetMapping("{id}/logs")
    public ResponseEntity<Object> logs(@PathVariable UUID id) {
        Optional<E> optionalE = this.bpmnService.get(id);
        if (optionalE.isPresent()) {
            return success(this.bpmnService.getLogs((BpmnEntity) optionalE.get()));
        } else {
            return error(HttpStatus.NOT_FOUND, "Object not found");
        }
    }
}
