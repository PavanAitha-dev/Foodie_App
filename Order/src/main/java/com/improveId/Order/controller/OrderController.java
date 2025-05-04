package com.improveId.Order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.improveId.Order.dto.DeliveryDto;
import com.improveId.Order.dto.OrderDto;
import com.improveId.Order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderSer;

    @PostMapping("/add")
    public ResponseEntity<?> place_Order(@RequestBody OrderDto order) {
        return ResponseEntity.ok(orderSer.placeOrder(order));
    }
    @GetMapping("/getAllOrders/{restaurantId}")
    public ResponseEntity<?> getAllOrdersByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderSer.getOrdersByRestaurentId(restaurantId));
    }
    @GetMapping("/getOrdersCustId/{customerId}")
    public ResponseEntity<?> getAllOrdersByCustomedId(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderSer.getOrdersByCustomerId(customerId));
    }
    @GetMapping("/report/{id}")
    public ResponseEntity<?> orderReport(@PathVariable Long id) {
        return ResponseEntity.ok(orderSer.getRestaurantById(id));
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<?> user(@PathVariable Long id) {
        return ResponseEntity.ok(orderSer.getUser(id));
    }
    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<Void>updateOrderStatus(@PathVariable Long id,@RequestBody Map<String, String> request) {
        String status = request.get("status");
        orderSer.updateStatus(id,status);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/pay/{method}")
    public ResponseEntity<?> pay(@RequestBody String request, @PathVariable("method") String method){
        orderSer.Payment(request,method);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/getActiveDeliveries")
    public ResponseEntity<?> getAllActiveOrders() {
        return ResponseEntity.ok(orderSer.getOrdersforDelivery());
    }
    @PostMapping("/addDelivery")
    public ResponseEntity<?> addDelivery(@RequestBody DeliveryDto deliveryDto) {
        orderSer.placeDelivery(deliveryDto);
        return  ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/activeDeliveries/{id}")
    public ResponseEntity<?> getAllActiveOrdersByDeliveryPersonId(@PathVariable Long id) {
        return ResponseEntity.ok(orderSer.getAllActiveDeliveries(id));
    }
    @PutMapping("/updateDeliveries/{id}")
    public ResponseEntity<Void>updateDeliveryStatus(@PathVariable Long id,@RequestBody Map<String, String> request) {
        String status = request.get("status");
        orderSer.updateDeliveryStatus(id,status);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/allDeliveriesById/{id}")
    public ResponseEntity<?> getAllDeliveriesByDeliveryPersonId(@PathVariable Long id) {
        return ResponseEntity.ok(orderSer.getAllDeliveries(id));
    }


}
