package com.webapp.backend.repository;

import com.webapp.backend.entity.ImportProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;

@Repository
public interface ImportProductRepository extends JpaRepository<ImportProduct, Long> {

    @Query("SELECT i.product.id as productId, SUM(i.quantity) as totalQuantity FROM ImportProduct i WHERE FUNCTION('DATE', i.createDate) = FUNCTION('DATE', :targetDate) AND i.product.id = :productId GROUP BY i.product.id")
    Map<String, Object> getDailyImport(@Param("targetDate") LocalDate targetDate, @Param("productId") Long productId);

}
