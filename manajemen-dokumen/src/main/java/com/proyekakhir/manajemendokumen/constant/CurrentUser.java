package com.proyekakhir.manajemendokumen.constant;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Component
@RequestScope
public class CurrentUser {
    private String userId;
    private String role;
    private String userName;
    private String name;

    public void set(String userId, String role, String userName, String name) {
        this.userId = userId;
        this.role = role;
        this.userName = userName;
        this.name = name;
    }

}
