package com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StorageDto extends BaseDto<ManajemenDokumenProyekAkhir, StorageDto> {
    private String ownerId;
    private String url;
    private String filePath;
    private String identifier;
}
