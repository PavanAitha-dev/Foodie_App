package com.improveid.restaurant.service;

import com.improveid.restaurant.dto.*;
import com.improveid.restaurant.entity.Item;
import com.improveid.restaurant.entity.MenuCategory;
import com.improveid.restaurant.entity.Restaurant;
import com.improveid.restaurant.exception.RestaurantNotFoundException;
import com.improveid.restaurant.repository.MenuCategoryRepository;
import com.improveid.restaurant.repository.ItemRepository;
import com.improveid.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ItemRepository itemRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    public Restaurant createRestaurant(RestaurantCreationDto dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setAddress(dto.getAddress());
        restaurant.setContactNumber(dto.getContactNumber());
        restaurant.setOpeningHours(dto.getOpeningHours());
        restaurant.setEmail(dto.getEmail());
        restaurant.setStatus("ON_HOLD");
        return restaurantRepository.save(restaurant);
    }
    public Restaurant updateRestaurant(Long id,RestaurantDto dto) throws RestaurantNotFoundException {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if(restaurant.isEmpty()){
            throw new RestaurantNotFoundException("Restaurant not found in updation"+id);
        }
        restaurant.get().setName(dto.getName());
        restaurant.get().setAddress(dto.getAddress());
        restaurant.get().setContactNumber(dto.getContactNumber());
        restaurant.get().setOpeningHours(dto.getOpeningHours());
        return restaurantRepository.save(restaurant.get());
    }

    public MenuCategory addCategory(MenuCategoryDto Cdto) throws RestaurantNotFoundException {
        Optional<Restaurant> restaurant = restaurantRepository.findById(Cdto.getRestaurantId());

        if (restaurant.isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant not Found "+Cdto.getRestaurantId());
        }
        MenuCategory cat = new MenuCategory();
        cat.setName(Cdto.getName());
        cat.setRestaurant(restaurant.get());
        return menuCategoryRepository.save(cat);
    }

    public Item addItem(ItemDto dto) throws RestaurantNotFoundException {
        Optional<MenuCategory> menuCategory=menuCategoryRepository.findByNameContainingAndRestaurantId(dto.getCategory(),dto.getRestaurantId());
        MenuCategory menuCategoryfinal=null;
        if(menuCategory.isPresent()){
            menuCategoryfinal=menuCategory.get();
        }else {
            menuCategoryfinal= addCategory(new MenuCategoryDto(dto.getCategory(),dto.getRestaurantId()));
        }

        Item item = new Item();
        if(dto.getItemId()!=null);
        item.setId(dto.getItemId());
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setDescription(dto.getDescription());
        item.setIsVegetarian(dto.getIsVegetarian());
        item.setCategory(menuCategoryfinal);

        return itemRepository.save(item);
    }
    public List<RestaurantDto> findAll() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<RestaurantDto> list = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            RestaurantDto dto = new RestaurantDto();
            dto.setId(restaurant.getId());
            dto.setName(restaurant.getName());
            dto.setEmail(restaurant.getEmail());
            dto.setAddress(restaurant.getAddress());
            dto.setRating(restaurant.getAverageRating());
            dto.setContactNumber(restaurant.getContactNumber());
            dto.setOpeningHours(restaurant.getOpeningHours());
            dto.setStatus(restaurant.getStatus());
                list.add(dto);
        }
//        list.sort((a,b)-> Math.toIntExact(a.getId() - b.getId()));
        return list;
    }

    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }


    public Restaurant find(String email) {
        Restaurant restaurant = restaurantRepository.findByEmail(email);
        return restaurant;
    }

    public void updateStatus(Long id, String status) throws RestaurantNotFoundException {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found"+id));
        restaurant.setStatus(status);
        restaurantRepository.save(restaurant);
    }

    public List<ItemDto> findAllItems(Long restaurantId) {
        List<Item> items = itemRepository.findAllByCategoryRestaurantId(restaurantId);
        items.sort((x,y)-> Math.toIntExact(x.getId() - y.getId()));

        return items.stream().map(item -> ItemDto.builder()
                 .category(item.getCategory().getName())
                 .name(item.getName())
                 .description(item.getDescription())
                 .price(item.getPrice())
                 .isVegetarian(item.getIsVegetarian())
                        .itemId(item.getId())
                        .restaurantId(item.getCategory().getRestaurant().getId())
                 .build())
                .toList();

    }

    public RestaurantDto RestaurantById(Long id) throws RestaurantNotFoundException {
        Optional<Restaurant> rest=restaurantRepository.findById(id);
        if (rest.isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant not Found "+id);
        }
        Restaurant restaurant=rest.get();
        RestaurantDto dto = new RestaurantDto();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setEmail(restaurant.getEmail());
        dto.setAddress(restaurant.getAddress());
        dto.setRating(restaurant.getAverageRating());
        dto.setContactNumber(restaurant.getContactNumber());
        dto.setOpeningHours(restaurant.getOpeningHours());
        dto.setStatus(restaurant.getStatus());
        return dto;
    }

    public  Map<Long, recordRestaurant> getAllIdNameAddress() {
        List<Restaurant> restaurants=restaurantRepository.findAll();
        Map<Long, recordRestaurant> restaurantsData=new HashMap<>();
        for (Restaurant restaurant:restaurants){
            restaurantsData.put(restaurant.getId(),new recordRestaurant(restaurant.getName(), restaurant.getAddress()));
        }
        return restaurantsData;
    }
}
