package com.webapp.backend.repository;

import com.webapp.backend.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {


    @Query("SELECT w FROM Warehouse w WHERE w.product.id = :productId")
    Warehouse findByProductId(@Param("productId") long productId);
}
