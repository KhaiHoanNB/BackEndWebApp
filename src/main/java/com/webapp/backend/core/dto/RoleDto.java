package com.webapp.backend.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {
    private long id;
    private String name;
    private String description;
}
