package com.webapp.backend.service;


import com.webapp.backend.common.Constants;
import com.webapp.backend.common.CustomException;
import com.webapp.backend.core.entities.User;
import com.webapp.backend.core.repositories.UserRepository;
import com.webapp.backend.dto.ReportDto;
import com.webapp.backend.entity.Order;
import com.webapp.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    public ReportDto getOrdersByDateAndShipper(String date, Long shipperId) throws CustomException {

        ReportDto report = new ReportDto();

        Optional<User> userOptional = userRepository.findById(shipperId);

        if(!userOptional.isPresent()){
            throw new CustomException("User not found");
        }

        User user = userOptional.get();

        boolean hasShipperRole = user.getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_SHIPPER"));

        if(!hasShipperRole){
            throw new CustomException("This user is not a shipper");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        List<Order> orderList = orderRepository.findOrdersByDateAndShipperId(dateFormated, shipperId);

        double totalCash = 0;

        for (int i = 0; i < orderList.size(); i++) {

            Order order = orderList.get(i);

            if (order.getStatus().equals(Constants.STATUS_CONFIRMED)) {
                report.getSuccessfulOrder().add(order);
                totalCash += order.getCash();
            } else if (order.getStatus().equals(Constants.STATUS_RETURN)) {
                report.getReturnedOrder().add(order);
            } else {
                report.getPendingOrder().add(order);
            }

        }

        report.setShipperName(user.getUsername());
        report.setShipperId(user.getId());
        report.setTotalCash(totalCash);
        report.setDate(dateFormated);

        return report;
    }

    public List<ReportDto> getAllReport(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        List<Order> orderList = orderRepository.findOrdersByDate(dateFormated);


        List<User> users = userRepository.findAll();

        List<ReportDto> reports = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {

            ReportDto report = new ReportDto();

            User user = users.get(i);

            if(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                continue;

            report.setShipperName(user.getUsername());
            report.setShipperId(user.getId());

            double totalCash = 0;

            for (int j = 0; j < orderList.size(); j++) {

                Order order = orderList.get(j);

                if (user.getId() == order.getShipper().getId()) {
                    if (order.getStatus().equals(Constants.STATUS_CONFIRMED)) {
                        report.getSuccessfulOrder().add(order);
                        totalCash += order.getCash();
                    } else if (order.getStatus().equals(Constants.STATUS_RETURN)) {
                        totalCash -= order.getNumReturn()*order.getPrice();
                        report.getReturnedOrder().add(order);
                    } else {
                        report.getPendingOrder().add(order);
                    }
                }
            }

            report.setTotalCash(totalCash);
            report.setDate(dateFormated);

            reports.add(report);
        }

        for (int i = 0; i < reports.size(); i++) {

            ReportDto report = reports.get(i);

            if(report.getPendingOrder().isEmpty()
                    && report.getSuccessfulOrder().isEmpty()
                    && report.getReturnedOrder().isEmpty()) {
                reports.remove(i);
            }
        }

        return reports;
    }

    public Long getTotalAmountByDay(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        List<Order> successfulOrders = orderRepository.findOrdersByDateAndStatus(dateFormated, Constants.STATUS_CONFIRMED);

        return successfulOrders.stream()
                .map(Order::getCash)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();
    }


    public Long getTotalAmountByDayAndShipper(String date, Long shipperId) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        List<Order> successfulOrders = orderRepository.findOrdersByDateAndShipperIdAndStatus(dateFormated, shipperId, Constants.STATUS_CONFIRMED);

        return successfulOrders.stream()
                .map(Order::getCash)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();

    }
}
