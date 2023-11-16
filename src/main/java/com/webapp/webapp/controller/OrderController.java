package com.webapp.webapp.controller;

import com.webapp.webapp.entity.Order;
import com.webapp.webapp.entity.Warehouse;
import com.webapp.webapp.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

}
