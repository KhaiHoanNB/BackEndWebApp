package com.webapp.backend.common;

import com.webapp.backend.core.entities.Role;
import com.webapp.backend.core.entities.User;
import com.webapp.backend.core.repositories.RoleRepository;
import com.webapp.backend.core.repositories.UserRepository;
import com.webapp.backend.core.service.RoleService;
import com.webapp.backend.core.service.UserService;
import com.webapp.backend.entity.Category;
import com.webapp.backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        initializeRoles();
        initializeCategory();
        initializeUser();
    }

    private void initializeUser() {
        if (userRepository.count() == 0) {
            for (int i = 0; i < 2; i++) {
                User user;
                if (i == 0) {
                    user = User.builder()
                            .firstName("Tran")
                            .lastName("Viet")
                            .username("tranviet")
                            .password(passwordEncoder.encode("tranviet@1983"))
                            .build();
                } else {
                    user = User.builder()
                            .firstName("Tran")
                            .lastName("Phuong")
                            .username("vietphuong")
                            .password(passwordEncoder.encode("vietphuong@123"))
                            .build();
                }

                List<Role> list = new ArrayList<>();

                Role role = roleService.getByName("ROLE_ADMIN");

                list.add(role);

                user.setRoles(list);

                userService.save(user);

            }
        }
    }

    private void initializeCategory() {

        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(1L, "Hoa", "Tất cả các loại hoa tươi"));
            categoryRepository.save(new Category(2L, "Quả", "Tất cả các loại quả tươi"));
            categoryRepository.save(new Category(3L, "Thuốc", "Tất cả các loại thuốc"));
            categoryRepository.save(new Category(4L, "Sữa", "Tất cả các loại sữa"));

        }
    }

    private void initializeRoles() {

        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_SHIPPER", "Ship order to customer"));
            roleRepository.save(new Role("ROLE_ADMIN", "Manager"));
        }
    }
}