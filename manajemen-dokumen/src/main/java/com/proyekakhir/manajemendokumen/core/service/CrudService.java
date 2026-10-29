package com.proyekakhir.manajemendokumen.core.service;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.core.entity.CrudEntity;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import com.proyekakhir.manajemendokumen.core.repository.search.GenericSearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public abstract class CrudService<E, D, R> {
    protected CrudRepository<E> jpaRepository;

    protected CrudService(R simpleJpaRepository) {
        this.jpaRepository = (CrudRepository<E>) simpleJpaRepository;
    }

    public E createFromDto(D dto) throws AlurKerjaException {
        BaseDto<E, D> dtoable = (BaseDto) dto;
        E entity = dtoable.fromDto();
        CrudEntity crudEntity = (CrudEntity) entity;
        crudEntity.setCreatedDate(new Date());
        crudEntity.setCreatedBy(this.getCurrentUser());
        this.jpaRepository.save(entity);
        return entity;
    }

    public D createFromDtoAsDto(D dto) throws AlurKerjaException {
        E entity = this.createFromDto(dto);
        BaseDto<E, D> baseDto = (BaseDto<E, D>) dto;
        return baseDto.toDto(entity);
    }

    public E create(E entity) {
        CrudEntity crudEntity = (CrudEntity) entity;
        crudEntity.setCreatedDate(new Date());
        crudEntity.setCreatedBy(this.getCurrentUser());
        this.jpaRepository.save(entity);
        return entity;
    }

    public List<E> findAll() {
        return this.jpaRepository.findAll();
    }

    public Page<E> findAll(Pageable pageable) {
        return this.jpaRepository.findAll(pageable);
    }

    public Page<E> findAll(D keyword, Pageable pageable) {
        return this.jpaRepository.findAll(this.instantiateSpecification(keyword), pageable);
    }

    public List<D> findAllAsDto() {
        List<E> entities = this.jpaRepository.findAll();
        List<D> dtos = new ArrayList<>();
        for(E entity : entities){
            D dto = this.instantiateDto();
            BaseDto<E, D> baseDto = (BaseDto<E, D>) dto;
            dtos.add(baseDto.toDto(entity));
        }
        return dtos;
    }

    public Page<D> findAllAsDto(Pageable pageable) {
        Page<E> entities = this.jpaRepository.findAll(pageable);
        List<D> dtos = new ArrayList<>();
        for(E entity : entities){
            D dto = this.instantiateDto();
            BaseDto<E, D> baseDto = (BaseDto<E, D>) dto;
            dtos.add(baseDto.toDto(entity));
        }
        Page<D> page = new PageImpl(dtos, pageable, entities.getTotalElements());
        return page;
    }

    public Page<D> findAllAsDto(D keyword, Pageable pageable) {
        Page<E> entities = this.jpaRepository.findAll(this.instantiateSpecification(keyword), pageable);
        List<D> dtos = new ArrayList<>();
        for(E entity : entities){
            D dto = this.instantiateDto();
            BaseDto<E, D> baseDto = (BaseDto<E, D>) dto;
            dtos.add(baseDto.toDto(entity));
        }
        Page<D> page = new PageImpl(dtos, pageable, entities.getTotalElements());
        return page;
    }

    public Optional<E> get(UUID id) {
        return jpaRepository.findById(id);
    }

    public E update(E entity) {
        CrudEntity crudEntity = (CrudEntity) entity;
        crudEntity.setUpdatedDate(new Date());
        crudEntity.setUpdatedBy(this.getCurrentUser());
        return this.jpaRepository.save(entity);
    }

    public E update(E entity, D dto) throws AlurKerjaException {
        BaseDto<E, D> baseDto = (BaseDto<E, D>) dto;
        CrudEntity crudEntity = (CrudEntity) entity;
        baseDto.copyFromDto(entity);
        crudEntity.setUpdatedDate(new Date());
        crudEntity.setUpdatedBy(this.getCurrentUser());
        return this.jpaRepository.save(entity);
    }

    public void delete(E e)throws AlurKerjaException {
        this.jpaRepository.delete(e);
    }

    public String getGenericClassName(int index) {
        return ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[index]).getTypeName();
    }

    public String getSimpleClassName(int index) {
        return ((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[index]).getSimpleName();
    }

    public D instantiateDto() {
        try {
            Class<?> clazz = Class.forName(((Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getTypeName());
            Constructor<?> cons = clazz.getConstructor();
            return (D) cons.newInstance();
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

    public Specification<E> instantiateSpecification(D dto) {
        return new GenericSearchSpecification<>(dto);
    }
    public Specification<E> instantiateSpecification(List<String> processInstanceIds, D dto) {
        return new GenericSearchSpecification<>( processInstanceIds, dto);
    }

    public abstract String getCurrentUser();
    public abstract String getRole();
}
