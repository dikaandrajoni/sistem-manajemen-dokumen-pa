package com.proyekakhir.manajemendokumen.bimbinganproyekakhir.revisi;

import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevisiDto extends BaseDto<BimbinganProyekAkhir, RevisiDto> {
    private Integer bimbinganKe;
}
