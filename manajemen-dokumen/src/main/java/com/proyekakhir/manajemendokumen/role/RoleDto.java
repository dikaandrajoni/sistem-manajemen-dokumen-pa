package com.proyekakhir.manajemendokumen.role;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto extends BaseDto<Role, RoleDto> {
    private String name;
    private String description;
}
