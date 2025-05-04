package com.improveid.restaurant.controller;

import com.improveid.restaurant.dto.*;
import com.improveid.restaurant.entity.Restaurant;
import com.improveid.restaurant.exception.RestaurantNotFoundException;
import com.improveid.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService Rservice;

    @PostMapping("/addRest")
    public ResponseEntity<?> addRestaurant(@RequestBody RestaurantCreationDto creationDto) {
        return ResponseEntity.ok(Rservice.createRestaurant(creationDto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateRestaurantStatus(@PathVariable Long id, @RequestBody RestaurantDto dto) throws RestaurantNotFoundException {
        Rservice.updateRestaurant(id,dto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/addItem")
    public ResponseEntity<?> addItem(@RequestBody ItemDto ItemDto) throws RestaurantNotFoundException {
        return ResponseEntity.ok(Rservice.addItem(ItemDto));
    }


    @GetMapping("/all")
    public ResponseEntity<?> allRestarants(){
        return ResponseEntity.ok(Rservice.findAll());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        Rservice.deleteRestaurant(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteItem/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        Rservice.deleteItem(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/by-user/{email}")
    public ResponseEntity<Restaurant>getRestaurantByEmail(@PathVariable String email){
      return ResponseEntity.ok(Rservice.find(email));
    }

    @GetMapping(path = "/byId/{id}",produces = "application/json")
    public ResponseEntity<RestaurantDto>getRestaurantById(@PathVariable Long id) throws RestaurantNotFoundException {
        return ResponseEntity.ok(Rservice.RestaurantById(id));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Void> updateRestaurantStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        Rservice.updateStatus(id, status);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/allItems/{restaurantId}")
    public ResponseEntity<?>getRestaurant(@PathVariable Long restaurantId){
        return ResponseEntity.ok(Rservice.findAllItems(restaurantId));
    }

    @GetMapping("/getNameAddress")
   public ResponseEntity<?> getRestaurantsIdNameAddress(){
        return ResponseEntity.ok(Rservice.getAllIdNameAddress());

    }


}

