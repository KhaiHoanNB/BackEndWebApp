package com.webapp.backend.dto;

import com.webapp.backend.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDto {

    private ProductDto productDto;

    private Integer quantity;

}
