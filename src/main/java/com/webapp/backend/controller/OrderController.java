package com.webapp.backend.controller;

import com.webapp.backend.entity.Order;
import com.webapp.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Validated
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
    public ResponseEntity<String> addOrder(@Valid @RequestBody Order order) throws Exception {

        service.addOrder(order);

        return ResponseEntity.ok("Add product successfully");

    }

    @PostMapping("/returnOrder/{orderId}")
    public ResponseEntity<String> returnOrder(@PathVariable(name = "orderId") Long orderId) throws Exception {

        service.returnOrder(orderId);

        return ResponseEntity.ok("Return order successfully");

    }

    @PostMapping("/confirmReturnOrder/{orderId}")
    public ResponseEntity<String> confirmReturnOrder(@PathVariable(name = "orderId") Long orderId) throws Exception {

        service.confirmReturnOrder(orderId);

        return ResponseEntity.ok("Return order successfully");

    }


    @PutMapping("/updateOrder")
    public ResponseEntity<String> updateOrder(@Valid @RequestBody Order order) throws Exception {

        service.updateOrder(order);

        return ResponseEntity.ok("Add product successfully");

    }

    @PutMapping("/comfirmOrder/{id}")
    public ResponseEntity<String> confirmOrder(@PathVariable(name = "id") Long id) throws Exception {

        service.confirmOrder(id);

        return ResponseEntity.ok("Confirm product successfully");

    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable(name = "id") Long orderId,
                                              @RequestParam(name = "shipperId") Long shipperID) throws Exception {

        service.deleteOrder(orderId, shipperID);

        return ResponseEntity.ok("Add product successfully");

    }

    @DeleteMapping("/deleteAllOrder")
    public ResponseEntity<String> deleteAllOrder() throws Exception {

        service.deleteAllOrder();

        return ResponseEntity.ok("Add product successfully");

    }


}
