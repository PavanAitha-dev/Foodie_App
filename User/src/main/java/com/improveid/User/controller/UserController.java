package com.improveid.User.controller;

import com.improveid.User.dto.AddressDto;
import com.improveid.User.dto.RegisterRequest;
import com.improveid.User.exception.NotFoundException;
import com.improveid.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) throws Exception {
//        return ResponseEntity.ok(userService.register(request));
//    }
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
//        return ResponseEntity.ok(userService.login(request));
//    }
    @GetMapping("/list")
    public ResponseEntity<?> allUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<?>getById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(userService.getUser(id));
    }
    @GetMapping("/idName")
    public ResponseEntity<Map<Long,String>> getIdName() {
        return ResponseEntity.ok(userService.getIdName());
    }
    @PostMapping("/addAddress")
    public ResponseEntity<String> addAddressToCustomer( @Valid @RequestBody AddressDto request) throws NotFoundException {
        userService.addAddress(request);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/getAddressById/{id}")
    public ResponseEntity<?>getAddressById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(userService.getAllAddressesById(id));
    }
    @PutMapping("/editAddressById/{id}")
    public ResponseEntity<?> addAddress( @Valid @RequestBody AddressDto request,@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(userService.updateAddress(request,id));
    }
    @DeleteMapping("/deleteAddress/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        userService.deleteAddress(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
