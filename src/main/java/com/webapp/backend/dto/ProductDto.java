package com.webapp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Long categoryId;

    private String description;
    @NotNull
    private Integer quantity;
}
