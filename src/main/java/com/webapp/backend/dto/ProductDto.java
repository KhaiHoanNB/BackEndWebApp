package com.webapp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private long categoryId;

    private String description;

    private int quantity;

    private CategoryDto categoryDto;

    public ProductDto(Long id, String name, Long categoryId, String description, Integer quantity) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.description = description;
        this.quantity = quantity;
    }
}
