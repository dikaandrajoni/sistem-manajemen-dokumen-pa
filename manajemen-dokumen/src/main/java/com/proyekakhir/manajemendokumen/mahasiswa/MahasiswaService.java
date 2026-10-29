package com.proyekakhir.manajemendokumen.mahasiswa;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiRepository;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import com.proyekakhir.manajemendokumen.user.User;
import com.proyekakhir.manajemendokumen.user.UserDto;
import com.proyekakhir.manajemendokumen.user.UserRepository;
import com.proyekakhir.manajemendokumen.user.UserService;
import org.camunda.bpm.engine.BadUserRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class MahasiswaService extends CrudManajemenService<Mahasiswa, MahasiswaDto, MahasiswaRepository> {

    @Autowired
    ProgramStudiRepository programStudiRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    protected MahasiswaService(MahasiswaRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }

    @Override
    public Mahasiswa update(Mahasiswa entity, MahasiswaDto dto) throws AlurKerjaException {
        if (dto.getProgramStudi() != null && !ObjectUtils.isEmpty(dto.getProgramStudi().getId())) {
            Optional<ProgramStudi> prodiOptional = programStudiRepository.findById(dto.getProgramStudi().getId());
            if (prodiOptional.isPresent()) {
                entity.setProgramStudi(prodiOptional.get());
            } else {
                throw new BadUserRequestException("Program studi tidak dapat ditemukan");
            }
        }
        return super.update(entity, dto);
    }

    @Override
    public Mahasiswa createFromDto(MahasiswaDto dto) {
        ProgramStudi prodi = null;
        if (dto.getProgramStudi() != null) {
            if (dto.getProgramStudi().getId() != null) {
                Optional<ProgramStudi> prodiOptional = programStudiRepository.findById(dto.getProgramStudi().getId());
                if (prodiOptional.isPresent()) {
                    prodi = prodiOptional.get();
                } else {
                    throw new BadUserRequestException("Program studi tidak dapat ditemukan");
                }
            } else {
                throw new BadUserRequestException("Program studi must be not empty");
            }
        }
        Mahasiswa mahasiswa = dto.fromDto();
        mahasiswa.setProgramStudi(prodi);
        mahasiswa.setCreatedDate(new Date());
        mahasiswa.setCreatedBy(this.getCurrentUser());
        return this.jpaRepository.save(mahasiswa);
    }

    public User generateUser(UUID id) throws AlurKerjaException {
        Optional<Mahasiswa> temp = this.get(id);
        if (temp.isPresent()){
            Mahasiswa mahasiswa = temp.get();
            User exist = userRepository.findByUsername(mahasiswa.getNim());
            if (exist != null){
                throw new BadRequestException("Username "+mahasiswa.getNim()+" sudah ada!");
            } else {
                UserDto userDto = new UserDto();
                userDto.setRole("mahasiswa");
                userDto.setUsername(mahasiswa.getNim());
                userDto.setName(mahasiswa.getName());
                userDto.setEmail(mahasiswa.getEmail());
                userDto.setPosisi("Mahasiswa");
                return userService.createFromDto(userDto);
            }
        } else {
            throw new NotFoundException("Data mahasiswa tidak ditemukan");
        }
    }
}
