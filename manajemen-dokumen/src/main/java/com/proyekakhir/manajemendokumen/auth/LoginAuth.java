package com.proyekakhir.manajemendokumen.auth;

import com.proyekakhir.manajemendokumen.core.controller.BaseController;
import com.proyekakhir.manajemendokumen.core.exception.AlurKerjaException;
import com.proyekakhir.manajemendokumen.user.UserDto;
import com.proyekakhir.manajemendokumen.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class LoginAuth extends BaseController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UserDto dto){
        return success(userService.cekUser(dto));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDto dto) throws AlurKerjaException {
        dto.setRole("user");
        return success(userService.createFromDto(dto));
    }
}
