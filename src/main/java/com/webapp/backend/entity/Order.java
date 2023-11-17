package com.webapp.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipper_id", referencedColumnName = "id")
    @NotNull
    private User shipper;

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull
    private Product product;

    @Column(name = "quantity")
    @NotNull
    private Integer quantity;

    @Column(name = "price")
    @NotNull
    private Double price;

    @Column(name = "total_cash")
    private Double totalCash;

    @Column
    private Integer status;

    @Column(name = "created_time")
    @NotNull
    private LocalDateTime createdTime;

    @Column(name = "confirm_time")
    private LocalDateTime confirmTime;

}
