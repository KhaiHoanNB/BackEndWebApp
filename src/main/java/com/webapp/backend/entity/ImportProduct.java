package com.webapp.backend.entity;


import com.webapp.backend.core.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "imported_product")
public class ImportProduct extends BaseEntity {

    @ManyToOne
    Product product;

    @Column
    Integer quantity;

}
