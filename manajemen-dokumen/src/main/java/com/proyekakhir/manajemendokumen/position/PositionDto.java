package com.proyekakhir.manajemendokumen.position;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionDto extends BaseDto<Position, PositionDto> {
    private String name;
    private String emailUser;
    private String namaUser;
    private String nipUser;
}
