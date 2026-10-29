package com.proyekakhir.manajemendokumen.core.controller;


import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.core.entity.BaseVariable;
import com.proyekakhir.manajemendokumen.core.entity.BpmnEntity;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.core.service.BpmnService;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
public abstract class BpmnController<E, D, V, S, R> extends BaseController {
    protected BpmnService<E, D, V, R> bpmnService;

    public BpmnController(S bpmnService) {
        this.bpmnService = (BpmnService<E, D, V, R>) bpmnService;
    }

    @SneakyThrows
    @PostMapping("")
    public ResponseEntity<Object> start(@RequestParam(required = false, defaultValue = "false") boolean withVariable, V variable, @Valid @RequestBody D dto) {
        if (withVariable) {
            return success(this.bpmnService.start(variable, dto));
        } else {
            return success(this.bpmnService.start(null, dto));
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> listProcess(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") int limit,
                                              @RequestParam(required = false) String sort,
                                              @RequestParam(required = false, defaultValue = "true") boolean asc,
                                              @RequestParam(required = false) String taskDefinitionKey,
                                              D dto) {
        Pageable pageable = pageFromRequest(page, limit, sort, asc);
        if(taskDefinitionKey != null && taskDefinitionKey.length()>0){
            List<String> processInstanceId = this.bpmnService.getCamundaProcessService().getActiveTaskIds(this.bpmnService.getProcessDefinitionKey(), taskDefinitionKey);
            return success(this.bpmnService.findAllAsDto(processInstanceId, dto, pageable));
        }
        else {
            return success(this.bpmnService.findAllAsDto(dto, pageable));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> detail(@PathVariable UUID id) {
        Optional<E> optionalE = this.bpmnService.get(id);
        if (optionalE.isPresent()) {
            HashMap<String, Object> map = new HashMap<>();
            E entity = optionalE.get();
            BpmnEntity bpmnEntity = (BpmnEntity) entity;
            bpmnEntity.setTasks(this.bpmnService.getLatestTasks(entity));
            map.put("object", entity);
            map.put("logs", this.bpmnService.getDistinctLogs(optionalE.get()));
            return success(map);
        } else {
            return error(HttpStatus.NOT_FOUND, "Object not found");
        }
    }

    @GetMapping("/{id}/logs")
    public ResponseEntity<Object> logs(@PathVariable UUID id) {
        Optional<E> optionalE = this.bpmnService.get(id);
        if (optionalE.isPresent()) {
            return success(this.bpmnService.getLogs((BpmnEntity) optionalE.get()));
        } else {
            return error(HttpStatus.NOT_FOUND, "Object not found");
        }
    }


    @GetMapping("/{taskDefinitionKey}/list")
    public ResponseEntity<Object> listTask(
            @PathVariable String taskDefinitionKey,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = Integer.MAX_VALUE + "") int limit,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "true") boolean asc,
            @RequestParam(required = false, defaultValue = "false") boolean search,
            D dto
    ) {
        Pageable pageable = pageFromRequest(page, limit, sort, asc);
        List<String> processInstanceId = this.bpmnService.getCamundaProcessService().getActiveTaskIds(this.bpmnService.getProcessDefinitionKey(), taskDefinitionKey);
        return success(this.bpmnService.findAll(processInstanceId, dto, pageable));
    }


    @PostMapping("/{taskDefinitionKey}/{id}/submit")
    public ResponseEntity<Object> submit(@PathVariable String taskDefinitionKey, @PathVariable UUID id, @RequestParam String decision, @RequestParam(required = false, defaultValue = "false") boolean withVariable, V variable, @RequestBody D dto) {
        if (withVariable) {
            return success(this.bpmnService.submit(taskDefinitionKey, id, decision, variable, dto));
        } else {
            return success(this.bpmnService.submit(taskDefinitionKey, id, decision, null, dto));
        }
    }

    @PostMapping("/{taskDefinitionKey}/{id}/submit/{taskId}")
    public ResponseEntity<Object> submitWithTaskId(@PathVariable String taskDefinitionKey,@PathVariable String taskId, @PathVariable UUID id, @RequestParam String decision, @RequestParam(required = false, defaultValue = "false") boolean withVariable, V variable, @RequestBody D dto) {
        if (withVariable) {
            return success(this.bpmnService.submit(taskDefinitionKey, taskId, id, decision, variable, dto, null));
        } else {
            return success(this.bpmnService.submit(taskDefinitionKey, taskId, id, decision, null, dto, null));
        }
    }

    @PostMapping("/{taskDefinitionKey}/{id}/draft")
    public ResponseEntity<Object> draft(@PathVariable String taskDefinitionKey, @PathVariable UUID id, @RequestBody D dto) {
        Optional<E> optionalE = this.bpmnService.get(id);
        if(optionalE.isPresent()){
            try {
                return success(this.bpmnService.draft(optionalE.get(), dto));
            }
            catch (AlurKerjaException alurKerjaException){
                return error(HttpStatus.EXPECTATION_FAILED, alurKerjaException.getMessage());
            }
        }
        else {
            return error(HttpStatus.NOT_FOUND, "Object not found");
        }
    }

    @GetMapping("/xml")
    public void getBpmnXml (HttpServletResponse httpServletResponse) throws IOException {
        InputStream inputStream = this.bpmnService.getBpmnXml();
        httpServletResponse.setContentType(MediaType.TEXT_XML_VALUE);
        IOUtils.copy(inputStream, httpServletResponse.getOutputStream());
    }

    @GetMapping("/{id}/xml")
    public void getBpmnXml (@PathVariable UUID id, HttpServletResponse httpServletResponse) throws NotFoundException, IOException {
        Optional<E> entity = this.bpmnService.get(id);
        if(entity.isPresent()) {
            InputStream inputStream = this.bpmnService.getBpmnXml((BpmnEntity) entity.get());
            httpServletResponse.setContentType(MediaType.TEXT_XML_VALUE);
            IOUtils.copy(inputStream, httpServletResponse.getOutputStream());
        }
        else{
            throw new NotFoundException("Object not found");
        }
    }

    @GetMapping("/statistic")
    public ResponseEntity<Object> statistic(@RequestParam(value="businessKeys", required = false) String[] businessKeys){
        if (businessKeys == null || businessKeys.length == 0)
            return success(this.bpmnService.getStatistic(null));
        else
            return success(this.bpmnService.getStatistic(businessKeys));
    }

    @GetMapping("/spec")
    public ResponseEntity<Object> spec() throws ClassNotFoundException {
        HashMap<String, Object> result = new HashMap<>();
        BaseDto<E, D> baseDto = (BaseDto<E, D>) this.bpmnService.instantiateDto();
        BaseVariable baseVariable = (BaseVariable) this.bpmnService.instantiateVariable();
        result.put("dto", baseDto.specifications());
        result.put("variable", baseVariable.specifications());
        result.put("decision", this.bpmnService.availableDecision("start"));
        return success(result);
    }


    @GetMapping("/{taskDefinitionKey}/spec")
    public ResponseEntity<Object> taskSpec(@PathVariable String taskDefinitionKey) throws ClassNotFoundException {
        HashMap<String, Object> result = new HashMap<>();
        BaseDto<E, D> baseDto = (BaseDto<E, D>) this.bpmnService.instantiateDto();
        BaseVariable baseVariable = (BaseVariable) this.bpmnService.instantiateVariable();
        result.put("dto", baseDto.specifications());
        result.put("variable", baseVariable.specifications());
        result.put("decision", this.bpmnService.availableDecision(taskDefinitionKey));
        return success(result);
    }

}
