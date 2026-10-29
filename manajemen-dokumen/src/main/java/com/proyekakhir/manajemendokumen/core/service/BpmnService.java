package com.proyekakhir.manajemendokumen.core.service;

import com.proyekakhir.manajemendokumen.core.entity.*;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import lombok.Getter;
import lombok.Setter;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.CallActivity;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class BpmnService<E, D, V, R> extends CrudService<E, D, R> {
    protected String processDefinitionKey;
    protected CamundaProcessService camundaProcessService;
    protected ActivityLogService activityLogService;

    protected BpmnService(R simpleJpaRepository, CamundaProcessService camundaProcessService, ActivityLogService activityLogService) {
        super(simpleJpaRepository);
        this.camundaProcessService = camundaProcessService;
        this.activityLogService = activityLogService;
        this.processDefinitionKey = this.getSimpleClassName(0);
    }
    public Map<String, Object> start(V variable, D dto) throws AlurKerjaException {
        return this.start(variable, dto, null);
    }
    public Map<String, Object> start(V variable, D dto,  String remarks) throws AlurKerjaException {
        /*
            #1 Create entity
         */
        E entity = this.createFromDto(dto);
        BpmnEntity bpmnEntity = (BpmnEntity) entity;
         /*
            #2 Start BPMN Process
         */
        ProcessInstance processInstance = camundaProcessService.startProcess(this.getProcessDefinitionKey(), bpmnEntity.getBusinessKey(), variable != null ? ((BaseVariable) variable).asMap() : null);
        
         /*
            #3 Save processInstance ID to entity
         */
        bpmnEntity.setProcessInstanceId(processInstance.getProcessInstanceId());
        this.update(entity);
        
         /*
            #4 Save Log
         */
        List<Task> afterTasks = this.camundaProcessService.getTaskByProcessInstanceId(
                this.getProcessDefinitionKey(),
                bpmnEntity.getProcessInstanceId());
        List<String> afterTaskKeys = afterTasks.stream().map(Task::getTaskDefinitionKey).collect(Collectors.toList());
        activityLogService.addLog(this.getGenericClassName(0), bpmnEntity.getId(), this.getCurrentUser(), remarks, "CREATED", String.join(", ", afterTaskKeys));
        
         /*
            #5 Send notification
         */
        
        this.sendStartNotification(entity, variable, dto);
        
        HashMap<String, Object> map = new HashMap<>();
        map.put("processInstanceId", processInstance.getProcessInstanceId());
        map.put("object", entity);
        return map;
    }

    public E draft(E entity, D dto) throws AlurKerjaException {
        BpmnEntity bpmnEntity = (BpmnEntity) entity;
        this.update(entity, dto);
        activityLogService.addLog(this.getGenericClassName(0), bpmnEntity.getId(), this.getCurrentUser(), null, "", "DRAFT");
        return entity;
    }

    public Map<String, Object> getSubprocessTasks(BpmnEntity  bpmnEntity){
        Map<String, Object> latestTasks = new HashMap<>();
        List<Task> tasks = this.camundaProcessService.getTaskByProcessInstanceId(bpmnEntity.getClass().getSimpleName(), bpmnEntity.getProcessInstanceId());
        if (tasks == null || tasks.isEmpty()){
            List<ActivityLog> activityLogs = this.getLogs((BpmnEntity) bpmnEntity);
            if(!CollectionUtils.isEmpty(activityLogs)) {
                activityLogs.sort(Comparator.comparing(CrudEntity::getCreatedDate).reversed());
                latestTasks.put("ENDED", activityLogs.get(0).getStatusAfter());
            }
            else {
                return null;
            }
        }
        for (Task task : tasks) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("label", task.getName());
            latestTasks.put(task.getTaskDefinitionKey(), map);
        }
        return latestTasks;
    }

    public Map<String, Object> getLatestTasks(E entity) {
        BpmnEntity bpmnEntity = (BpmnEntity) entity;
        Map<String, Object> latestTasks = new HashMap<>();

        List<Task> tasks = this.camundaProcessService.getTaskByProcessInstanceId(this.getProcessDefinitionKey(), bpmnEntity.getProcessInstanceId());
        if (tasks == null || tasks.isEmpty()){
            if(bpmnEntity.subProcessEntity() != null && bpmnEntity.secondarySubProcessEntity() == null){
                Map<String, Object> subProcessTasks = this.getSubprocessTasks(bpmnEntity.subProcessEntity());
                if(subProcessTasks != null){
                    return subProcessTasks;
                }
            }
            if (bpmnEntity.subProcessEntity() != null && bpmnEntity.secondarySubProcessEntity() != null){
                Map<String, Object> secondarySubProcessTasks = this.getSubprocessTasks(bpmnEntity.secondarySubProcessEntity());
                if(secondarySubProcessTasks != null){
                    return secondarySubProcessTasks;
                }
            }

            List<ActivityLog> activityLogs = this.getLogs((BpmnEntity) entity);
            if(!CollectionUtils.isEmpty(activityLogs)) {
                activityLogs.sort(Comparator.comparing(CrudEntity::getCreatedDate).reversed());
                latestTasks.put("ENDED", activityLogs.get(0).getStatusAfter());
            }
        } else{
            for (Task task : tasks) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", task.getId());
                map.put("label", task.getName());
                latestTasks.put(task.getTaskDefinitionKey(), map);
            }

        }
        return latestTasks;
    }

    public Map<String, Object> getVariables(List<Task> tasks) {
        return this.camundaProcessService.getVariables(tasks);
    }

    public Map<String, Object> getVariables(Task task){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine.getRuntimeService().getVariables(task.getExecutionId());
    }
    public boolean submit(String taskDefinitionKey, UUID id, String decision, V variable, D dto) {
        return this.submit(taskDefinitionKey, id, decision, variable, dto, null);
    }

    public boolean submit(String taskDefinitionKey, String taskId, UUID id, String decision, V variable, D dto,  String remarks) {
        Optional<E> optionalE = this.get(id);
        if (optionalE.isPresent()) {
            BpmnEntity bpmnEntity = (BpmnEntity) optionalE.get();

            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery()
                    .processDefinitionKey(this.getProcessDefinitionKey())
                    .taskDefinitionKey(taskDefinitionKey)
                    .processInstanceId(bpmnEntity.getProcessInstanceId())
                    .taskId(taskId)
                    .active()
                    .singleResult();
            if (task != null) {
                if(this.isValidAssignee(optionalE.get(), task)){
                    throw new BadUserRequestException("Invalid Assignee");
                }
                E entity = optionalE.get();

                this.addLogAfterSubmit(entity, decision, task, remarks);
                this.sendSubmitNotification(taskId, decision, entity, variable, dto);
                return true;
            }
            else{
                throw new BadUserRequestException("Invalid Task");
            }
        }
        return false;
    }

    private void addLogAfterSubmit(E entity, String decision, Task task, String remarks) {
        BpmnEntity bpmnEntity = (BpmnEntity) entity;

        Map<String, Object> afterTasks = this.getLatestTasks(entity);
        if((afterTasks == null || afterTasks.size() < 1) && bpmnEntity.subProcessEntity() != null){
            afterTasks = this.getSubprocessTasks(bpmnEntity.subProcessEntity());
        }

        Set<String> afterTaskKeys = afterTasks.keySet();

        String ended;
        if (decision != null && !ObjectUtils.isEmpty(decision)){
            ended = "ENDED - "+decision;
        } else{
            ended = "ENDED";
        }

        activityLogService.addLog(
                this.getGenericClassName(0),
                bpmnEntity.getId(), this.getCurrentUser(), remarks,
                task.getTaskDefinitionKey(),
                (afterTaskKeys.size()>0 && !afterTaskKeys.contains("ENDED"))? String.join(", ", afterTaskKeys) : ended);
    }

    public boolean isValidAssignee(E e, Task task) {
        return true;
    }

    public boolean submit(String taskDefinitionKey, UUID id, String decision, V variable, D dto,  String remarks) {
        Optional<E> optionalE = this.get(id);
        if (optionalE.isPresent()) {
            BpmnEntity bpmnEntity = (BpmnEntity) optionalE.get();
            if (this.camundaProcessService.isTaskActive(this.getProcessDefinitionKey(), taskDefinitionKey, bpmnEntity.getProcessInstanceId())) {
                List<Task> beforeTasks = this.camundaProcessService.getTask(this.getProcessDefinitionKey(), taskDefinitionKey, bpmnEntity.getProcessInstanceId());

                List<Task> validTasks = beforeTasks.stream().filter(t ->
                        t.getAssignee() == null || this.getCurrentUser() == null ||
                        t.getAssignee().equals(this.getCurrentUser())).collect(Collectors.toList());
                Task beforeTask = null;

                if(validTasks.size()>0){
                    beforeTask = validTasks.get(0);
                }
                else {
                    throw new BadUserRequestException("Invalid Assignee");
                }

                E entity = optionalE.get();
                Map<String, Object> objectHashMap = variable != null ? ((BaseVariable) variable).asMap() : new HashMap<>();
                objectHashMap.put("decision", decision);

                BaseDto<E, D> entityFromDto = (BaseDto) dto;
                entityFromDto.copyFromDto(entity);
                this.update(entity);
                this.camundaProcessService.completeTask(beforeTask.getId(), objectHashMap);
                this.addLogAfterSubmit(entity, decision, beforeTask, remarks);
                this.sendSubmitNotification(taskDefinitionKey, decision, entity, variable, dto);
                return true;
            }
            else{
                throw new BadUserRequestException("Invalid Task");
            }
        }
        return false;
    }

    public List<ActivityLog> getLogs(BpmnEntity bpmnEntity) {
        List<ActivityLog> logs = this.activityLogService.allLogs(bpmnEntity.getClass().getName(), bpmnEntity.getId());
        if(bpmnEntity.subProcessEntity() != null){
            logs.addAll(this.getLogs(bpmnEntity.subProcessEntity()));
        }
        if (bpmnEntity.secondarySubProcessEntity() != null){
            logs.addAll(this.getLogs(bpmnEntity.secondarySubProcessEntity()));
        }
        return logs.stream().sorted(Comparator.comparing(ActivityLog::getCreatedDate).reversed()).collect(Collectors.toList());
    }

    public List<ActivityLog> getDistinctLogs(E e) {
//        BpmnEntity bpmnEntity = (BpmnEntity) e;
//        return this.activityLogService.distinctLogs(this.getGenericClassName(0), bpmnEntity.getId());
        /*
        TODO : make it distinct
         */
        return this.getLogs((BpmnEntity) e);
    }

    public Page<E> findAll(List<String> processInstanceIds, D dto, Pageable pageable){
        Page<E> data = this.jpaRepository.findAll(this.instantiateSpecification(processInstanceIds, dto), pageable);

        /*
        TODO : set tasks for each entity
         */
        List<E> entities = data.getContent();
        for(E entity : entities){
            BpmnEntity bpmnEntity = (BpmnEntity) entity;
            bpmnEntity.setTasks(this.getLatestTasks(entity));
        }
        Page<E> newData = new PageImpl<>(entities, pageable, data.getTotalElements());
        return newData;
    }



    protected abstract void sendStartNotification(E entity, V variable, D dto);
    protected abstract void sendSubmitNotification(String taskDefinitionKey, String decision, E entity, V variable, D dto);

    public InputStream getBpmnXml() {
        return this.getBpmnXml(this.getProcessDefinitionKey());
    }
    public InputStream getBpmnXml(String processDefinitionKey) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Logger.getLogger(getClass().getName()).info(processDefinitionKey);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion().singleResult();

        return repositoryService.getProcessModel(processDefinition.getId());
    }

    public InputStream getBpmnXml(BpmnEntity entity) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(entity.getProcessInstanceId()).singleResult();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processInstance.getProcessDefinitionId())
                .singleResult();
        return repositoryService.getProcessModel(processDefinition.getId());
    }
    public List<String> getTaskDefinitionKeys(String processDefinitionKey){
        InputStream inputStream = this.getBpmnXml(processDefinitionKey);
        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(inputStream);
        ModelElementType taskType = bpmnModelInstance.getModel().getType(UserTask.class);
        Collection<ModelElementInstance> taskInstances = bpmnModelInstance.getModelElementsByType(taskType);
        List<String> taskDefinitionkeys = new ArrayList<>();
        for(ModelElementInstance x : taskInstances){
            taskDefinitionkeys.add(x.getAttributeValue("id"));
        }
        return taskDefinitionkeys;
    }
    public Map<String, List<String>> getSubProcessTaskDefinitionKeys(){
        return this.getSubProcessTaskDefinitionKeys(this.getProcessDefinitionKey());
    }
    public Map<String, List<String>> getSubProcessTaskDefinitionKeys(String processDefinitionKey){
        List<String> subprocessess = this.getSubProcesess(processDefinitionKey);
        if(subprocessess != null && subprocessess.size()>0){
            Map<String, List<String>>  taskDefinitionkeys = new HashMap<>();
            for(String subprocess : subprocessess){
                taskDefinitionkeys.put(subprocess, this.getTaskDefinitionKeys(subprocess));
            }
            return taskDefinitionkeys;
        }
        return null;
    }
    public List<String> getTaskDefinitionKeys(){
        return this.getTaskDefinitionKeys(this.getProcessDefinitionKey());
    }
    public List<String> getSubProcesess(){
        return this.getSubProcesess(this.getProcessDefinitionKey());
    }

    public List<String> getSubProcesess(String processDefinitionKey){
        InputStream inputStream = this.getBpmnXml(processDefinitionKey);
        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(inputStream);
        ModelElementType taskType = bpmnModelInstance.getModel().getType(CallActivity.class);
        Collection<ModelElementInstance> taskInstances = bpmnModelInstance.getModelElementsByType(taskType);
        List<String> taskDefinitionkeys = new ArrayList<>();
        for(ModelElementInstance x : taskInstances){
            taskDefinitionkeys.add(x.getAttributeValue("calledElement"));
        }
        return taskDefinitionkeys;
    }

    public Map<String, Object> getStatistic(String[] businessKeys) {
        HashMap<String, Object> result = new HashMap<>();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = processEngine.getTaskService();
        result.put("active", runtimeService.createProcessInstanceQuery().processDefinitionKey(this.getProcessDefinitionKey()).active().count());
        HashMap<String, Long> taskStats = new HashMap<>();
        for(String taskDefinitionKey : this.getTaskDefinitionKeys()){
            if (businessKeys == null)
                taskStats.put(taskDefinitionKey, taskService.createTaskQuery().processDefinitionKey(this.getProcessDefinitionKey()).taskDefinitionKey(taskDefinitionKey).count());
            else
                taskStats.put(taskDefinitionKey, taskService.createTaskQuery().processDefinitionKey(this.getProcessDefinitionKey()).taskDefinitionKey(taskDefinitionKey).processInstanceBusinessKeyIn(businessKeys).count());
        }
        result.put("tasks", taskStats);

        Map<String, List<String>> subProcessTaskDefinitionKeys = this.getSubProcessTaskDefinitionKeys();
        Map<String, Object> subprocessStats = null;
        if(subProcessTaskDefinitionKeys != null) {
            subprocessStats = new HashMap<>();
            for (Map.Entry<String, List<String>> sub : subProcessTaskDefinitionKeys.entrySet()) {
                Map<String, Long> subTaskStats = new HashMap<>();
                for(String task : sub.getValue()){
                    subTaskStats.put(task, taskService.createTaskQuery().processDefinitionKey(sub.getKey()).taskDefinitionKey(task).count());
                }
                subprocessStats.put(sub.getKey(), subTaskStats);
            }

        }
        result.put("subprocess", subprocessStats);
        return result;
    }

    public V instantiateVariable() {
        try {
            Class<?> clazz = Class.forName(((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[2]).getTypeName());
            Constructor<?> cons = clazz.getConstructor();
            return (V) cons.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, Object> availableDecision(String taskDefinitionKey) {
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<String> decisions = new ArrayList<>();
        InputStream inputStream = this.getBpmnXml();
        BpmnModelInstance bpmnModelInstance = Bpmn.readModelFromStream(inputStream);
        ModelElementType taskType = bpmnModelInstance.getModel().getType(SequenceFlow.class);
        Collection<ModelElementInstance> sequenceFlows = bpmnModelInstance.getModelElementsByType(taskType);
        for(ModelElementInstance flow : sequenceFlows){
            if(flow.getAttributeValue("id").contains(taskDefinitionKey + ".")) {
                String id = flow.getAttributeValue("id");
                String[] tmp = id.split("\\.");
                decisions.add(tmp[1]);
            }
        }
        result.put("exclusive", decisions);
        return result;
    }

    public Page<D> findAllAsDto(List<String> processInstanceId, D dto, Pageable pageable){
        Page<E> entities = this.findAll(processInstanceId, dto, pageable);
        List<BpmnEntity> bpmnEntities = entities.getContent().stream().map(e -> (BpmnEntity) e).collect(Collectors.toList());
        Map<UUID, Map<String, Object>> latestTasks = this.getLatestTasks(bpmnEntities);
        List<D> dtos = new ArrayList<>();
        for(E entity : entities.getContent()){
            D dtoInstance = this.instantiateDto();
            BpmnEntity bpmnEntity = (BpmnEntity) entity;
            if(bpmnEntity.subProcessEntity() != null){

            }
            bpmnEntity.setTasks(latestTasks.get(bpmnEntity.getId()));
            BaseDto<E, D> baseDto = (BaseDto<E, D>) dtoInstance;
            dtos.add(baseDto.toDto(entity));
        }

        Page<D> page = new PageImpl(dtos, pageable, dtos.size());
        return page;
    }

    @Override
    public Page<D> findAllAsDto(D keyword, Pageable pageable) {
        Page<E> entities = this.jpaRepository.findAll(this.instantiateSpecification(keyword), pageable);
        List<BpmnEntity> bpmnEntities = entities.getContent().stream().map(e -> (BpmnEntity) e).collect(Collectors.toList());
        Map<UUID, Map<String, Object>> tasks = this.getLatestTasks(bpmnEntities);

        List<D> dtos = new ArrayList<>();
        for(E entity : entities){
            BpmnEntity bpmnEntity = (BpmnEntity) entity;
            bpmnEntity.setTasks(tasks.get(bpmnEntity.getId()));

            D dto = this.instantiateDto();
            BaseDto<E, D> baseDto = (BaseDto<E, D>) dto;
            dtos.add(baseDto.toDto(entity));
        }
        Page<D> page = new PageImpl(dtos, pageable, entities.getTotalElements());
        return page;
    }

    @Override
    public Page<D> findAllAsDto(Pageable pageable) {
        Page<E> entities = this.jpaRepository.findAll(pageable);
        List<BpmnEntity> bpmnEntities = entities.getContent().stream().map(e -> (BpmnEntity) e).collect(Collectors.toList());
        Map<UUID, Map<String, Object>> tasks = this.getLatestTasks(bpmnEntities);
        List<D> dtos = new ArrayList<>();
        for(E entity : entities){
            BpmnEntity bpmnEntity = (BpmnEntity) entity;
            bpmnEntity.setTasks(tasks.get(bpmnEntity.getId()));
            D dto = this.instantiateDto();
            BaseDto<E, D> baseDto = (BaseDto<E, D>) dto;
            dtos.add(baseDto.toDto(entity));
        }
        Page<D> page = new PageImpl(dtos, pageable, entities.getTotalElements());
        return page;
    }

    public Map<UUID, Map<String, Object>> getLatestTasks(List<BpmnEntity> bpmnEntities) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();

        String[] businessKeys = new String[bpmnEntities.size()];
        int i = 0;
        for(BpmnEntity entity : bpmnEntities){
            businessKeys[i++] = String.valueOf(entity.getBusinessKey());
        }
        List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKeyIn(businessKeys).list();
        List<UUID> uuidList = bpmnEntities.stream().map(CrudEntity::getId).collect(Collectors.toList());
        List<ActivityLog> logs = activityLogService.getLatestLogs(this.getGenericClassName(0), uuidList);

        Map<UUID, Map<String, Object>> result = new HashMap<>();
        for(BpmnEntity entity : bpmnEntities){
            List<Task> entityTasks = tasks.stream().filter(p -> p.getProcessInstanceId().equals(entity.getProcessInstanceId())).collect(Collectors.toList());
            Map<String, Object> tasksMap = new HashMap<>();
            if(!entityTasks.isEmpty() ){
                for(Task t : entityTasks){
                    Map<String, Object> map = new HashMap();
                    map.put("id", t.getId());
                    map.put("label", t.getName());
                    tasksMap.put(t.getTaskDefinitionKey(), map);
                }

            }
            else{
                if(entity.subProcessEntity() != null && entity.secondarySubProcessEntity() == null){
                    tasksMap = this.getSubprocessTasks(entity.subProcessEntity());
                } else if (entity.secondarySubProcessEntity() != null){
                    tasksMap = this.getSubprocessTasks(entity.secondarySubProcessEntity());
                }
                else{
                    Optional<ActivityLog> optionalActivityLog = logs.stream().filter(p -> p.getEntityId().equals(entity.getId())).findFirst();
                    if(optionalActivityLog.isPresent()){
                        ActivityLog activityLog = optionalActivityLog.get();
                        Map<String, Object> map = new HashMap();
                        map.put("id", activityLog.getEntityId());
                        map.put("label", activityLog.getStatusAfter());
                        tasksMap.put("ENDED", map);
                    }
                }
            }
            result.put(entity.getId(), tasksMap);
        }
        return result;
    }

    @Override
    public List<D> findAllAsDto() {
        List<E> entities = this.jpaRepository.findAll();

        List<BpmnEntity> bpmnEntities = entities.stream().map(e -> (BpmnEntity) e).collect(Collectors.toList());
        Map<UUID, Map<String, Object>> tasks = this.getLatestTasks(bpmnEntities);
        List<D> dtos = new ArrayList<>();
        for(E entity : entities){
            BpmnEntity bpmnEntity = (BpmnEntity) entity;
            bpmnEntity.setTasks(tasks.get(bpmnEntity.getId()));
            D dto = this.instantiateDto();
            BaseDto<E, D> baseDto = (BaseDto<E, D>) dto;
            dtos.add(baseDto.toDto(entity));
        }
        return dtos;
    }
}
