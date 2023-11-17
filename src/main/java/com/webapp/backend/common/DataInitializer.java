package com.webapp.backend.common;

import com.webapp.backend.entity.Category;
import com.webapp.backend.entity.Role;
import com.webapp.backend.repository.CategoryRepository;
import com.webapp.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {

        initializeRoles();
        initializeCategory();
    }

    private void initializeCategory() {

        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(1L,"Hoa", "Tất cả các loại hoa tươi"));
            categoryRepository.save(new Category(2L,"Quả", "Tất cả các loại quả tươi"));
            categoryRepository.save(new Category(3L,"Thuốc", "Tất cả các loại thuốc"));
            categoryRepository.save(new Category(4L,"Sữa", "Tất cả các loại sữa"));

        }
    }

    private void initializeRoles() {

        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(1,"ROLE_SHIPPER", "Ship order to customer"));
            roleRepository.save(new Role(2,"ROLE_ADMIN", "Ship order to customer"));
        }
    }
}