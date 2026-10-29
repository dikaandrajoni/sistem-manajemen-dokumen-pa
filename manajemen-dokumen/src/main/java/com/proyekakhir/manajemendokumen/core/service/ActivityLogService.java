package com.proyekakhir.manajemendokumen.core.service;

import com.proyekakhir.manajemendokumen.core.entity.ActivityLog;
import com.proyekakhir.manajemendokumen.core.repository.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityLogService {
    @Autowired
    ActivityLogRepository activityLogRepository;
    public void addLog(String className, UUID entityId, String actBy, String remarks, String statusBefore, String statusAfter ){
        ActivityLog activityLog = new ActivityLog();
        activityLog.setClassName(className);
        activityLog.setEntityId(entityId);
        activityLog.setActBy(actBy);
        activityLog.setCreatedDate(new Date());
        activityLog.setRemarks(remarks);
        activityLog.setStatusBefore(statusBefore);
        activityLog.setStatusAfter(statusAfter);
        this.activityLogRepository.save(activityLog);
    }

    public List<ActivityLog> allLogs(String className, UUID entityId){
        return activityLogRepository.findAll(className, entityId);
    }

    public List<ActivityLog> distinctLogs(String className, UUID entityId){
        return activityLogRepository.distinctLogs(className, entityId);
    }

    public List<ActivityLog> getLatestLogs(String className, List<UUID> entityIds){
        List<ActivityLog> logs = activityLogRepository.latestLogs(className, entityIds);
        return logs;
    }

    public List<ActivityLog> getLogsByClassNameAndStatusAfter(String className,String statusAfter){
        return activityLogRepository.findAllByClassNameAndStatusAfter(className, statusAfter);
    }
}
