package com.improveId.Order.repository;

import com.improveId.Order.entity.DeliveryDetailsEntity;
import com.improveId.Order.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<DeliveryDetailsEntity,Long> {


    List<DeliveryDetailsEntity> findByDeliveryPersonId(Long id);

    List<DeliveryDetailsEntity> findByDeliveryPersonIdAndStatus(Long id, DeliveryStatus deliveryStatus);
}

