package com.proyekakhir.manajemendokumen.user;

import com.proyekakhir.manajemendokumen.core.repository.search.BaseSearchSpecification;
import com.proyekakhir.manajemendokumen.core.repository.search.SearchCriteria;
import com.proyekakhir.manajemendokumen.core.repository.search.SearchOperation;
import org.thymeleaf.util.StringUtils;

public class UserSpecifications extends BaseSearchSpecification<User, UserDto> {

    protected UserSpecifications(UserDto dto) {
        super(dto);
    }

    public static UserSpecifications create(UserDto dto){
        UserSpecifications dataSpecification = new UserSpecifications(dto);
        dataSpecification.buildSpecification(dto);
        return dataSpecification;
    }

    @Override
    public void buildSpecification(UserDto dto) {
        if (!StringUtils.isEmpty(dto.getUsername())){
            addCriteria(new SearchCriteria("username", dto.getUsername(), SearchOperation.MATCH));
        }

        if (!StringUtils.isEmpty(dto.getEmail())){
            addCriteria(new SearchCriteria("email", dto.getEmail(), SearchOperation.EQUAL));
        }

    }
}
