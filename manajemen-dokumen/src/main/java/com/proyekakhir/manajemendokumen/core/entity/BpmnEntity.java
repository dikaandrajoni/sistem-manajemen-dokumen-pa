package com.proyekakhir.manajemendokumen.core.entity;

import lombok.Getter;
import lombok.Setter;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Map;

@MappedSuperclass
@Getter
@Setter
public abstract class BpmnEntity extends CrudEntity {
    private String processInstanceId;
    public String getBusinessKey(){
        return this.getId().toString();
    }

    @Transient
    private Map<String, Object> tasks;

    @Transient
    private int bpmnVersion;

    public void setTasks(Map<String, Object> tasks) {
        this.tasks = tasks;
    }

    public void getLatestLog(){
        
    }

    public Map<String, Object> getTasks(){
//        if(this.tasks != null){
            return this.tasks;
//        }

//        if(processInstanceId != null) {
//            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//            TaskService taskService = processEngine.getTaskService();
//            RuntimeService runtimeService = processEngine.getRuntimeService();
//            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(this.processInstanceId).singleResult();
//            if (processInstance == null)
//                return null;
//            List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().superProcessInstanceId(processInstance.getProcessInstanceId()).list();
//            processInstances.add(processInstance);
//            List<String> processInstanceIds = processInstances.stream().map(pi->pi.getProcessInstanceId()).collect(Collectors.toList());
//            List<Task> tasks = taskService.createTaskQuery().processInstanceIdIn(processInstanceIds.toArray(String[]::new)).list();
//
//
//            if (tasks != null) {
//                Map<String, Object> result = new HashMap<>();
//                for (Task task : tasks) {
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("id", task.getId());
//                    map.put("label", task.getName());
//                    result.put(task.getTaskDefinitionKey(), map);
//                }
//                return result;
//            }
//        }
//        return null;
    }

    public Integer getBpmnVersion(){
        if(processInstanceId != null) {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = processEngine.getRepositoryService();
            RuntimeService runtimeService = processEngine.getRuntimeService();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(this.processInstanceId).singleResult();
            if (processInstance == null)
                return null;
            return repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult().getVersion();
        }
        return null;
    }

    public <T extends BpmnEntity> T subProcessEntity(){
        return null;
    }

    public <T extends BpmnEntity> T secondarySubProcessEntity(){
        return null;
    }
}
