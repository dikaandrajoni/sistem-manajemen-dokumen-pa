package com.proyekakhir.manajemendokumen.core.repository.search;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseSearchSpecification<E, D> implements Specification<E> {
    protected List<String> processInstanceIds;


    @Transient
    protected List<SearchCriteria> searchCriteriaList = new ArrayList<>();

    protected BaseSearchSpecification(D dto) {
        buildSpecification(dto);
    }

    protected BaseSearchSpecification(List<String> processInstanceIds, D dto) {
        this.processInstanceIds = processInstanceIds;
        buildSpecification(dto);
    }

    @Override
    public Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        BasePredicate basePredicate = new BasePredicate();
        if(this.processInstanceIds != null){
            addCriteria(new SearchCriteria("processInstanceId", this.processInstanceIds, SearchOperation.IN));
        }
        List<Predicate> predicates = basePredicate.predicate(root, criteriaQuery, criteriaBuilder, searchCriteriaList);
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public void buildSpecification(D dto) {
        this.defaultSpecification(dto);
    }

    public void defaultSpecification(Object dto) {
        Class<?> clazzEntity = dto.getClass();
        for (Field field : clazzEntity.getDeclaredFields()) {
            Object value = this.getPropertyValue(field.getName(), dto);
            if(value != null) {
                if(field.getType().getName().contains("com.alurkerja") && field.getType().getDeclaredFields().length>0){
                    for(Field parentField : field.getType().getDeclaredFields()){
                        this.addCriteria(field.getName() + "." + parentField.getName(), this.getPropertyValue(field.getName() + "." + parentField.getName(), dto));
                    }
                }
                else{
                   this.addCriteria(field.getName(), value);
                }
            }
        }

    }

    public void addCriteria(SearchCriteria criteria) {
        this.searchCriteriaList.add(criteria);
    }

    public void addCriteria(String fieldName, Object value){
        String[] properties = fieldName.split("\\.");
        boolean isId = (properties.length == 1 && properties[0].equalsIgnoreCase("id")) || (properties.length == 2 && properties[1].equalsIgnoreCase("id"));
        if(value != null) {
            if (!isId && value.getClass().getSimpleName().equalsIgnoreCase("String")) {
                this.addCriteria(new SearchCriteria(fieldName, value, SearchOperation.MATCH));
            } else {
                this.addCriteria(new SearchCriteria(fieldName, value, SearchOperation.EQUAL));
            }
        }
    }


    public Object getPropertyValue(String property, Object dto) {
        String[] strLevels = property.split("\\.");
        try {
            Class<?> clazzDto = dto.getClass();
            Field fieldLevel0 = clazzDto.getDeclaredField(strLevels[0]);
            fieldLevel0.setAccessible(true);
            Object level0 = fieldLevel0.get(dto);
            if (strLevels.length == 1) {
                return level0;
            } else if (strLevels.length == 2 && level0 != null) {
                return this.getPropertyValue(strLevels[1], this.getPropertyValue(strLevels[0], dto));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;

    }
}
