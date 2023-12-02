package com.webapp.backend.dto;

import com.webapp.backend.core.entities.User;
import com.webapp.backend.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

    private Long shipperId;

    private String shipperName;

    private List<Order> successfulOrder = new ArrayList<>();

    private List<Order> returnedOrder= new ArrayList<>();

    private List<Order> pendingOrder= new ArrayList<>();

    private LocalDate date;

    private Double totalCash;

}
