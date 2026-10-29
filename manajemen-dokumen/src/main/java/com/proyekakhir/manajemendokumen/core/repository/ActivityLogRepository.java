package com.proyekakhir.manajemendokumen.core.repository;

import com.proyekakhir.manajemendokumen.core.entity.ActivityLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityLogRepository extends CrudRepository<ActivityLog> {
    @Query(value = "select distinct on(status_after) status_after, id, status_before, class_name, remarks, act_by , created_date, updated_date,  entity_id,created_by,updated_by from ak_activity_log dbl " +
            "where entity_id = :id and class_name = :className " +
            "order by status_after , created_date desc", nativeQuery = true)
    List<ActivityLog> distinctLogs(@Param("className") String className, @Param("id") UUID id);

    @Query(value = "select distinct on(entity_id) entity_id, id, status_after, status_before, class_name, remarks, act_by , created_date, updated_date,created_by,updated_by from ak_activity_log dbl " +
            "where entity_id in :ids and class_name = :className " +
            "order by entity_id , created_date desc", nativeQuery = true)
    List<ActivityLog> latestLogs(@Param("className") String className, @Param("ids") List<UUID> ids);

    @Query(value = "select al from ActivityLog al where al.className = :className and al.entityId = :id order by al.createdDate desc ")
    List<ActivityLog> findAll(@Param("className") String className, @Param("id") UUID id);

    @Query(value = "select al from ActivityLog al where al.className = :className and al.statusAfter = :statusAfter order by al.createdDate desc ")
    List<ActivityLog> findAllByClassNameAndStatusAfter(@Param("className") String className,@Param("statusAfter") String statusAfter);
}
