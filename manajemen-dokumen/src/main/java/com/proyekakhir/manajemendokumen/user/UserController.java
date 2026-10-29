package com.proyekakhir.manajemendokumen.user;

import com.proyekakhir.manajemendokumen.auth.Secured;
import com.proyekakhir.manajemendokumen.constant.RoleUser;
import com.proyekakhir.manajemendokumen.core.controller.CrudController;
import com.proyekakhir.manajemendokumen.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController extends CrudController<User, UserDto, UserService, UserRepository> {

    @Autowired
    StorageService storageService;

    @Autowired
    UserService userService;

    protected UserController(UserService crudService) {
        super(crudService);
    }

    @PostMapping("/{id}/upload-signature")
    public ResponseEntity<Object> uploadSignature(@PathVariable UUID id, @RequestParam MultipartFile file) throws IOException {
        return success(userService.uploadSignature(id, file));
    }

    @GetMapping("/{id}/download-signature")
    public ResponseEntity<Object> downloadSignature(@PathVariable UUID id) throws IOException {
        Optional<User> userOptional = this.crudService.get(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            if (user.getSignaturePath() == null){
                return badRequest("Tanda tangan belum diupload");
            }
            ByteArrayResource resource = storageService.download(user.getSignaturePath());

            HttpHeaders header = new HttpHeaders();

            header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + user.getSignatureName() + "");
            header.add("Cache-Control", "no-cache, no-store, must-revalidate");
            header.add("Pragma", "no-cache");
            header.add("Expires", "0");
            return ResponseEntity.ok()
                    .headers(header)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return badRequest("User tidak ditemukan");
        }

    }

    @Secured(RoleUser.admin)
    @PostMapping("/{id}/setPassword")
    public ResponseEntity<Object> setPassword(@PathVariable UUID id, @RequestBody UserDto dto){
        return success(userService.setPassowrd(id, dto.getPassword()));
    }

    @PostMapping("/ubah-password")
    public ResponseEntity<Object> ubahPassword(@RequestBody UserDto dto){
        return success(userService.ubahPassword(dto));
    }
}
