package com.proyekakhir.manajemendokumen.core.entity;

import com.proyekakhir.manajemendokumen.core.anotation.FormReference;
import com.proyekakhir.manajemendokumen.core.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import javax.persistence.Lob;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

@Getter
@Setter
public abstract class BaseDto<E, D> implements Serializable {
    private UUID id;
    public void copyFromDto(E t){
        BeanUtils.copyProperties(this, t, getNullPropertyNames(this));
    }
    private Map<String, Object> tasks;

    public String[] getNullPropertyNames(Object source) {
        List<String> nullValuePropertyNames = new ArrayList<>();
        nullValuePropertyNames.add("id");
        for (Field f : source.getClass().getDeclaredFields()) {
            try {
                f.setAccessible(true);
                if (f.get(source) == null) {
                    nullValuePropertyNames.add(f.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return nullValuePropertyNames.toArray(new String[0]);
    }

    public E fromDto() {
        try {
            Class<?> clazz = Class.forName(((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getTypeName());
            Constructor<?> cons = clazz.getConstructor();
            E t = (E) cons.newInstance();
            BeanUtils.copyProperties(this, t);
            return t;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public D toDto(E entity) {
        BeanUtils.copyProperties(entity, this);
        return (D) this;
    }

    @SneakyThrows
    public List<FormSpec> specifications() throws ClassNotFoundException {
        Class<?> clazz = this.getClass();
        List<FormSpec> result = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            FormSpec form = new FormSpec();
            form.setName(field.getName());
            form.setLabel(StringUtil.convertFieldToLabel(field.getName()));
            String[] constraints = new String[field.getDeclaredAnnotations().length];
            form.setType(field.getType().getSimpleName());
            if (field.getDeclaredAnnotations().length > 0) {
                ArrayList<String> xx = new ArrayList<>();

                int i = 0;
                Map<String, Object> constraintsConv = new HashMap<>();
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    constraints[i++] = annotation.annotationType().getSimpleName();
                    for (Method method : annotation.annotationType().getDeclaredMethods()) {
                        Object value = method.invoke(annotation, (Object[]) null);
                        constraintsConv.putAll(this.javaxValidationConstraints(annotation.annotationType().getSimpleName(), method.getName(), value));
                    }
                    if (annotation.annotationType().getSimpleName().equals(FormReference.class.getSimpleName())) {
                        FormReference formReference = (FormReference) annotation;
                        HashMap<String, Object> metadata = new HashMap<>();
                        metadata.putAll(constraintsConv);
                        metadata.put("label", formReference.label());
                        if (formReference.url().length() > 0) {
                            metadata.put("url", formReference.url());
                        }
                        if (formReference.jsonValues().length() > 0) {
                            metadata.put("jsonValues", formReference.jsonValues());
                        }
                        metadata.put("key", formReference.key());
                        metadata.put("value", formReference.value());
                        form.setMetadata(metadata);
                        form.setType(formReference.type());
                    } else if (annotation.annotationType().getSimpleName().equals(Lob.class.getSimpleName())) {
                        form.setType("Textarea");
                    }
                }
            }


            form.setConstraints(constraints);
            form.setRequired(Arrays.stream(constraints).anyMatch("NotEmpty"::equals));
            result.add(form);
        }
        return result;
    }

    private Map<String, Object> javaxValidationConstraints(String validationKey, String key, Object value) {
        Map<String, Object> validationKeys = new HashMap<>();
        validationKeys.put("AssertFalse", "booleanValue=false");
        validationKeys.put("AssertTrue", "booleanValue=true");
        validationKeys.put("DecimalMax", "value");
        validationKeys.put("DecimalMin", "value");
        validationKeys.put("Digits", "integer,fraction");
        validationKeys.put("Email", "email=true");
        validationKeys.put("Future", "date=future");
        validationKeys.put("FutureOrPresent", "date=futureOrPresent");
        validationKeys.put("Max", "value");
        validationKeys.put("Min", "value");
        validationKeys.put("Negative", "negative=yes");
        validationKeys.put("NegativeOrZero", "negative=yesOrZero");
        validationKeys.put("NotBlank", "required=true");
        validationKeys.put("NotEmpty", "required=true");
        validationKeys.put("NotNull", "required=true");
        validationKeys.put("Null", "required=false");
        validationKeys.put("Past", "date=past");
        validationKeys.put("PastOrPresent", "date=pastOrPresent");
        validationKeys.put("Pattern", "regexp");
        validationKeys.put("Positive", "negative=no");
        validationKeys.put("PositiveOrZero", "negative=noOrZero");
        validationKeys.put("Size", "min,max");
        String[] commaVar = {"min", "max", "integer", "fraction"};
        Map<String, Object> result = new HashMap<>();
        if (validationKeys.containsKey(validationKey)) {
            String val = validationKeys.get(validationKey).toString();
            boolean isComma = val.contains(",");
            boolean isEq = val.contains("=");
            if ((isComma && Arrays.stream(commaVar).anyMatch(key::equals)) || key.equals("regexp")) {
                result.put(key, value);
            } else if(isEq) {
                String[] keyVal = val.split("=");
                result.put(keyVal[0], keyVal[1]);
                return result;
            } else if (key.equals("value")) {
                result.put(validationKey.toLowerCase(), value);
            }
        }
        return result;
    }

}
