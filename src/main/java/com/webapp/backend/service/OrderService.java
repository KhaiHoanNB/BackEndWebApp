package com.webapp.backend.service;

import com.webapp.backend.common.Constants;
import com.webapp.backend.common.NotFoundException;
import com.webapp.backend.common.TooLargeQuantityException;
import com.webapp.backend.entity.Order;
import com.webapp.backend.entity.OrderDetail;
import com.webapp.backend.entity.User;
import com.webapp.backend.entity.Warehouse;
import com.webapp.backend.repository.OrderDetailRepository;
import com.webapp.backend.repository.OrderRepository;
import com.webapp.backend.repository.UserRepository;
import com.webapp.backend.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WarehouseRepository warehouseRepository;


    public void addOrder(Order order) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(order.getId());

        if (existedOrderOptional.isPresent()) {

            throw new NotFoundException("This order is existed");
        }

        List<OrderDetail> orderDetails = order.getOrderDetails();

        updateWarehouse(orderDetails);

        order.setStatus(Constants.STATUS_NOT_CONFIRM);
        Double totalCashOrder = caculatedTotalCashOrder(order.getOrderDetails());

        order.setTotalCash(totalCashOrder);

        repository.save(order);

    }

    private void updateWarehouse(List<OrderDetail> orderDetails) throws TooLargeQuantityException {
        for (int i = 0; i < orderDetails.size(); i++) {

            OrderDetail orderDetail = orderDetails.get(i);

            Warehouse warehouse = warehouseRepository.findByProductId(orderDetail.getProduct().getId());

            Integer theRestQuantity = warehouse.getQuantity() - orderDetail.getQuantity();

            if(theRestQuantity < 0){
                throw new TooLargeQuantityException("The quantity is too large");
            } else {
                warehouse.setQuantity(theRestQuantity);
                warehouseRepository.save(warehouse);
            }
        }
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

            throw new NotFoundException("This order is not existed");

        }
    }

    public void confirmOrder(Long id) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(id);

        if (existedOrderOptional.isPresent()) {

            Order existedOrder = existedOrderOptional.get();
            existedOrder.setStatus(Constants.STATUS_CONFIRMED);

            repository.save(existedOrder);

        } else {

            throw new NotFoundException("This order is not existed");

        }
    }

    public void deleteOrder(Long id) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(id);

        if (existedOrderOptional.isPresent()) {

            repository.deleteById(id);

        } else {

            throw new NotFoundException("This order is not existed");

        }
    }

    public void deleteAllOrder() {
        repository.deleteAll();
    }

    public void returnOrderDetail(Long orderDetailId) throws Exception {

        Optional<OrderDetail> existedOrderDetailOptional = orderDetailRepository.findById(orderDetailId);

        if (!existedOrderDetailOptional.isPresent()) {

            throw new NotFoundException("This order item is not existed");

        } else {

            OrderDetail orderDetail = existedOrderDetailOptional.get();

            Warehouse warehouse = warehouseRepository.findByProductId(orderDetail.getProduct().getId());

            warehouse.setQuantity(warehouse.getQuantity() + orderDetail.getQuantity());

            warehouseRepository.save(warehouse);

            orderDetailRepository.deleteById(orderDetailId);

        }

    }

    public Order getOrder(Long orderId) throws Exception {

        Optional<Order> existedOrderDetailOptional = repository.findById(orderId);

        if (existedOrderDetailOptional.isPresent()) {

            return existedOrderDetailOptional.get();

        } else {

            throw new NotFoundException("This order is not existed");

        }
    }

    public List<Order> getAllOrder() {
        return repository.findAll();
    }

    public List<Order> getOrdersOfShipper(Long shipperId) throws Exception {

        Optional<User> userOptional = userRepository.findById(shipperId);

        if(!userOptional.isPresent()){
            throw new NotFoundException("The shipper is not existed");
        }

        return repository.findOrderByShipperId(shipperId);

    }

    public void returnOrder(Long orderId) throws NotFoundException {

        Optional<Order> existedOrderDetailOptional = repository.findById(orderId);

        if(!existedOrderDetailOptional.isPresent()){
            throw new NotFoundException("This order is not existed");
        }

        Order order = existedOrderDetailOptional.get();

        updateReturnWarehouse(order.getOrderDetails());

        order.setStatus(Constants.STATUS_RETURN);

        repository.save(order);

    }

    private void updateReturnWarehouse(List<OrderDetail> orderDetails) {

        for (int i = 0; i < orderDetails.size(); i++) {

            OrderDetail orderDetail = orderDetails.get(i);

            Warehouse warehouse = warehouseRepository.findByProductId(orderDetail.getProduct().getId());

            warehouse.setQuantity(warehouse.getQuantity() + orderDetail.getQuantity());

            warehouseRepository.save(warehouse);

        }
    }
}
