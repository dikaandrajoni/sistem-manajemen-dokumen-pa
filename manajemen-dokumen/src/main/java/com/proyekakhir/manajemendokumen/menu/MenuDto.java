package com.proyekakhir.manajemendokumen.menu;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MenuDto extends BaseDto<Menu, MenuDto> {
    @NotNull
    private String name;
    @NotNull
    private String url;
}
