package com.improveid.User.controller;

import com.improveid.User.dto.AddressDto;
import com.improveid.User.dto.LoginRequest;
import com.improveid.User.dto.RegisterRequest;
import com.improveid.User.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @GetMapping("/list")
    public ResponseEntity<?> allUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        authService.deleteUser(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<?>getById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getUser(id));
    }
    @GetMapping("/idName")
    public ResponseEntity<Map<Long,String>> getIdName() {
        return ResponseEntity.ok(authService.getIdName());
    }
    @PostMapping("/addAddress")
    public ResponseEntity<String> addAddressToCustomer(@RequestBody AddressDto request) {
        authService.addAddress(request);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/getAddressById/{id}")
    public ResponseEntity<?>getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getAllAddressesById(id));
    }
    @PutMapping("/editAddressById/{id}")
    public ResponseEntity<?> addAddress(@RequestBody AddressDto request,@PathVariable Long id) {
        return ResponseEntity.ok(authService.updateAddress(request,id));
    }
    @DeleteMapping("/deleteAddress/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        authService.deleteAddress(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
