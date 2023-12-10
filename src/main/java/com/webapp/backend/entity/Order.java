package com.webapp.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webapp.backend.core.entities.BaseEntity;
import com.webapp.backend.core.entities.User;
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
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipper_id", referencedColumnName = "id")
    @NotNull
    private User shipper;

    @ManyToOne
    @NotNull
    private Product product;

    @Column(name = "quantity")
    @NotNull
    private int quantity;

    @Column(name = "price")
    @NotNull
    private Long price;

    @Column(name = "total_cash")
    private Long cash;

    @Column
    private Integer status;

    @Column
    private int freeShip;

    @Column
    private int numReturn;

    @Column(name = "confirm_time")
    private LocalDateTime confirmTime;

}
