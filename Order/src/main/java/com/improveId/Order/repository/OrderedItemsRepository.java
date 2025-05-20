package com.improveId.Order.repository;

import com.improveId.Order.entity.ItemsDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedItemsRepository extends JpaRepository<ItemsDetailsEntity,Long> {

}
