package com.webapp.backend.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @NotBlank
    @NotNull
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "quantity")
    @NotBlank
    @NotNull
    private Long quantity;

    @Column(name = "created_time")
    @NotBlank
    @NotNull
    private Date createdTime;

    @Column(name = "updated_time")
    @NotBlank
    @NotNull
    private Date updatedTime;

}
