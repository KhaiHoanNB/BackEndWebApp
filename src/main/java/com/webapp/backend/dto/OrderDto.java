package com.webapp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long id;

    @NotNull
    private Long shipperId;
    @NotNull
    private Long productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Long price;

}
