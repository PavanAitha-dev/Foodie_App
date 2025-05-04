package com.improveid.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Item name is required")
    @Size(max = 100, message = "Item name must be less than 100 characters")
    private String name;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price must be at least 0")
    @DecimalMax(value = "1000.0", message = "Price must be less than 1000")
    private Double price;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable = true;

    @NotNull(message = "Vegetarian status is required")
    private Boolean isVegetarian;

    @NotNull(message = "Category is required")
    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private MenuCategory category;
}