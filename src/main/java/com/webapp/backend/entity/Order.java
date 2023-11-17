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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @NotNull
    @JoinTable(
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "orderdetail_id")
    )
    private List<OrderDetail> orderDetails;

    @Column
    private Integer status;

    @Column
    private Double totalCash;

    @Column(name = "created_time")
    @NotNull
    private LocalDateTime createdTime;

}
