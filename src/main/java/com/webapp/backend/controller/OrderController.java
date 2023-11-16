package com.webapp.backend.controller;

import com.webapp.backend.entity.Order;
import com.webapp.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@Validated
public class OrderController {

        @Autowired
        OrderService service;

        @PostMapping("/addOrder")
        public ResponseEntity<String> addOrder(@Valid @RequestBody Order order) {

            service.addOrder(order);

            return ResponseEntity.ok("Add product successfully");

        }


        @PutMapping("/updateOrder")
        public ResponseEntity<String> updateOrder(@Valid @RequestBody Order order) throws Exception {

            service.updateOrder(order);

            return ResponseEntity.ok("Add product successfully");

        }

        @PutMapping("/comfirmOrder/{id}")
        public ResponseEntity<String> confirmOrder(@PathVariable(name = "id") Long id) throws Exception {

            service.confirmOrder(id);

            return ResponseEntity.ok("Add product successfully");

        }

        @DeleteMapping("/deleteOrder/{id}")
        public ResponseEntity<String> deleteOrder(@PathVariable(name = "id") Long id) throws Exception {

            service.deleteOrder(id);

            return ResponseEntity.ok("Add product successfully");

        }

    @DeleteMapping("/deleteAllOrder")
    public ResponseEntity<String> deleteAllOrder() throws Exception {

        service.deleteAllOrder();

        return ResponseEntity.ok("Add product successfully");

    }

}
