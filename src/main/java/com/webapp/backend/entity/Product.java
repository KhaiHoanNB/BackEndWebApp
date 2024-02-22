package com.webapp.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webapp.backend.core.entities.BaseEntity;
import com.webapp.backend.dto.ProductDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(unique = true, length = 255, nullable = false)
    @NotBlank
    private String name;

    @Column(name = "quantity")
    @NotNull
    private double quantity;

    @ManyToOne
    @JsonIgnore
    private Category category;

    @Column
    private String description;

}
