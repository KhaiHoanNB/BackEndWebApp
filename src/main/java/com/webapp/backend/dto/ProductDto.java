package com.webapp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private CategoryDto categoryDto;

    public ProductDto(Long id, String name, Long categoryId, String description, Integer quantity) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.description = description;
        this.quantity = quantity;
    }
}
