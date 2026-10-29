package com.proyekakhir.manajemendokumen.filedocument;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhir;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.ManajemenDokumenProyekAkhirDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class FileDocumentDto extends BaseDto<FileDocument, FileDocumentDto> {
    private ManajemenDokumenProyekAkhirDto manajemenDokumenProyekAkhir;
    private String fileName;
    private String type;
    private String fileId;
    private String filePath;
    private String extension;

    @Override
    public FileDocument fromDto() {
        FileDocument fileDocument = new FileDocument();
        BeanUtils.copyProperties(this, fileDocument, getNullPropertyNames(this));
        if (manajemenDokumenProyekAkhir != null){
            ManajemenDokumenProyekAkhir proyekAkhir = new ManajemenDokumenProyekAkhir();
            proyekAkhir.setId(this.manajemenDokumenProyekAkhir.getId());
            fileDocument.setManajemenDokumenProyekAkhir(proyekAkhir);
        }
        return fileDocument;
    }

    @Override
    public FileDocumentDto toDto(FileDocument entity) {
        BeanUtils.copyProperties(entity, this, getNullPropertyNames(entity));

        if (entity.getManajemenDokumenProyekAkhir() != null){
            ManajemenDokumenProyekAkhirDto proyekAkhirDto = new ManajemenDokumenProyekAkhirDto();
            proyekAkhirDto.toDto(entity.getManajemenDokumenProyekAkhir());
            setManajemenDokumenProyekAkhir(proyekAkhirDto);
        }
        this.setId(entity.getId());
        return this;
    }
}
