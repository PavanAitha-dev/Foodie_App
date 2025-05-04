package com.improveid.restaurant.repository;

import com.improveid.restaurant.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingIgnoreCase(String namePart);
//    List<Item> findByRestaurantId(Long restaurantId);
//
//    // Find items by category for a specific restaurant
//    List<Item> findByRestaurantIdAndCategory(Long restaurantId, String category);
//
//    // Find vegetarian items for a restaurant
//    List<Item> findByRestaurantIdAndIsVegetarianTrue(Long restaurantId);
//
//    // Find available items for a restaurant
//    List<Item> findByRestaurantIdAndIsAvailableTrue(Long restaurantId);
//
//    // Count menu items per restaurant
//    @Query("SELECT COUNT(m) FROM MenuItem m WHERE m.restaurant.id = :restaurantId")
//    long countByRestaurantId(@Param("restaurantId") Long restaurantId);
//
//    // Find items in price range for a restaurant
//    List<Item> findByRestaurantIdAndPriceBetween(
//            Long restaurantId,
//            Double minPrice,
//            Double maxPrice
//    );
    List<Item> findAllByCategoryRestaurantId(Long restaurantId);
}