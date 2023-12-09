package com.webapp.backend.service;

import com.webapp.backend.common.Constants;
import com.webapp.backend.common.CustomException;
import com.webapp.backend.core.entities.User;
import com.webapp.backend.core.repositories.UserRepository;
import com.webapp.backend.dto.OrderDto;
import com.webapp.backend.dto.UpdateStatusOrder;
import com.webapp.backend.entity.Order;
import com.webapp.backend.entity.Product;
import com.webapp.backend.repository.OrderRepository;
import com.webapp.backend.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    ProductRepository productRepository;
    @Autowired
    public EntityManager manager;

    @Transactional
    public Order addOrder(OrderDto orderDto) throws Exception {

        Optional<Product> productOptional = productRepository.findById(orderDto.getProductId());

        if(!productOptional.isPresent()){
            throw new CustomException("This product is not existed");
        }

        Optional<User> userOptional = userRepository.findById(orderDto.getShipperId());

        if(!userOptional.isPresent()){
            throw new CustomException("This shipper is not existed");
        }

        Optional<User> shipper = userRepository.findById(orderDto.getShipperId());

        if(!shipper.isPresent() || !shipper.get().getRoles().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_SHIPPER"))){
            throw new CustomException("The shipper is not existed");
        }

        Order order = new Order();

        order.setShipper(userOptional.get());

        order.setProduct(productOptional.get());

        order.setStatus(Constants.STATUS_NOT_CONFIRM);

        order.setQuantity(orderDto.getQuantity());

        order.setPrice(orderDto.getPrice());

        Long totalCashOrder = orderDto.getPrice() * orderDto.getQuantity();

        order.setCash(totalCashOrder);

        updateWarehouse(order);

        return repository.save(order);

    }

    public List<Order> getAllOrderByDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        return repository.findOrdersByDate(dateFormated);

    }

    private void updateWarehouse(Order order) throws CustomException {

        Product product = productRepository.findById(order.getProduct().getId()).get();

        int theRestQuantity = product.getQuantity() - order.getQuantity();

        if (theRestQuantity < 0) {
            throw new CustomException("The quantity you ordered is too large");
        } else {
            product.setQuantity(theRestQuantity);
            productRepository.save(product);
        }
    }

    public Order updateOrder(OrderDto updateOrder) throws Exception {
        Optional<Order> existedOrderOptional = repository.findById(updateOrder.getId());

        if (!existedOrderOptional.isPresent()) {
            throw new CustomException("This order is not existed");
        }

        Order existedOrder = existedOrderOptional.get();

        if (existedOrder.getStatus() != Constants.STATUS_NOT_CONFIRM) {
            throw new CustomException("Can not change confirmed order");
        }

        updateReturnWarehouse(existedOrder);

        existedOrder.setQuantity(updateOrder.getQuantity());

        updateWarehouse(existedOrder);

        existedOrder.setPrice(updateOrder.getPrice());

        existedOrder.setCash(existedOrder.getPrice()*existedOrder.getQuantity());

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

        LOGGER.info("Order confirmed - Product: {}, Shipper: {},  Quantity: {}, Price: {}, Total Cash: {}, Status: {}",
                existedOrder.getProduct().getName(), existedOrder.getShipper().getUsername(), existedOrder.getQuantity(), existedOrder.getPrice(), existedOrder.getCash(), existedOrder.getStatus());

        return repository.save(existedOrder);
    }

    public void deleteOrder(Long orderId) throws Exception {

        Optional<Order> existedOrderOptional = repository.findById(orderId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object object = authentication.getPrincipal();
        User userDetails = (User) object;

        if (!existedOrderOptional.isPresent()) {
            throw new CustomException("This order is not existed");
        }

        Order order = existedOrderOptional.get();

        if (!(order.getStatus() == Constants.STATUS_NOT_CONFIRM)) {
            throw new CustomException("You do not have permission to delete this order");
        }

        if(!order.getShipper().getId().equals(userDetails.getId())){
            throw new CustomException("You do not have permission to delete this order");
        }

        updateDeleteOrderQuantity(order);

        repository.deleteById(orderId);
    }

    private void updateDeleteOrderQuantity(Order order) {

        Product product = productRepository.findById(order.getProduct().getId()).get();

        product.setQuantity(product.getQuantity() + order.getQuantity());
        productRepository.save(product);

    }

    public void deleteAllOrder() throws CustomException {
        List<Order> allOrder = repository.findAll();

        if(allOrder.isEmpty()){
            throw new CustomException("There is no order to delete");
        }

        for (int i = 0; i < allOrder.size(); i++) {

            Order order = allOrder.get(i);

            if(order.getStatus() == Constants.STATUS_NOT_CONFIRM){
                updateDeleteOrderQuantity(order);
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

    public List<Order> getAll(OrderDto orderDto) {

        String sql = "Select e from Order e where 1=1 ";
        if (orderDto.getShipperId() != null) {
            sql += "and e.shipper.id = " + orderDto.getShipperId() ;
        }

        if (orderDto.getStatus() != null) {
            sql += "and e.status = " + orderDto.getStatus() ;
        }
        if (orderDto.getDate() != null) {
            sql += "and TO_CHAR(e.createDate, 'YYYYMMDD') = '" + orderDto.getDate() + "'" ;
        }

        Query query = manager.createQuery(sql);

        return query.getResultList();
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
        updateReturnWarehouse(order);

        order.setStatus(Constants.STATUS_RETURN);

        return repository.save(order);

    }

    private void updateReturnWarehouse(Order order) throws CustomException {

        Optional<Product> productOptional = productRepository.findById(order.getProduct().getId());

        if(!productOptional.isPresent()){
            throw new CustomException("This product is not existed");
        }

        Product product = productOptional.get();

        product.setQuantity(product.getQuantity() + order.getQuantity());

        productRepository.save(product);

    }


    public List<Order> getOrdersOfShipperByDate(Long shipperId, String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate dateFormated = LocalDate.parse(date, formatter);

        return repository.findOrdersByDateAndShipperIdForDisplay(dateFormated, shipperId);

    }

    public Order updateOrder(UpdateStatusOrder updateOrder) throws Exception {

        Order order = repository.getById(updateOrder.getId());

        if(order == null){
            throw new CustomException("Order was not existed");
        }

        order.setStatus(updateOrder.getStatus());

        return repository.save(order);

    }
}

