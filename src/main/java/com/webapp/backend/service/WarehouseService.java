package com.webapp.backend.service;

import com.webapp.backend.common.CustomException;
import com.webapp.backend.dto.ProductDto;
import com.webapp.backend.dto.WarehouseDto;
import com.webapp.backend.entity.Category;
import com.webapp.backend.entity.Product;
import com.webapp.backend.entity.Warehouse;
import com.webapp.backend.repository.CategoryRepository;
import com.webapp.backend.repository.ProductRepository;
import com.webapp.backend.repository.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseService.class);

    @Autowired
    WarehouseRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Warehouse addProduct(WarehouseDto warehouseDto) throws CustomException {

        Warehouse warehouse = new Warehouse();

        Product product = new Product();

        ProductDto productDto = warehouseDto.getProductDto();

        Optional<Category> categoryOptional = categoryRepository.findById(warehouseDto.getProductDto().getCategoryId());

        if(!categoryOptional.isPresent()){
            throw new CustomException("The category is not existed");
        }

        if(warehouseDto.getQuantity() <= 0){
            throw new CustomException("Quantity must be bigger than 0");
        }

        product.setCategory(categoryOptional.get());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());

        warehouse.setCreatedTime(LocalDateTime.now());

        warehouse.setProduct(product);

        warehouse.setQuantity(warehouseDto.getQuantity());

        Warehouse savedWarehouse = repository.save(warehouse);

        LOGGER.info("Add " + warehouse.toString());

        return savedWarehouse;

    }

    public void deleteProduct(Long id) throws Exception {

        Optional<Warehouse> existingWarehouseOptional = repository.findById(id);

        if (!existingWarehouseOptional.isPresent()) {

            throw new CustomException("This product is not existed");
        }

        repository.deleteById(id);
    }

    public void updateProduct(Warehouse updatedWarehouse) throws Exception {

        Optional<Warehouse> existingWarehouseOptional = repository.findById(updatedWarehouse.getId());

        if (existingWarehouseOptional.isPresent()) {
            Warehouse existingWarehouse = existingWarehouseOptional.get();

            existingWarehouse.setQuantity(updatedWarehouse.getQuantity());
            existingWarehouse.setUpdatedTime(updatedWarehouse.getUpdatedTime());
            existingWarehouse.setProduct(updatedWarehouse.getProduct());

            repository.save(existingWarehouse);

        } else {

            throw new CustomException("This prduct is not existed");

        }
    }

    public void deleteAllProduct() {
        repository.deleteAll();
    }

    public List<Warehouse> getAllProduct() {

        return repository.findAll();
    }

    public Warehouse getProduct(Long warehousetId) {
        Optional<Warehouse> warehouseOptional = repository.findById(warehousetId);
        if(warehouseOptional.isPresent())
            return warehouseOptional.get();
        return null;
    }
}
