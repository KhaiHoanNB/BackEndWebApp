package com.webapp.backend.controller;

import com.webapp.backend.dto.OrderDto;
import com.webapp.backend.entity.Order;
import com.webapp.backend.service.OrderService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService service;

    @GetMapping("/getOrder/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable(name = "orderId") Long orderId) throws Exception {

       Order order =  service.getOrder(orderId);

        return ResponseEntity.ok(order);

    }

    @GetMapping("/getAllOrderByDate/{date}")
    public ResponseEntity<List<Order>> getOrder(@PathVariable(name = "date") String date) throws Exception {

        List<Order> orders =  service.getAllOrderByDate(date);

        if(orders.isEmpty()){
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);

    }

    @GetMapping("/getAllOrder")
    public ResponseEntity<List<Order>> getAllOrder() throws Exception {

        List<Order> orders = service.getAllOrder();

        if(orders.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);

    }

    @GetMapping("/getOrdersOfShipper/{shipperId}")
    public ResponseEntity<List<Order>> getOrdersOfShipper(@PathVariable(name = "shipperId") Long shipperId) throws Exception {

        List<Order> orders = service.getOrdersOfShipper(shipperId);

        if(orders.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);

    }

    @GetMapping("/getOrdersOfShipperByDate/{date}")
    public ResponseEntity<List<Order>> getOrdersOfShipper(@PathVariable(name = "date") String date,
                                                          @RequestParam(name = "shipperId") Long shipperId) throws Exception {

        List<Order> orders = service.getOrdersOfShipperByDate(shipperId,date);

        if(orders.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);

    }

    @PostMapping("/addOrder")
    public ResponseEntity<Order> addOrder(@RequestBody OrderDto orderDto) throws Exception {

        Order order = service.addOrder(orderDto);

        return ResponseEntity.ok(order);

    }

    @PostMapping("/returnOrder/{orderId}")
    public ResponseEntity<Order> returnOrder(@PathVariable(name = "orderId") Long orderId) throws Exception {

        return ResponseEntity.ok(service.returnOrder(orderId));

    }

    @PostMapping("/confirmReturnOrder/{orderId}")
    public ResponseEntity<Order> confirmReturnOrder(@PathVariable(name = "orderId") Integer orderId) throws Exception {

        return ResponseEntity.ok(service.confirmReturnOrder(orderId));

    }


    @PutMapping("/updateOrder")
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody OrderDto orderDto) throws Exception {

        return ResponseEntity.ok(service.updateOrder(orderDto));

    }

    @PutMapping("/comfirmOrder/{id}")
    public ResponseEntity<Order> confirmOrder(@PathVariable(name = "id") Long id) throws Exception {

        return ResponseEntity.ok(service.confirmOrder(id));

    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable(name = "id") Long orderId,
                                              @RequestParam(name = "shipperId") Long shipperID) throws Exception {

        service.deleteOrder(orderId, shipperID);

        return ResponseEntity.ok("Delete order successfully");

    }

    @DeleteMapping("/deleteAllOrder")
    public ResponseEntity<String> deleteAllOrder() throws Exception {

        service.deleteAllOrder();

        return ResponseEntity.ok("Add product successfully");

    }


}
