package com.proyekakhir.manajemendokumen.jurusan;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class JurusanDto extends BaseDto<Jurusan, JurusanDto> {
    private String name;
    private String kode;
    private String kajurId;
    private String kajurNip;
    private String kajurName;
    private String kajurEmail;
    @OneToMany
    private List<ProgramStudiDto> programStudi;

    @Override
    public Jurusan fromDto() {
        Jurusan jurusan = new Jurusan();
        BeanUtils.copyProperties(this, jurusan, getNullPropertyNames(this));
        jurusan.setProgramStudi(new ArrayList<>());
        if (!CollectionUtils.isEmpty(programStudi)){
            for (ProgramStudiDto studiDto : programStudi) {
                jurusan.getProgramStudi().add(studiDto.fromDto());
            }
        }
        return jurusan;
    }
}
