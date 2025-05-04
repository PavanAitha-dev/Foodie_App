package com.improveid.restaurant.repository;

import com.improveid.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {

//    List<Restaurant> findAllOrderByIdAsc();
    //    List<Restaurant> findByIsActiveTrue();4
   // Find restaurants by name (case insensitive)
   List<Restaurant> findByNameContainingIgnoreCase(String name);

    List<Restaurant> findByNameContainingOrAddressContainingAllIgnoreCase(String nameQuery, String addressQuery);


//    @Query(value = "SELECT rst FROM restaurant rst WHERE rst.userid = :id")
//    Optional<Restaurant> findByuserId(Long id);

    Restaurant findByEmail(String email);
    //
//    // Find restaurants with menu items containing search term
//    @Query("SELECT DISTINCT r FROM Restaurant r JOIN r.menuItems m WHERE " +
//            "LOWER(r.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
//            "LOWER(m.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
//    List<Restaurant> searchRestaurantsAndMenuItems(@Param("searchTerm") String searchTerm);
//
//    // Check if restaurant exists by name
//    boolean existsByName(String name);
}
