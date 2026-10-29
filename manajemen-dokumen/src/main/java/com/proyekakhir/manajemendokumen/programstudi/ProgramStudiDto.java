package com.proyekakhir.manajemendokumen.programstudi;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class ProgramStudiDto extends BaseDto<ProgramStudi, ProgramStudiDto> {
    private String diploma;
    private String kode;
    private String name;
    private UUID kaprodiId;
    private String kaprodiName;
    private String kaprodiEmail;
    private String kaprodiNip;

    @Override
    public ProgramStudi fromDto() {
        ProgramStudi programStudi = new ProgramStudi();
        BeanUtils.copyProperties(this, programStudi, getNullPropertyNames(this));
        programStudi.setCreatedDate(new Date());
        return programStudi;
    }
}

