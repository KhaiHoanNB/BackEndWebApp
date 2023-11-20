package com.webapp.backend.service;


import com.webapp.backend.common.CustomException;
import com.webapp.backend.dto.CategoryDto;
import com.webapp.backend.entity.Category;
import com.webapp.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repository;

    public void saveCategory(CategoryDto categoryDto) throws Exception {

        Category category = new Category();

        repository.save(category);
    }

    public void deleteCategory(Long id) throws Exception {

        Optional<Category> existedOrderOptional = repository.findById(id);

        if (!existedOrderOptional.isPresent()) {
            throw new CustomException("This category is not existed");
        }

        repository.deleteById(id);

    }

    public List<Category> getAllCategories() {

        return repository.findAll();
    }

    public Category getCategory(Long categoryId) {
        Optional<Category> categoryOptional = repository.findById(categoryId);

        if(categoryOptional.isPresent()){
            return categoryOptional.get();
        }

        return null;
    }
}





























