package com.webapp.backend.controller;


import com.webapp.backend.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.webapp.backend.service.ReportService;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    ReportService reportService;

    @GetMapping("getSuccessfulOrderByDate/{date}")
    public ResponseEntity<List<Order>> getSuccessfulOrderByDate(@PathVariable(name = "date") String date){


        List<Order> listOrder = reportService.getSuccessfulOrderByDate(date);

        if(listOrder.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listOrder);

    }



    @GetMapping("getSuccessfulOrderByDateAndShipper/{date}")
    public ResponseEntity<List<Order>> getSuccessfulOrderByDateAndShipper(
                                        @PathVariable(name = "date") String date,
                                        @RequestParam(name = "shipperId") Long shipperId){


        List<Order> listOrder = reportService.getSuccessfulOrderByDateAndShipper(date, shipperId);

        if(listOrder.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listOrder);

    }



}



    

