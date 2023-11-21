package com.webapp.backend.service;

import com.webapp.backend.common.Constants;
import com.webapp.backend.common.CustomException;
import com.webapp.backend.core.entities.User;
import com.webapp.backend.core.repositories.UserRepository;
import com.webapp.backend.dto.OrderDto;
import com.webapp.backend.entity.Order;
import com.webapp.backend.entity.Product;
import com.webapp.backend.entity.Warehouse;
import com.webapp.backend.repository.OrderRepository;
import com.webapp.backend.repository.ProductRepository;
import com.webapp.backend.repository.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger("info_trace");


    @Autowired
    OrderRepository repository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    ProductRepository productRepository;


    @Transactional
    public Order addOrder(OrderDto orderDto) throws Exception {

        Optional<Product> productOptional = productRepository.findById(orderDto.getProductId());

        if(!productOptional.isPresent()){
            throw new CustomException("This product is not existed");
        }

        Optional<User> userOptional = userRepository.findById(orderDto.getShipperId());

        if(!userOptional.isPresent()){
            throw new CustomException("This product is not existed");
        }

        Order order = new Order();

        order.setShipper(userOptional.get());

        order.setProduct(productOptional.get());

        order.setStatus(Constants.STATUS_NOT_CONFIRM);

        order.setQuantity(orderDto.getQuantity());

        order.setPrice(orderDto.getPrice());

        Double totalCashOrder = orderDto.getPrice() * orderDto.getQuantity();

        order.setCash(totalCashOrder);

        order.setCreatedTime(LocalDateTime.now());

        updateWarehouse(order);

        return repository.save(order);

    }

    public List<Order> getAllOrderByDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        return repository.findOrdersByDate(dateFormated);

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

    public Order updateOrder(OrderDto updateOrder) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(updateOrder.getId());

        if (!existedOrderOptional.isPresent()) {

            throw new CustomException("This order is not existed");

        }

        Order existedOrder = existedOrderOptional.get();

        if (!(existedOrder.getStatus() == Constants.STATUS_NOT_CONFIRM)) {
            throw new CustomException("You only can update not_confirmed order.");
        }

        existedOrder.setQuantity(updateOrder.getQuantity());
        existedOrder.setPrice(updateOrder.getPrice());

        existedOrder.setCash(updateOrder.getPrice() * updateOrder.getQuantity());

        return repository.save(existedOrder);
    }

    public Order confirmOrder(Long id) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(id);

        if (!existedOrderOptional.isPresent()) {
            throw new CustomException("This order is not existed");
        }

        Order existedOrder = existedOrderOptional.get();
        if(!(existedOrder.getStatus() == Constants.STATUS_NOT_CONFIRM)){
            throw new CustomException("Only confirm unconfirmed order");
        }
        existedOrder.setStatus(Constants.STATUS_CONFIRMED);
        existedOrder.setConfirmTime(LocalDateTime.now());

        LOGGER.info("Confirm order " + existedOrder.toString());

        return repository.save(existedOrder);
    }

    public void deleteOrder(Long orderId, Long shipperId) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(orderId);

        if (!existedOrderOptional.isPresent()) {
            throw new CustomException("This order is not existed");
        }

        Order order = existedOrderOptional.get();

        if (!(order.getStatus() == Constants.STATUS_NOT_CONFIRM)) {
            throw new CustomException("You do not have permission to delete this order");
        }

        if(!(order.getShipper().getId() == shipperId)){
            throw new CustomException("You do not have permission to delete this order");
        }

        updateDeleteOrderWarehouse(order);

        repository.deleteById(orderId);
    }

    private void updateDeleteOrderWarehouse(Order order) {

        Warehouse warehouse = warehouseRepository.findByProductId(order.getProduct().getId());

        warehouse.setQuantity(warehouse.getQuantity() + order.getQuantity());
        warehouseRepository.save(warehouse);

    }

    public void deleteAllOrder() throws CustomException {
        List<Order> allOrder = repository.findAll();

        if(allOrder.isEmpty()){
            throw new CustomException("There is no order to delete");
        }

        for (int i = 0; i < allOrder.size(); i++) {

            Order order = allOrder.get(i);

            if(order.getStatus() == Constants.STATUS_NOT_CONFIRM){
                updateDeleteOrderWarehouse(order);
            }

            if(order.getStatus() == Constants.STATUS_RETURN) continue;

            repository.deleteById(order.getId());
        }


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

    public Order returnOrder(Long orderId) throws CustomException {

        Optional<Order> existedOrderOptional = repository.findById(orderId);

        if (!existedOrderOptional.isPresent()) {
            throw new CustomException("This order is not existed");
        }

        Order order = existedOrderOptional.get();

        if(!(order.getStatus() == Constants.STATUS_CONFIRMED)){
            throw new CustomException("Only return confirmed order");
        }
//        updateReturnWarehouse(order);

        order.setStatus(Constants.STATUS_RETURN);

        return repository.save(order);

    }

    private void updateReturnWarehouse(Order order) {

        Warehouse warehouse = warehouseRepository.findByProductId(order.getProduct().getId());

        warehouse.setQuantity(warehouse.getQuantity() + order.getQuantity());

        warehouseRepository.save(warehouse);

    }

    public Order confirmReturnOrder(Integer orderId) throws CustomException {

        Optional<Order> existedOrderOptional = repository.findById(Long.valueOf(orderId));

        if (!existedOrderOptional.isPresent()) {

            throw new CustomException("This order is not existed");

        }

        Order existedOrder = existedOrderOptional.get();

        if(existedOrder.getStatus() == Constants.STATUS_RETURN){

            existedOrder.setStatus(Constants.STATUS_CONFIRMED_RETURN);

            updateReturnWarehouse(existedOrder);
        }

        LOGGER.info("Confirm return order " + existedOrder.toString());

       return repository.save(existedOrder);

    }

    public List<Order> getOrdersOfShipperByDate(Long shipperId, String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        return repository.findOrdersByDateAndShipperId(dateFormated, shipperId);

    }
}

