package com.webapp.backend.service;

import com.webapp.backend.common.CustomException;
import com.webapp.backend.common.GlobalExceptionHandler;
import com.webapp.backend.entity.Product;
import com.webapp.backend.entity.Warehouse;
import com.webapp.backend.repository.WarehouseRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    private static final org.apache.logging.log4j.Logger LOGGER =  LogManager.getLogger(GlobalExceptionHandler.class);

    @Autowired
    WarehouseRepository repository;

    public void addProduct(Warehouse warehouse) {

        warehouse.setCreatedTime(LocalDateTime.now());
        warehouse.setUpdatedTime(LocalDateTime.now());

        repository.save(warehouse);
        LOGGER.info("Add " + warehouse.toString());
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
