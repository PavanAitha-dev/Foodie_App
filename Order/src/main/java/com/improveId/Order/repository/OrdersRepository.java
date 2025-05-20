package com.improveId.Order.repository;

import com.improveId.Order.entity.OrderDetailsEntity;
import com.improveId.Order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<OrderDetailsEntity,Long> {

    List<OrderDetailsEntity>findByrestaurantId(Long id);

    List<OrderDetailsEntity> findBycustomerId(Long id);

    List<OrderDetailsEntity> findByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT o FROM OrderDetailsEntity o WHERE o.orderTimestamp >= :startOfDay")
    List<OrderDetailsEntity> todayOrdersByTimestamp(@Param("startOfDay") LocalDateTime startOfDay);

}
