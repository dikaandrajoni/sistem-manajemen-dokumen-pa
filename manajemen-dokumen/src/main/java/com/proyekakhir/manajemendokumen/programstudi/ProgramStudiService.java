package com.proyekakhir.manajemendokumen.programstudi;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.dosen.Dosen;
import com.proyekakhir.manajemendokumen.dosen.DosenRepository;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProgramStudiService extends CrudManajemenService<ProgramStudi, ProgramStudiDto, ProgramStudiRepository>{

    @Autowired
    DosenRepository dosenRepository;

    protected ProgramStudiService(ProgramStudiRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }

    @Override
    public ProgramStudi createFromDto(ProgramStudiDto dto) throws AlurKerjaException {
        if (!ObjectUtils.isEmpty(dto.getKaprodiId())){
            Dosen dosen = dosenRepository.getById(dto.getKaprodiId());
            if (dosen != null){
                dto.setKaprodiEmail(dosen.getEmail());
                dto.setKaprodiName(dosen.getName());
                dto.setKaprodiNip(dosen.getNip());
            }
        }
        return super.createFromDto(dto);
    }

    public ProgramStudi setKaprodi(UUID id, ProgramStudiDto dto) {
        Optional<ProgramStudi> programStudi = this.get(id);
        if (programStudi.isPresent()){
            ProgramStudi prodi = programStudi.get();
            prodi.setKaprodiId(dto.getKaprodiId());
            prodi.setKaprodiEmail(dto.getKaprodiEmail());
            prodi.setKaprodiName(dto.getKaprodiName());
            prodi.setKaprodiNip(dto.getKaprodiNip());
            return this.update(prodi);
        } else {
            throw new NotFoundException("Data tidak ditemukan");
        }
    }
}
