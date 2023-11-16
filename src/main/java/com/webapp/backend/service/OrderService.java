package com.webapp.backend.service;

import com.webapp.backend.entity.Order;
import com.webapp.backend.entity.OrderDetail;
import com.webapp.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;


    public void addOrder(Order order) {

        order.setStatus(0);
        Double totalCashOrder = caculatedTotalCashOrder(order.getOrderDetails());

        order.setTotalCash(totalCashOrder);

        repository.save(order);
    }

    private Double caculatedTotalCashOrder(List<OrderDetail> orderDetails) {

        Double totalCash = 0.0;

        for (int i = 0; i < orderDetails.size(); i++) {

            orderDetails.get(i).setTotalCash(orderDetails.get(i).getPrice()*orderDetails.get(i).getQuantity());

            totalCash += orderDetails.get(i).getTotalCash();
        }
        return totalCash;
    }

    public void updateOrder(Order updateOrder) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(updateOrder.getId());

        if (existedOrderOptional.isPresent()) {
             Order existedOrder = existedOrderOptional.get();

            existedOrder.setOrderDetails(updateOrder.getOrderDetails());
            Double totalCashOrder = caculatedTotalCashOrder(updateOrder.getOrderDetails());

            existedOrder.setTotalCash(totalCashOrder);
            existedOrder.setShipper(updateOrder.getShipper());

            existedOrder.setCreatedTime(updateOrder.getCreatedTime());

            repository.save(existedOrder);

        } else {

            throw new Exception("This order is not existed");

        }
    }

    public void confirmOrder(Long id) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(id);

        if (existedOrderOptional.isPresent()) {

            Order existedOrder = existedOrderOptional.get();
            existedOrder.setStatus(1);

            repository.save(existedOrder);

        } else {

            throw new Exception("This order is not existed");

        }
    }

    public void deleteOrder(Long id) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(id);

        if (existedOrderOptional.isPresent()) {

            repository.deleteById(id);

        } else {

            throw new Exception("This order is not existed");

        }
    }

    public void deleteAllOrder() {
        repository.deleteAll();
    }
}
