package com.proyekakhir.manajemendokumen.core.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "ak_activity_log")
public class ActivityLog extends CrudEntity{
    String className;
    UUID entityId;
    String actBy;
    String remarks;
    String statusBefore;
    String statusAfter;
}
