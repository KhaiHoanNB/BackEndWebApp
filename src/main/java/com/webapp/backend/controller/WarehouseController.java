package com.webapp.backend.controller;


import com.webapp.backend.entity.Warehouse;
import com.webapp.backend.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warehouse")
@Validated
public class WarehouseController {

    @Autowired
    WarehouseService service;


    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct( @Valid @RequestBody Warehouse warehouse, BindingResult bindingResult) {

        service.addProduct(warehouse);

        return ResponseEntity.ok("Add product successfully");

    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = ("id")) Long id) {

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

}
