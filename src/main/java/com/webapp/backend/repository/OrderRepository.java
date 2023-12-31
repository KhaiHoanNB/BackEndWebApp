package com.webapp.backend.repository;

import com.webapp.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("SELECT o FROM Order o WHERE o.shipper.id = :shipperId")
    List<Order> findOrderByShipperId(@Param("shipperId") Long shipperId);

    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.createDate) = FUNCTION('DATE', :targetDate)")
    List<Order> findOrdersByDate(@Param("targetDate") LocalDate targetDate);


    @Query("SELECT o FROM Order o WHERE (FUNCTION('DATE', o.createDate) = :targetDate OR FUNCTION('DATE', o.confirmTime) = :targetDate) AND o.shipper.id = :shipperId")
    List<Order> findOrdersByDateAndShipperIdForDisplay(
            @Param("targetDate") LocalDate targetDate,
            @Param("shipperId") Long shipperId
    );

    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.createDate) = :targetDate AND o.shipper.id = :shipperId AND o.status = 1")
    List<Order> findOrdersByDateAndShipperId(
            @Param("targetDate") LocalDate targetDate,
            @Param("shipperId") Long shipperId
    );

    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.confirmTime) = :targetDate AND o.status = :status")
    List<Order> findOrdersByDateAndStatus(@Param("targetDate") LocalDate targetDate,
                                          @Param("status") Integer status);

    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.confirmTime) = :targetDate AND o.shipper.id = :shipperId AND o.status = :status")
    List<Order> findOrdersByDateAndShipperIdAndStatus(
            @Param("targetDate") LocalDate targetDate,
            @Param("shipperId") Long shipperId,
            @Param("status") Integer status
    );

    @Query("SELECT o.product.id as productId, SUM(o.quantity) as totalQuantity, SUM(o.cash) as totalCash FROM Order o WHERE FUNCTION('DATE', o.createDate) = FUNCTION('DATE', :targetDate) AND o.status IN (1, 3) AND o.product.id = :productId GROUP BY o.product.id")
    Map<String, Object> getReportByProduct(@Param("targetDate") LocalDate targetDate, @Param("productId") Long productId);

}
