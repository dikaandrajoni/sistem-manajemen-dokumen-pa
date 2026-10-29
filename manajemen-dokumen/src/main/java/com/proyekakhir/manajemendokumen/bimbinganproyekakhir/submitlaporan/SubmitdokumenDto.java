package com.proyekakhir.manajemendokumen.bimbinganproyekakhir.submitlaporan;

import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitdokumenDto extends BaseDto<BimbinganProyekAkhir, SubmitdokumenDto> {
    private Integer bimbinganKe;
}
