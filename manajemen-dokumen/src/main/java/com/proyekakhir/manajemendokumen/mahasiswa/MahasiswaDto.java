package com.proyekakhir.manajemendokumen.mahasiswa;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
public class MahasiswaDto extends BaseDto<Mahasiswa, MahasiswaDto> {
    private String nim;
    private String name;
    private String email;
    private String kelas;
    private String jenisKelamin;
    private String signatureMinioId;
    private String signatureMinioPath;
    private ProgramStudiDto programStudi;

    @Override
    public Mahasiswa fromDto() {
        Mahasiswa mahasiswa = new Mahasiswa();
        BeanUtils.copyProperties(this, mahasiswa, getNullPropertyNames(this));

        if (!ObjectUtils.isEmpty(programStudi) && !ObjectUtils.isEmpty(programStudi.getId())){
            ProgramStudi studi = new ProgramStudi();
            studi.setId(this.programStudi.getId());
            mahasiswa.setProgramStudi(studi);
        }
        return mahasiswa;
    }

    @Override
    public MahasiswaDto toDto(Mahasiswa entity) {
        MahasiswaDto dto = super.toDto(entity);
        if(entity.getProgramStudi()!=null){
            ProgramStudiDto programStudiDto = new ProgramStudiDto();
            programStudiDto.toDto(entity.getProgramStudi());
            dto.setProgramStudi(programStudi);
        }
        return dto;
    }
}
