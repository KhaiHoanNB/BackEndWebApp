package com.webapp.webapp.service;

import com.webapp.webapp.common.GlobalExceptionHandler;
import com.webapp.webapp.entity.Warehouse;
import com.webapp.webapp.repository.WarehouseRepository;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseService {

    private static final org.apache.logging.log4j.Logger LOGGER =  LogManager.getLogger(GlobalExceptionHandler.class);

    @Autowired
    WarehouseRepository repository;

    public void addProduct(Warehouse warehouse) {

        repository.save(warehouse);
        LOGGER.info("Add " + warehouse.toString());
    }

    public void deleteProduct(Long id) {

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

            throw new Exception("This prduct is not existed");

        }
    }

    public void deleteAllProduct() {
        repository.deleteAll();
    }
}
