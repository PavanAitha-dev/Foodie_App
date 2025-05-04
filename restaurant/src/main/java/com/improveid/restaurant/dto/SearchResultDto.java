package com.improveid.restaurant.dto;

import com.improveid.restaurant.entity.Item;
import com.improveid.restaurant.entity.MenuCategory;
import com.improveid.restaurant.entity.Restaurant;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultDto {
    private List<Restaurant> restaurants;
    private List<MenuCategory> categories;
    private List<Item> items;
}
