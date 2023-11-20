package com.webapp.backend.service;


import com.webapp.backend.common.Constants;
import com.webapp.backend.common.CustomException;
import com.webapp.backend.common.Report;
import com.webapp.backend.core.entities.User;
import com.webapp.backend.core.repositories.UserRepository;
import com.webapp.backend.entity.Order;
import com.webapp.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    public Report getOrdersByDateAndShipper(String date, Long shipperId) throws CustomException {

        Report report = new Report();

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
                totalCash += order.getTotalCash();
            } else if (order.getStatus().equals(Constants.STATUS_CONFIRMED_RETURN)) {
                report.getReturnedOrder().add(order);
            } else {
                report.getPendingOrder().add(order);
            }

        }

        report.setTotalCash(totalCash);
        report.setDate(dateFormated);

        return report;
    }

    public List<Report> getAllReport(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        List<Order> orderList = orderRepository.findOrdersByDate(dateFormated);


        List<User> users = userRepository.findAll();

        List<Report> reports = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {

            Report report = new Report();

            User user = users.get(i);

            report.setShipper(user);

            double totalCash = 0;

            for (int j = 0; j < orderList.size(); j++) {

                Order order = orderList.get(i);

                if (user.getId() == order.getShipper().getId()) {
                    if (order.getStatus().equals(Constants.STATUS_CONFIRMED)) {
                        report.getSuccessfulOrder().add(order);
                        totalCash += order.getTotalCash();
                    } else if (order.getStatus().equals(Constants.STATUS_CONFIRMED_RETURN)) {
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

            Report report = reports.get(i);

            if(report.getPendingOrder().size() == 0
                    && report.getSuccessfulOrder().size() == 0
                    && report.getReturnedOrder().size() == 0) {
                reports.remove(i);
            }
        }

        return reports;
    }
}
