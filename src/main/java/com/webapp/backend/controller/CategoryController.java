package com.webapp.backend.controller;


import com.webapp.backend.dto.CategoryDto;
import com.webapp.backend.entity.Category;
import com.webapp.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Validated
public class CategoryController {

    @Autowired
    CategoryService service;

    @PostMapping("/addCategory")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDto categoryDto) throws Exception {

        service.saveCategory(categoryDto);

        return ResponseEntity.ok("Add category successfully");

    }

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable (name = "id") Long id) throws Exception {

        service.deleteCategory(id);

        return ResponseEntity.ok("Delete category successfully");

    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<Category>> getAllCategories() throws Exception {

        List<Category> categories = service.getAllCategories();

        if(categories.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categories);

    }

    @GetMapping("/getCategory/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable(name = "categoryId") Long categoryId) throws Exception {

        Category category = service.getCategory(categoryId);

        if(category == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(category);

    }

}
