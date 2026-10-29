package com.proyekakhir.manajemendokumen.role;

import com.proyekakhir.manajemendokumen.core.repository.search.BaseSearchSpecification;
import com.proyekakhir.manajemendokumen.core.repository.search.SearchCriteria;
import com.proyekakhir.manajemendokumen.core.repository.search.SearchOperation;

public class RoleSpecifications extends BaseSearchSpecification<Role, RoleDto> {
    protected RoleSpecifications(RoleDto dto) {
        super(dto);
    }

    public static RoleSpecifications create(RoleDto dto){
        RoleSpecifications specifications = new RoleSpecifications(dto);
        specifications.buildSpecification(dto);
        return specifications;
    }

    @Override
    public void buildSpecification(RoleDto dto) {
        if (dto.getName() != null){
            addCriteria(new SearchCriteria("name", dto.getName(), SearchOperation.MATCH));
        }
    }
}
