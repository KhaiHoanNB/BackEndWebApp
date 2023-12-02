package com.webapp.backend.repository;

import com.webapp.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("SELECT o FROM Order o WHERE o.shipper.id = :shipperId")
    List<Order> findOrderByShipperId(@Param("shipperId") Long shipperId);

    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.confirmTime) = FUNCTION('DATE', :targetDate)")
    List<Order> findOrdersByDate(@Param("targetDate") LocalDate targetDate);


    @Query("SELECT o FROM Order o WHERE FUNCTION('DATE', o.confirmTime) = :targetDate AND o.shipper.id = :shipperId")
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
}
