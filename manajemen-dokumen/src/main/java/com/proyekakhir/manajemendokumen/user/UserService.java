package com.proyekakhir.manajemendokumen.user;

import com.proyekakhir.manajemendokumen.config.JwtTokenUtil;
import com.proyekakhir.manajemendokumen.constant.ApplicationEnum;
import com.proyekakhir.manajemendokumen.constant.CurrentUser;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.exception.BadRequestException;
import com.proyekakhir.manajemendokumen.exception.UnauthorizedException;
import com.proyekakhir.manajemendokumen.manajemendokumenproyekakhir.StorageDto;
import com.proyekakhir.manajemendokumen.services.CrudManajemenService;
import com.proyekakhir.manajemendokumen.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService extends CrudManajemenService<User, UserDto, UserRepository> {

    @Autowired
    StorageService storageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected UserService(UserRepository simpleJpaRepository, CurrentUser currentUser) {
        super(simpleJpaRepository, currentUser);
    }

    public Map<String, Object> cekUser(UserDto dto){
        if ( dto != null && dto.getUsername() != null){
            User user = userRepository.findByUsername(dto.getUsername());
            if (user != null && dto.getPassword() != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())){
                    String token =  jwtTokenUtil.generateToken(user);
                    Map<String, Object> map = new HashMap<>();
                    map.put("token", token);
                    map.put("user", user.getName());
                    map.put("email", user.getEmail());
                    map.put("username", user.getUsername());
                    map.put("role", user.getRole());
                    map.put("posisi", user.getPosisi());
                    return map;
            } else {
                throw new UnauthorizedException("Username atau password salah.");
            }
        } else {
            throw new UnauthorizedException("Username dan password kosong.");
        }
    }

    @Override
    public User createFromDto(UserDto dto) throws AlurKerjaException {
        User exist = userRepository.findByUsername(dto.getUsername());
        if (exist != null){
            throw new BadRequestException("username sudah ada");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()){
            throw new BadRequestException("Password tidak boleh kosong");
        }
        dto.setPassword(encryptionPass(dto.getPassword()));
        if (dto.getRole() == null){
            dto.setRole("user");
        }
        return super.createFromDto(dto);
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null){
            return user;
        } else {
            throw new UnauthorizedException("User "+username+" not found");
        }
    }

    public User uploadSignature(UUID id, MultipartFile file) throws IOException {
        Optional<User> userOptional = this.get(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            cekTypeFile(file);
            StorageDto storageDto = storageService.upload(file.getBytes(), ApplicationEnum.minioFolder.SIGNATURE.name(), null);
            if (storageDto != null){
                user.setSignatureId(storageDto.getIdentifier());
                user.setSignaturePath(storageDto.getFilePath());
                user.setSignatureName(file.getOriginalFilename());
                this.update(user);
            }
            return user;
        } else {
            throw new NotFoundException("Data tidak ditemukan");
        }
    }

    private void cekTypeFile(MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".png") && !file.getOriginalFilename().endsWith(".jpg")
                && !file.getOriginalFilename().endsWith(".jpeg")) {
            throw new BadRequestException("Tipe file harus jpg/png");
        }
    }

    public String encryptionPass(String pass) {
        return passwordEncoder.encode(pass);
    }

    public User setPassowrd(UUID id, String password) {
        Optional<User> userOptional = this.get(id);
        if (userOptional.isPresent()){
             User user = userOptional.get();
             if (password == null)
                 throw new BadRequestException("Password tidak boleh kosong");
             user.setPassword(encryptionPass(password));
             this.update(user);
             return user;
        } else {
            throw new NotFoundException("Data user tidak dapat ditemukan");
        }
    }

    public User ubahPassword(UserDto dto) {
        if (dto.getPassword() == null || dto.getPassword().isEmpty())
            throw new BadRequestException("Password tidak boleh kosong");
        User user = userRepository.findByUsername(currentUser.getUserName());
        if (user != null){
            user.setPassword(encryptionPass(dto.getPassword()));
            return this.update(user);
        } else {
            throw new NotFoundException("Data user tidak dapat ditemukan");
        }
    }
}
