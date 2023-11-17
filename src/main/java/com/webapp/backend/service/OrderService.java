package com.webapp.backend.service;

import com.webapp.backend.common.Constants;
import com.webapp.backend.common.CustomException;
import com.webapp.backend.entity.Order;
import com.webapp.backend.entity.User;
import com.webapp.backend.entity.Warehouse;
import com.webapp.backend.repository.OrderRepository;
import com.webapp.backend.repository.UserRepository;
import com.webapp.backend.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WarehouseRepository warehouseRepository;


    @Transactional
    public void addOrder(Order order) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(order.getId());

        if (existedOrderOptional.isPresent()) {

            throw new CustomException("This order is existed");
        }

        order.setStatus(Constants.STATUS_NOT_CONFIRM);

        Double totalCashOrder = order.getPrice() * order.getQuantity();

        order.setTotalCash(totalCashOrder);

        order.setCreatedTime(LocalDateTime.now());

        repository.save(order);

        updateWarehouse(order);

    }

    private void updateWarehouse(Order order) throws CustomException {

        Warehouse warehouse = warehouseRepository.findByProductId(order.getProduct().getId());

        Integer theRestQuantity = warehouse.getQuantity() - order.getQuantity();

        if (theRestQuantity < 0) {
            throw new CustomException("The quantity is too large");
        } else {
            warehouse.setQuantity(theRestQuantity);
            warehouseRepository.save(warehouse);
        }
    }

    public void updateOrder(Order updateOrder) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(updateOrder.getId());

        if (!existedOrderOptional.isPresent()) {

            throw new CustomException("This order is not existed");

        } else {

            Order existedOrder = existedOrderOptional.get();

            if (existedOrder.getStatus() == Constants.STATUS_CONFIRMED) {
                throw new CustomException("Can not update order confirmed");
            }

            existedOrder.setQuantity(updateOrder.getQuantity());
            existedOrder.setShipper(updateOrder.getShipper());
            existedOrder.setProduct(updateOrder.getProduct());
            existedOrder.setShipper(updateOrder.getShipper());
            existedOrder.setPrice(updateOrder.getPrice());

            existedOrder.setTotalCash(updateOrder.getPrice() * updateOrder.getQuantity());

            existedOrder.setCreatedTime(updateOrder.getCreatedTime());

            repository.save(existedOrder);

        }
    }

    public void confirmOrder(Long id) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(id);

        if (existedOrderOptional.isPresent()) {

            Order existedOrder = existedOrderOptional.get();
            existedOrder.setStatus(Constants.STATUS_CONFIRMED);

            repository.save(existedOrder);

        } else {

            throw new CustomException("This order is not existed");

        }
    }

    public void deleteOrder(Long id) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(id);

        if (!existedOrderOptional.isPresent()) {
            throw new CustomException("This order is not existed");
        }

        Order order = existedOrderOptional.get();

        if (!(order.getStatus() == Constants.STATUS_NOT_CONFIRM)) {
            throw new CustomException("You do not have permission to delete this order");
        }

        updateDeleteOrderWarehouse(order);

        repository.deleteById(id);
    }

    private void updateDeleteOrderWarehouse(Order order) {

        Warehouse warehouse = warehouseRepository.findByProductId(order.getProduct().getId());

        warehouse.setQuantity(warehouse.getQuantity() + order.getQuantity());
        warehouseRepository.save(warehouse);

    }

    public void deleteAllOrder() {
        repository.deleteAll();
    }


    public Order getOrder(Long orderId) throws Exception {

        Optional<Order> existedOrderDetailOptional = repository.findById(orderId);

        if (existedOrderDetailOptional.isPresent()) {

            return existedOrderDetailOptional.get();

        } else {

            throw new CustomException("This order is not existed");

        }
    }

    public List<Order> getAllOrder() {
        return repository.findAll();
    }

    public List<Order> getOrdersOfShipper(Long shipperId) throws Exception {

        Optional<User> userOptional = userRepository.findById(shipperId);

        if (!userOptional.isPresent()) {
            throw new CustomException("The shipper is not existed");
        }

        return repository.findOrderByShipperId(shipperId);

    }

    public void returnOrder(Long orderId) throws CustomException {

        Optional<Order> existedOrderDetailOptional = repository.findById(orderId);

        if (!existedOrderDetailOptional.isPresent()) {
            throw new CustomException("This order is not existed");
        }

        Order order = existedOrderDetailOptional.get();

        updateReturnWarehouse(order);

        order.setStatus(Constants.STATUS_RETURN);

        repository.save(order);

    }

    private void updateReturnWarehouse(Order order) {

        Warehouse warehouse = warehouseRepository.findByProductId(order.getProduct().getId());

        warehouse.setQuantity(warehouse.getQuantity() + order.getQuantity());

        warehouseRepository.save(warehouse);

    }
}

