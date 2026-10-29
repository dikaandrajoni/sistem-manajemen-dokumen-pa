package com.proyekakhir.manajemendokumen.kategori;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KategoriDto extends BaseDto<Kategori, KategoriDto> {
    private String name;
    private String templateType;
    private String templateFileName;
    private String templateMinioId;
    private String templateMinioPath;
}
