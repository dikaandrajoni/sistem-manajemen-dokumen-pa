package com.proyekakhir.manajemendokumen.core.entity;

import com.proyekakhir.manajemendokumen.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class BaseVariable implements Serializable {
    protected String decision;
    public Map<String, Object> asMap(){
        HashMap<String, Object> asMap = new HashMap<>();

        Class<?> clazz = this.getClass();
        for(Field field : clazz.getDeclaredFields()){
            field.setAccessible(true);
            try {
                asMap.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return asMap;
    }

    public List<FormSpec> specifications() {

        Class<?> clazz = this.getClass();
        List<FormSpec> result = new ArrayList<>();

        for(Field field : clazz.getDeclaredFields()){
            FormSpec form = new FormSpec();
            form.setName(field.getName());
            form.setLabel(StringUtil.convertFieldToLabel(field.getName()));
            result.add(form);
        }
        return result;
    }
}
