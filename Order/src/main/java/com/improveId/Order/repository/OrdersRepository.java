package com.improveId.Order.repository;

import com.improveId.Order.entity.OrderDetailsEntity;
import com.improveId.Order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<OrderDetailsEntity,Long> {

    List<OrderDetailsEntity>findByrestaurantId(Long id);

    List<OrderDetailsEntity> findBycustomerId(Long id);

    List<OrderDetailsEntity> findByOrderStatus(OrderStatus orderStatus);
}
