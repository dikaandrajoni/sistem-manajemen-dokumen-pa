package com.proyekakhir.manajemendokumen.bimbinganproyekakhir.reviewpembimbing;

import com.proyekakhir.manajemendokumen.bimbinganproyekakhir.BimbinganProyekAkhir;
import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
public class ReviewpembimbingDto extends BaseDto<BimbinganProyekAkhir, ReviewpembimbingDto> {
    @Type(type = "text")
    private String catatan;
}
