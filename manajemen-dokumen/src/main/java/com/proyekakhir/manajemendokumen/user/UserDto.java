package com.proyekakhir.manajemendokumen.user;

import com.proyekakhir.manajemendokumen.core.entity.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends BaseDto<User, UserDto> {
    private String username;
    private String name;
    private String email;
    private String role;
    private String password;
    private String signatureId;
    private String signaturePath;
    private String signatureName;
    private String posisi;
}
