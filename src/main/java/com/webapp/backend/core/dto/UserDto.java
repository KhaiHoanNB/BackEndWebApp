package com.webapp.backend.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private boolean isAdmin;
    private boolean isShipper;
    private List<RoleDto> roles;
}
