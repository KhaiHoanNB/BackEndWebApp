package com.webapp.backend.controller;


import com.webapp.backend.common.CustomException;
import com.webapp.backend.dto.ReportByProductDto;
import com.webapp.backend.dto.ReportDto;
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

    @GetMapping("/admin/getAllReport/{date}")
    public ResponseEntity<List<ReportDto>> getSuccessfulOrderByDate(@PathVariable(name = "date") String date){


        List<ReportDto> listOrder = reportService.getAllReport(date);

        if(listOrder.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listOrder);

    }


    @GetMapping("/admin/getAllReportByProduct/{date}")
    public ResponseEntity<List<ReportByProductDto>> getReportByProduct(@PathVariable(name = "date") String date){


        List<ReportByProductDto> listOrder = reportService.getReportByProduct(date);

        if(listOrder.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listOrder);

    }



    @GetMapping("/all/getReportByDateAndShipper/{date}")
    public ResponseEntity<ReportDto> getSuccessfulOrderByDateAndShipper(
                                        @PathVariable(name = "date") String date,
                                        @RequestParam(name = "shipperId") Long shipperId) throws CustomException {

        return ResponseEntity.ok(reportService.getOrdersByDateAndShipper(date, shipperId));

    }


    @GetMapping("/admin/getTotalAmountByDay/{date}")
    public ResponseEntity<Long> getTotalAmountByDay(@PathVariable(name = "date") String date){


        return ResponseEntity.ok(reportService.getTotalAmountByDay(date));
    }


    @GetMapping("/all/getTotalAmountByDayAndShipper/{date}")
    public ResponseEntity<Long> getTotalAmountByDayAndShipper(@PathVariable(name = "date") String date,
                                                                @RequestParam(name = "shipperId") Long shipperId){


        return ResponseEntity.ok(reportService.getTotalAmountByDayAndShipper(date, shipperId));
    }


}



    

