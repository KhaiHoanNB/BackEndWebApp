package com.webapp.backend.service;


import com.webapp.backend.common.CustomException;
import com.webapp.backend.dto.CategoryDto;
import com.webapp.backend.entity.Category;
import com.webapp.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repository;

    public void saveCategory(CategoryDto categoryDto) throws Exception {

        if(categoryDto.getId() != null && repository.findById(categoryDto.getId()).isPresent()){
            throw new CustomException("The id of category is existed");
        }

        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());

        repository.save(category);
    }

    public void deleteCategory(Long id) throws Exception {

        Optional<Category> existedOrderOptional = repository.findById(id);

        if (!existedOrderOptional.isPresent()) {
            throw new CustomException("This category is not existed");
        }

        repository.deleteById(id);

    }

    public List<CategoryDto> getAllCategories() {
        List<Category> listCategories = repository.findAll();

        if (listCategories.isEmpty()) {
            return Collections.emptyList();
        }

        return listCategories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public Category getCategory(Long categoryId) {
        Optional<Category> categoryOptional = repository.findById(categoryId);

        if(categoryOptional.isPresent()){
            return categoryOptional.get();
        }

        return null;
    }

    public CategoryDto convertToDto(Category category) {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }
}





























