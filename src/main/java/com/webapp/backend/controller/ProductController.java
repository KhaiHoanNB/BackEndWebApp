package com.webapp.backend.controller;


import com.webapp.backend.common.CustomException;
import com.webapp.backend.dto.ProductDto;
import com.webapp.backend.entity.Product;
import com.webapp.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
@Validated
public class ProductController {

    @Autowired
    ProductService service;


    @PostMapping("/admin/addProduct")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductDto productDto) throws CustomException {

        return ResponseEntity.ok(service.addProduct(productDto));

    }

    @DeleteMapping("/admin/deleteProduct/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = ("id")) Long id) throws Exception {

        service.deleteProduct(id);

        return ResponseEntity.ok("Delete product successfully");

    }

    @PutMapping("/admin/updateProduct")
    public ResponseEntity<Product> updateProduct(@RequestBody ProductDto productDto) throws Exception {

        return ResponseEntity.ok(service.updateProduct(productDto));

    }

    @DeleteMapping("/admin/deleteAllProduct")
    public ResponseEntity<String> updateProduct() throws Exception {

        service.deleteAllProduct();

        return ResponseEntity.ok("Deleted all products successfully");

    }

    @GetMapping("/all/getAllProduct")
    public ResponseEntity<List<Product>> getAllProduct() throws Exception {

        List<Product> products =  service.getAllProduct();

        if(products.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(products);

    }

    @GetMapping("/all/getProduct/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable(name = "productId") Long productId) throws Exception {

        Product product =  service.getProduct(productId);

        if(product == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(product);

    }
}
