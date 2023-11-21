package com.webapp.backend.controller;


import com.webapp.backend.common.CustomException;
import com.webapp.backend.dto.WarehouseDto;
import com.webapp.backend.entity.Warehouse;
import com.webapp.backend.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@Validated
public class WarehouseController {

    @Autowired
    WarehouseService service;


    @PostMapping("/addProduct")
    public ResponseEntity<Warehouse> addProduct( @Valid @RequestBody WarehouseDto warehouseDto) throws CustomException {

        return ResponseEntity.ok(service.addProduct(warehouseDto));

    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = ("id")) Long id) throws Exception {

        service.deleteProduct(id);

        return ResponseEntity.ok("Delete product successfully");

    }

    @PutMapping("/updateProduct")
    public ResponseEntity<String> updateProduct(@Valid @RequestBody Warehouse warehouse) throws Exception {

        service.updateProduct(warehouse);

        return ResponseEntity.ok("Updated product successfully");

    }

    @DeleteMapping("/deleteAllProduct")
    public ResponseEntity<String> updateProduct() throws Exception {

        service.deleteAllProduct();

        return ResponseEntity.ok("Deleted all products successfully");

    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<List<Warehouse>> getAllProduct() throws Exception {

        List<Warehouse> products =  service.getAllProduct();

        if(products.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);

    }

    @GetMapping("/getProduct/{warehousetId}")
    public ResponseEntity<Warehouse> getProduct(@PathVariable(name = "warehousetId") Long warehousetId) throws Exception {

        Warehouse product =  service.getProduct(warehousetId);

        if(product == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(product);

    }

}
