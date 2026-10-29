package com.proyekakhir.manajemendokumen.dosen;

import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.position.PositionRepository;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudi;
import com.proyekakhir.manajemendokumen.programstudi.ProgramStudiRepository;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import com.proyekakhir.manajemendokumen.services.StorageService;
import com.proyekakhir.manajemendokumen.user.User;
import com.proyekakhir.manajemendokumen.user.UserDto;
import com.proyekakhir.manajemendokumen.user.UserRepository;
import com.proyekakhir.manajemendokumen.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class DosenService extends CrudManajemenService<Dosen, DosenDto, DosenRepository> {

    @Autowired
    StorageService storageService;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DosenRepository dosenRepository;

    @Autowired
    ProgramStudiRepository programStudiRepository;

    protected DosenService(DosenRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }

    @Override
    public Dosen createFromDto(DosenDto dto) {
        Dosen exist = dosenRepository.findByEmail(dto.getEmail());
        if (exist != null){
            throw new BadRequestException("Data sudah ada");
        }
        Dosen dosen = dto.fromDto();
        if (dto.getProgramStudi() != null && dto.getProgramStudi().getId() != null){
            Optional<ProgramStudi> programStudiOptional = programStudiRepository.findById(dto.getProgramStudi().getId());
            if (programStudiOptional.isPresent()){
                dosen.setProgramStudi(programStudiOptional.get());
            } else {
                throw new BadRequestException("program studi tidak ditemukan");
            }
        }

        dosen.setCreatedDate(new Date());
        return this.jpaRepository.save(dosen);
    }

    public User generateUser(UUID id) throws AlurKerjaException {
        Optional<Dosen> dosenOp = this.get(id);
        if (dosenOp.isPresent()){
            Dosen dosen = dosenOp.get();
            User exist = userRepository.findByUsername(dosen.getEmail());
            if (exist != null){
                throw new BadRequestException("Username "+dosen.getEmail()+" sudah ada!");
            } else {
                UserDto userDto = new UserDto();
                userDto.setRole("dosen");
                userDto.setUsername(dosen.getEmail());
                userDto.setName(dosen.getName());
                userDto.setEmail(dosen.getEmail());
                userDto.setPosisi("Dosen");
                return userService.createFromDto(userDto);
            }
        } else {
            throw new NotFoundException("Data dosen tidak ditemukan");
        }
    }
}
