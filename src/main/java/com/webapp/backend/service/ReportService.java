package com.webapp.backend.service;


import com.webapp.backend.entity.Order;
import com.webapp.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    OrderRepository orderRepository;


    public List<Order> getSuccessfulOrderByDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);
        
        return orderRepository.findOrdersByDate(dateFormated);

    }

    public List<Order> getSuccessfulOrderByDateAndShipper(String date, Long shipperId) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        return orderRepository.findOrdersByDateAndShipperId(dateFormated, shipperId);
    }
}
