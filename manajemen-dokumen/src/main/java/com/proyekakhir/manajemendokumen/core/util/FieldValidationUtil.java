package com.proyekakhir.manajemendokumen.core.util;

import com.proyekakhir.manajemendokumen.core.anotation.ValidDto;
import org.camunda.bpm.engine.BadUserRequestException;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.logging.Logger;

public class FieldValidationUtil<D> {
    public void validate(String taskDefinitionKey, D dto){
        if (Objects.isNull(dto)) {
            throw new BadUserRequestException("Requestbody is null");
        }

        Class<?> clazz = dto.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ValidDto.class)) {
                Logger.getLogger(getClass().getName()).info(field.getName() + " has annotation");
                String[] taskDefinitionKeys = field.getAnnotation(ValidDto.class).requiredIn();
                for(String taskDef : taskDefinitionKeys){
                    Logger.getLogger(getClass().getName()).info(taskDef);
                }
                Logger.getLogger(getClass().getName()).info(taskDefinitionKeys.toString());
            }
            else{
                Logger.getLogger(getClass().getName()).info(field.getName() + " doesn't have annotation");
            }
        }
    }
}
