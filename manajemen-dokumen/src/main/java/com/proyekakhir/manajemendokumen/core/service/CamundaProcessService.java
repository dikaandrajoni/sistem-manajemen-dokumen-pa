package com.proyekakhir.manajemendokumen.core.service;
import org.apache.commons.collections4.ListUtils;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
@Transactional
public class CamundaProcessService {

    public CamundaProcessService() {

    }

    public void getBpmnXml(String processDefinitionKey){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        RepositoryService repositoryService = processEngine.getRepositoryService();
    }

    public ProcessInstance startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        return runtimeService.createProcessInstanceByKey(processDefinitionKey)
                .setVariables(variables)
                .businessKey(businessKey)
                .executeWithVariablesInReturn();
    }

    public List<Task> getActiveTasksByProcessDefKey(String processDefKey) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine.getTaskService().createTaskQuery()
                .processDefinitionKey(processDefKey)
                .active().list();
    }

    public List<Task> getActiveTasks(String processDefKey, String taskDefKey) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine.getTaskService().createTaskQuery()
                .processDefinitionKey(processDefKey)
                .taskDefinitionKey(taskDefKey)
                .active().list();
    }

    public List<Task> getActiveTasksByName(String processDefKey, String taskName) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine.getTaskService().createTaskQuery()
                .processDefinitionKey(processDefKey)
                .taskName(taskName)
                .active().list();
    }

    public List<String> getActiveTaskIds(String processDefKey, String taskDefKey) {
        return getActiveTasks(processDefKey, taskDefKey).stream()
                .map(Task::getProcessInstanceId)
                .collect(Collectors.toList());
    }

    public List<String> getActiveTaskIdsByName(String processDefKey, String taskName) {
        return getActiveTasksByName(processDefKey, taskName).stream()
                .map(Task::getProcessInstanceId)
                .collect(Collectors.toList());
    }

    public List<Task> getTask(String processDefKey, String taskDefKey, String processInstanceId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Logger.getLogger(getClass().getName()).info("Process Definition Key" + processDefKey);
        Logger.getLogger(getClass().getName()).info(taskDefKey);
        Logger.getLogger(getClass().getName()).info(processInstanceId);
        return processEngine.getTaskService().createTaskQuery().processDefinitionKey(processDefKey)
                .taskDefinitionKey(taskDefKey).processInstanceId(processInstanceId).active().list();
    }


    public List<Task> getTaskByProcessInstanceId(String processDefKey, String processInstanceId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine.getTaskService().createTaskQuery().processDefinitionKey(processDefKey)
                .processInstanceId(processInstanceId).active().list();
    }

    public List<Task> getTaskByAssignee(String processDefKey, String assignee) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine.getTaskService().createTaskQuery().processDefinitionKey(processDefKey)
                .taskAssignee(assignee).active().list();
    }

    public List<Task> getTaskByAssigneeAndTaskDefintionKeyLike(String processDefKey, String assignee,String taskDefinitionKeys) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine.getTaskService().createTaskQuery().processDefinitionKey(processDefKey)
                .taskAssignee(assignee).taskDefinitionKeyLike(taskDefinitionKeys).active().list();
    }

    public Execution getExecution(String processDefKey, String activityId, String processInstanceId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return processEngine.getRuntimeService().createExecutionQuery().processDefinitionKey(processDefKey)
                .processInstanceId(processInstanceId).activityId(activityId).singleResult();
    }

    public void completeTask(String taskId, Map<String, Object> variable) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        if (variable != null) {
            processEngine.getTaskService().complete(taskId, variable);
        }
        if (variable == null) {
            processEngine.getTaskService().complete(taskId);
        }
    }

    public Map<String, Object> getVariables(List<Task> tasks){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HashMap<String, Object> map = new HashMap<>();
        for(Task task : tasks){
            map.put(task.getTaskDefinitionKey(), processEngine.getRuntimeService().getVariables(task.getExecutionId()));
        }

        return map;
    }

    public void completeTask(String taskId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getTaskService().complete(taskId);
    }

    public void completeRuntime(String exId, Map<String, Object> variables) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRuntimeService().signal(exId, variables);
    }

    public void completeRuntime(String exId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRuntimeService().signal(exId);
    }

    public void completeTask(String processDefKey, String taskDefKey, String processInstanceId, Map<String, Object> variable) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Task task = processEngine.getTaskService().createTaskQuery().processDefinitionKey(processDefKey)
                .taskDefinitionKey(taskDefKey).processInstanceId(processInstanceId).active().singleResult();
        if (variable != null) {
            processEngine.getTaskService().complete(task.getId(), variable);
        }
        if (variable == null) {
            processEngine.getTaskService().complete(task.getId());
        }
    }

    public boolean isTaskActive(String processDefKey, String taskDefKey, String processInstanceId) {
        try {
            Task task = this.getTask(processDefKey, taskDefKey, processInstanceId).get(0);
            return (task != null);
        }
        catch (Exception ex){
            return false;
        }
    }

    public void terminateProcessInstance(String processInstanceId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, "delete");
    }


// Added
    public ProcessInstanceQuery getInstanceQuery(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        return runtimeService.createProcessInstanceQuery().active();
    }



    public ProcessInstance getProcessInstanceByBusinessKeyAndDefinitionKey( String busniessDefinitionKey ,String businessKey) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();

        List<ProcessInstance>  listProcess =  runtimeService.createProcessInstanceQuery().processDefinitionKey(busniessDefinitionKey).processInstanceBusinessKey(businessKey).list();
        return listProcess.isEmpty() ?  null : listProcess.get(listProcess.size() -1 );

    }

    public List<String> getTaskDefinitionKey(String process,  String id) {
        ProcessInstance processInstance = getProcessInstanceByBusinessKeyAndDefinitionKey(process, id);
        if(processInstance != null){
            List<Task> tasks = getTaskByProcessInstanceId(process, processInstance.getProcessInstanceId());
            return tasks.stream().map(Task::getTaskDefinitionKey).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }



    public List<Integer> getTaskCount(String processDefinitionKey, String activityIds , String bIds) {
        List<Integer> cn = new ArrayList<>();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        int perPage = 500;

        String[] pagedBids =  bIds.split(",");
        System.out.println();
        for (String activity : activityIds.split(","))
        {
            Long jumlah = 0L;
            List<List<String>> listId = ListUtils.partition(Arrays.stream(pagedBids).collect(Collectors.toList()), perPage );
            for (List<String> data :  listId) {
                jumlah = jumlah + taskService.createTaskQuery().taskDefinitionKey(activity).processDefinitionKey(processDefinitionKey).processInstanceBusinessKeyIn(String.join(",", data).split(",")).count();
            }
            cn.add(Integer.valueOf(jumlah.toString()));
        }

        return cn;
    }


    public boolean isValidAssignee(String taskId, String assignee){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        return taskService.createTaskQuery().taskId(taskId).taskAssignee(assignee).active().count() > 0;
    }
}
