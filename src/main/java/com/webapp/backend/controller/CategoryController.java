package com.webapp.backend.controller;


import com.webapp.backend.entity.Category;
import com.webapp.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@Validated
public class CategoryController {

    @Autowired
    CategoryService service;

    @PostMapping("/addCategory")
    public ResponseEntity<String> addCategory(@Valid @RequestBody Category category) throws Exception {

        service.saveCategory(category);

        return ResponseEntity.ok("Add category successfully");

    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable (name = "id") Long id) throws Exception {

        service.deleteCategory(id);

        return ResponseEntity.ok("Delete category successfully");

    }

}
