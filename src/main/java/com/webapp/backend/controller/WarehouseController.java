package com.webapp.backend.controller;


import com.webapp.backend.common.CustomException;
import com.webapp.backend.core.dto.RoleDto;
import com.webapp.backend.core.dto.UserDto;
import com.webapp.backend.core.entities.User;
import com.webapp.backend.dto.WarehouseDto;
import com.webapp.backend.entity.Warehouse;
import com.webapp.backend.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<Warehouse> updateProduct(@RequestBody WarehouseDto warehouseDto) throws Exception {

        return ResponseEntity.ok(service.updateProduct(warehouseDto));

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
