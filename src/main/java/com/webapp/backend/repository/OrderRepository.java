package com.webapp.backend.repository;

import com.webapp.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("SELECT o FROM Order o WHERE o.shipper.id = :shipperId")
    List<Order> findOrderByShipperId(@Param("shipperId") Long shipperId);

    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.confirmTime) = :targetDate")
    List<Order> findOrdersByDate(@Param("targetDate") LocalDate targetDate);


    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.confirmTime) = :targetDate AND o.shipper.id = :shipperId")
    List<Order> findOrdersByDateAndShipperId(
            @Param("targetDate") LocalDate targetDate,
            @Param("shipperId") Long shipperId
    );
}
