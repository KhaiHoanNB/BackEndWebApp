package com.webapp.backend.service;


import com.webapp.backend.entity.Category;
import com.webapp.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository repository;

    public void saveCategory(Category category) throws Exception {

        repository.save(category);
    }

    public void deleteCategory(Long id) throws Exception {

        Optional<Category> existedOrderOptional = repository.findById(id);

        if (!existedOrderOptional.isPresent()) {
            throw new Exception("This category is not existed");
        }

        repository.deleteById(id);

    }
}
