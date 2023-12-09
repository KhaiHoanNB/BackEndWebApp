package com.webapp.backend.controller;


import com.webapp.backend.common.CustomException;
import com.webapp.backend.dto.CategoryDto;
import com.webapp.backend.entity.Category;
import com.webapp.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class CategoryController {

    @Autowired
    CategoryService service;

    @PostMapping("/admin/addCategory")
    public ResponseEntity<Category> addCategory(@RequestBody @Valid CategoryDto categoryDto, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            throw new CustomException("Check data payload");
        }

        return ResponseEntity.ok(service.saveCategory(categoryDto));

    }

    @DeleteMapping("/admin/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable (name = "id") Long id) throws Exception {

        service.deleteCategory(id);

        return ResponseEntity.ok("Delete category successfully");

    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() throws Exception {

        List<CategoryDto> categorieDtos = service.getAllCategories();

        if(categorieDtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorieDtos);

    }

    @GetMapping("/all/getCategory/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable(name = "categoryId") Long categoryId) throws Exception {

        Category category = service.getCategory(categoryId);

        if(category == null){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(category);

    }

}
