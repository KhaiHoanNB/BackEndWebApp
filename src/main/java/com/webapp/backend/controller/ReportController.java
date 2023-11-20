package com.webapp.backend.controller;


import com.webapp.backend.common.CustomException;
import com.webapp.backend.common.Report;
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

    @GetMapping("getAllReport/{date}")
    public ResponseEntity<List<Report>> getSuccessfulOrderByDate(@PathVariable(name = "date") String date){


        List<Report> listOrder = reportService.getAllReport(date);

        if(listOrder.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listOrder);

    }



    @GetMapping("getReportByDateAndShipper/{date}")
    public ResponseEntity<Report> getSuccessfulOrderByDateAndShipper(
                                        @PathVariable(name = "date") String date,
                                        @RequestParam(name = "shipperId") Long shipperId) throws CustomException {

        return ResponseEntity.ok(reportService.getOrdersByDateAndShipper(date, shipperId));

    }



}



    

