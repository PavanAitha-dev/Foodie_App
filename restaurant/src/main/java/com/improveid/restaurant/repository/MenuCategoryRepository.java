package com.improveid.restaurant.repository;

import com.improveid.restaurant.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory,Long> {
    List<MenuCategory> findByNameContainingIgnoreCase(String name);

    //@Query(value = "SELECT * FROM menu_category WHERE restaurant_id = :restaurantId", nativeQuery = true)
    @Query("SELECT mc FROM MenuCategory mc WHERE restaurant.id = :restaurantId")
    List<MenuCategory> findAllByRestaurantIdNative(@Param("restaurantId") Long restaurantId);

    Optional<MenuCategory> findByNameContainingAndRestaurantId(String name, Long id);



}
