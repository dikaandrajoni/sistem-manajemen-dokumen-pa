package com.proyekakhir.manajemendokumen.dosen;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

@Getter
@Setter
public class DosenDto extends BaseDto<Dosen, DosenDto> {
    private String nip;
    private String name;
    private String email;
    private String positionName;
    private String jenisKelamin;
    private String position;
    private ProgramStudiDto programStudi;

    @Override
    public Dosen fromDto() {
        Dosen dosen = new Dosen();
        BeanUtils.copyProperties(this, dosen, getNullPropertyNames(this));
        dosen.setId(this.getId());
        if (!ObjectUtils.isEmpty(programStudi) && !ObjectUtils.isEmpty(programStudi.getId())){
            ProgramStudi studi = new ProgramStudi();
            studi.setId(this.programStudi.getId());
            dosen.setProgramStudi(studi);
        }
        return dosen;
    }

    @Override
    public DosenDto toDto(Dosen entity) {
        DosenDto dto = super.toDto(entity);
        if(entity.getProgramStudi()!=null){
            ProgramStudiDto programStudiDto = new ProgramStudiDto();
            programStudiDto.toDto(entity.getProgramStudi());
            dto.setProgramStudi(programStudiDto);
        }
        return dto;
    }
}
