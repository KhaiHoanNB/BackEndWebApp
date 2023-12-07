package com.webapp.backend.controller;

import com.webapp.backend.dto.OrderDto;
import com.webapp.backend.dto.UpdateStatusOrder;
import com.webapp.backend.entity.Order;
import com.webapp.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService service;

    @GetMapping("/all/getOrder/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable(name = "orderId") Long orderId) throws Exception {

       Order order =  service.getOrder(orderId);

        return ResponseEntity.ok(order);

    }

    @GetMapping("/admin/getAllOrderByDate/{date}")
    public ResponseEntity<List<Order>> getOrder(@PathVariable(name = "date") String date) throws Exception {

        List<Order> orders =  service.getAllOrderByDate(date);

        if(orders.isEmpty()){
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);

    }

    @PostMapping(value = "/getAll")
    public ResponseEntity<List<Order>> getAll(@RequestBody OrderDto orderDto) {

        List<Order> list = service.getAll(orderDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @GetMapping("/all/getOrdersOfShipper/{shipperId}")
    public ResponseEntity<List<Order>> getOrdersOfShipper(@PathVariable(name = "shipperId") Long shipperId) throws Exception {

        List<Order> orders = service.getOrdersOfShipper(shipperId);

        if(orders.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);

    }

    @GetMapping("/all/getOrdersOfShipperByDate/{date}")
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

    @PostMapping("/admin/returnOrder/{orderId}")
    public ResponseEntity<Order> returnOrder(@PathVariable(name = "orderId") Long orderId) throws Exception {

        return ResponseEntity.ok(service.returnOrder(orderId));

    }

    @PutMapping("/all/updateOrder")
    public ResponseEntity<Order> updateOrder(@Valid @RequestBody OrderDto orderDto) throws Exception {

        return ResponseEntity.ok(service.updateOrder(orderDto));

    }

    @PutMapping("/admin/comfirmOrder/{id}")
    public ResponseEntity<Order> confirmOrder(@PathVariable(name = "id") Long id) throws Exception {

        return ResponseEntity.ok(service.confirmOrder(id));

    }

    @DeleteMapping("/all/deleteOrder/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable(name = "id") Long orderId,
                                              @RequestParam(name = "shipperId") Long shipperID) throws Exception {

        service.deleteOrder(orderId, shipperID);

        return ResponseEntity.ok("Delete order successfully");

    }

    @DeleteMapping("/admin/deleteAllOrder")
    public ResponseEntity<String> deleteAllOrder() throws Exception {

        service.deleteAllOrder();

        return ResponseEntity.ok("Add product successfully");

    }

    @PutMapping("/admin/updateOrder")
    public ResponseEntity<Order> updateOrder(@RequestBody UpdateStatusOrder updateOrder) throws Exception {

        Order order = service.updateOrder(updateOrder);

        if(order == null){
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(order);

    }


}
