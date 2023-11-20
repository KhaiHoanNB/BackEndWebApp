package com.webapp.backend.common;

import com.webapp.backend.core.entities.User;
import com.webapp.backend.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    private User shipper;

    private List<Order> successfulOrder;

    private List<Order> returnedOrder;

    private List<Order> pendingOrder;

    private LocalDate date;

    private Double totalCash;

}
