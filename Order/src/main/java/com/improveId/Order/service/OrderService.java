package com.improveId.Order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.improveId.Order.dto.*;
import com.improveId.Order.entity.*;
import com.improveId.Order.feignClient.EmailClient;
import com.improveId.Order.feignClient.PaymentClient;
import com.improveId.Order.feignClient.RestaurantClient;
import com.improveId.Order.feignClient.UserClient;
import com.improveId.Order.repository.DeliveryRepository;
import com.improveId.Order.repository.OrderedItemsRepository;
import com.improveId.Order.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrdersRepository ordersRepository;
    private final OrderedItemsRepository itemsRepo;
    private final RestaurantClient restaurantClient;
    private final DeliveryRepository deliveryRepository;
    private final UserClient userClient;
    private final PaymentClient paymentClient;
    private final EmailClient emailClient;

    @Autowired
    ObjectMapper objectMapper;

    @Transactional
    public OrderDetailsEntity placeOrder(OrderDto orderDto) {
        try {
            log.info("placing order for customerId:{} and resturnatId:{}", orderDto.getCustomerId(), orderDto.getRestaurantId());

            OrderDetailsEntity orderDetails = new OrderDetailsEntity();
            orderDetails.setCustomerId(orderDto.getCustomerId());
            orderDetails.setRestaurantId(orderDto.getRestaurantId());
            orderDetails.setPaymentStatus(PaymentStatus.valueOf(orderDto.getPaymentStatus()));
            orderDetails.setTotalPrice(orderDto.getTotalPrice());
            orderDetails.setOrderStatus(OrderStatus.PLACED);
            orderDetails.setDeliveryAddress(orderDto.getDeliveryAddress());
            List<ItemsDetailsEntity> itemsDetailsEntities = getItemsDetailsEntities(orderDto, orderDetails);
            orderDetails.setOrderedItems(itemsDetailsEntities);
            orderDetails.setOrderTimestamp(LocalDateTime.now());
            orderDetails = ordersRepository.save(orderDetails);

            return orderDetails;
        } catch (Exception e) {
            log.error("Error accoured at Placing order"+e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private List<ItemsDetailsEntity> getItemsDetailsEntities(OrderDto orderDto, OrderDetailsEntity orderDetails) {
        log.info("getItemsDetailsEntities" + orderDto.getOrderedItem());
        List<ItemsDetailsEntity> itemsDetailsEntities = new ArrayList<>();
        for (ItemDto itemDto : orderDto.getOrderedItem()) {
            ItemsDetailsEntity itemEntity = new ItemsDetailsEntity();
            itemEntity.setItemName(itemDto.getItemName());
            itemEntity.setItemPrice(itemDto.getItemPrice());
            itemEntity.setItemId(itemDto.getItemId());
            itemEntity.setOrder(orderDetails);
            itemEntity.setQuantity(itemDto.getQuantity());
            itemsDetailsEntities.add(itemEntity);
        }
        return itemsDetailsEntities;
    }

    public List<OrderDetailsDto> getOrdersByRestaurentId(Long restaurantId) {
        try
    {
        log.info("getOrdersByRestaurentId" + restaurantId);
        Map<Long, String> data = userClient.getIdName();
        List<OrderDetailsEntity> orders = ordersRepository.findByrestaurantId(restaurantId);
        orders.sort((a, b) -> Math.toIntExact(a.getId() - b.getId()));
        List<OrderDetailsDto> orderDetailsDtos = new ArrayList<>();
        for (OrderDetailsEntity order : orders) {
            OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
            List<ItemDetailsDto> itemDetailsDtos = new ArrayList<>();
            orderDetailsDto.setId(order.getId());
            orderDetailsDto.setTotalPrice(order.getTotalPrice());
            orderDetailsDto.setPaymentStatus(order.getPaymentStatus());
            orderDetailsDto.setOrderStatus(order.getOrderStatus());
            orderDetailsDto.setCustomerName(data.get(order.getCustomerId()));
            orderDetailsDto.setDeliveryPersonName(data.get(order.getDeliveryPersonId()));
            for (ItemsDetailsEntity itemsDetailsEntity : order.getOrderedItems()) {
                ItemDetailsDto itemDto = new ItemDetailsDto();
                itemDto.setName(itemsDetailsEntity.getItemName());
                itemDto.setPrice(itemsDetailsEntity.getItemPrice());
                itemDto.setId(itemsDetailsEntity.getId());
                itemDto.setQuantity(itemsDetailsEntity.getQuantity());
                itemDetailsDtos.add(itemDto);
            }
            orderDetailsDto.setOrderedItems(itemDetailsDtos);
            log.debug("orderDetailsDto:{}", orderDetailsDto.toString());
            orderDetailsDtos.add(orderDetailsDto);
        }
        orderDetailsDtos.sort((a, b) -> Math.toIntExact(b.getId() - a.getId()));
        return orderDetailsDtos;
    } catch (Exception e) {
            log.error("Error accoured at getOrdersByRestaurentId"+e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public RestaurantDto getRestaurantById(Long id) {
        try {
            log.info("getRestaurantById" + id);
            RestaurantDto restaurant = restaurantClient.getRestaurantById(id);
            log.debug("restaurant:{}", restaurant);
            return restaurant;
        }
        catch (Exception e) {
            log.error("Error accoured at getRestaurantById"+e.getMessage());
            throw new RuntimeException(e);
        }
    }
    public UserProfileDto getUser(Long id) {
        log.info("getUser" + id);
        UserProfileDto user = userClient.getById(id);
        log.debug("user:{}",user);
        return user;
    }

    public PaymentDto Payment(String request, String method) {
        try {
            log.info("Payment " + method + "Data" + request);
            PaymentDto result = paymentClient.pay(request, method);
            OrderDetailsEntity orderdata = null;
            if (result != null) {
                Optional<OrderDetailsEntity> entity = ordersRepository.findById(result.getOrderID());
                entity.get().setPaymentStatus(result.getStatus());
                orderdata = ordersRepository.save(entity.get());
            }
//        if(result.getStatus().equals(PaymentStatus.SUCCESS))
//        {
//            UserProfileDto cutomer=getUser(result.getCustomerID());
//            RestaurantDto restaurant=getRestaurantById(result.getRestaurantID());
//            MailDto mail=new MailDto();
//            mail.setFullName(cutomer.getFullName());
//            mail.setMailId(cutomer.getEmail());
//            mail.setAmount(result.getAmount());
//            mail.setOrderId(result.getOrderID());
//            mail.setRestaurantName(restaurant.getName());
//            emailClient.sendEmail(mail);
//        }

            return result;
        } catch (RuntimeException e) {
            log.error(" Error Message"+e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public void updateStatus(Long id,String request) {
        try {
            log.info("updateStatus" + id + "Request" + request);

            OrderStatus status = OrderStatus.valueOf(request);
            Optional<OrderDetailsEntity> order = ordersRepository.findById(id);
            log.debug("order:{}", order);
            if (order.isPresent()) {
                order.get().setOrderStatus(status);
            }
            ordersRepository.save(order.get());
        } catch (Exception e) {
            log .error("Error Message"+e.getMessage());
            throw new RuntimeException(e);
        }

    }


    public Object getOrdersByCustomerId(Long customerId) {
        try {
            log.debug("getOrdersByCustomerId" + customerId);
            Map<Long, String> data = userClient.getIdName();
            Map<Long, recordRestaurant> restaurantsData = restaurantClient.getRestaurantsIdNameAddress();
            List<OrderDetailsEntity> orders = ordersRepository.findBycustomerId(customerId);
            List<OrderDetailsDto> orderDetailsDtos = new ArrayList<>();
            for (OrderDetailsEntity order : orders) {
                OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
                List<ItemDetailsDto> itemDetailsDtos = new ArrayList<>();
                orderDetailsDto.setId(order.getId());
                orderDetailsDto.setRestaurantName(restaurantsData.get(order.getRestaurantId()).name());
                orderDetailsDto.setRating(order.getRating());
                orderDetailsDto.setTotalPrice(order.getTotalPrice());
                orderDetailsDto.setPaymentStatus(order.getPaymentStatus());
                orderDetailsDto.setOrderStatus(order.getOrderStatus());
                orderDetailsDto.setCustomerName(data.get(order.getCustomerId()));
                orderDetailsDto.setDeliveryPersonName(data.get(order.getDeliveryPersonId()));
                for (ItemsDetailsEntity itemsDetailsEntity : order.getOrderedItems()) {
                    ItemDetailsDto itemDto = new ItemDetailsDto();
                    itemDto.setName(itemsDetailsEntity.getItemName());
                    itemDto.setPrice(itemsDetailsEntity.getItemPrice());
                    itemDto.setId(itemsDetailsEntity.getId());
                    itemDto.setQuantity(itemsDetailsEntity.getQuantity());
                    itemDetailsDtos.add(itemDto);
                }
                orderDetailsDto.setOrderedItems(itemDetailsDtos);
                orderDetailsDtos.add(orderDetailsDto);
            }
            log.debug("orderDetailsDtos:{}", orderDetailsDtos.toString());
            orderDetailsDtos.sort((a, b) -> Math.toIntExact(b.getId() - a.getId()));
            return orderDetailsDtos;
        } catch (Exception e) {
            log.error("Error accoured at getOrdersByRestaurantId"+e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public List<DeliveryDto> getOrdersforDelivery() {
        try {
            Map<Long, recordRestaurant> restaurantsData = restaurantClient.getRestaurantsIdNameAddress();
            List<OrderDetailsEntity> orders = ordersRepository.findByOrderStatus(OrderStatus.READY_FOR_DELIVERY);
            Map<Long, String> userdata = userClient.getIdName();
            List<DeliveryDto> readyforDeliveryDtoList = new ArrayList<>();
            for (OrderDetailsEntity order : orders) {
                DeliveryDto readyforDeliveryDto = new DeliveryDto();
                readyforDeliveryDto.setOrderId(order.getId());
                readyforDeliveryDto.setRestaurantName(restaurantsData.get(order.getRestaurantId()).name());
                readyforDeliveryDto.setRestaurantAddress(restaurantsData.get(order.getRestaurantId()).Address());
                readyforDeliveryDto.setCustomerAddress(order.getDeliveryAddress());
                readyforDeliveryDto.setCustomerName(userdata.get(order.getCustomerId()));
                readyforDeliveryDto.setDeliveryPersonId(order.getDeliveryPersonId());
                readyforDeliveryDtoList.add(readyforDeliveryDto);
            }
            log.debug("readyforDeliveryDtos:{}", readyforDeliveryDtoList.toString());
            readyforDeliveryDtoList.sort((a, b) -> Math.toIntExact(b.getOrderId() - a.getOrderId()));
            return readyforDeliveryDtoList;
        }
        catch (Exception e) {
            log.error("Error accoured at getOrdersforDelivery"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String placeDelivery(DeliveryDto deliveryDto) {
        try {
            DeliveryDetailsEntity deliveryDetails = new DeliveryDetailsEntity();
            deliveryDetails.setOrderId(deliveryDto.getOrderId());
            deliveryDetails.setDeliveryPersonId(deliveryDto.getDeliveryPersonId());
            deliveryDetails.setRestaurantName(deliveryDto.getRestaurantName());
            deliveryDetails.setPickupLocation(deliveryDto.getRestaurantAddress());
            deliveryDetails.setCustomerName(deliveryDto.getCustomerName());
            deliveryDetails.setDeliveryLocation(deliveryDto.getCustomerAddress());
            deliveryDetails.setStatus(DeliveryStatus.ASSIGNED);
            deliveryRepository.save(deliveryDetails);
            Optional<OrderDetailsEntity> order = ordersRepository.findById(deliveryDto.getOrderId());
            order.get().setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
            order.get().setDeliveryPersonId(deliveryDto.getDeliveryPersonId());
            log.debug("order:{}", order.get().toString());
            ordersRepository.save(order.get());
            return "Success";
        } catch (Exception e) {
            log.error("Error accoured at placeDelivery"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Object getAllActiveDeliveries(Long id) {
        try {
            List<DeliveryDetailsEntity> detailsEntityList = deliveryRepository.findByDeliveryPersonId(id);
            List<DeliveryDto> deliveryDtoList = new ArrayList<>();
            for (DeliveryDetailsEntity deliveryDetails : detailsEntityList) {
                if (!deliveryDetails.getStatus().equals(DeliveryStatus.DELIVERED)) {
                    DeliveryDto deliveryDto = new DeliveryDto();
                    deliveryDto.setId(deliveryDetails.getId());
                    deliveryDto.setOrderId(deliveryDetails.getOrderId());
                    deliveryDto.setRestaurantName(deliveryDetails.getRestaurantName());
                    deliveryDto.setRestaurantAddress(deliveryDetails.getPickupLocation());
                    deliveryDto.setCustomerAddress(deliveryDetails.getDeliveryLocation());
                    deliveryDto.setCustomerName(deliveryDetails.getCustomerName());
                    deliveryDto.setDeliveryPersonId(deliveryDetails.getDeliveryPersonId());
                    deliveryDto.setDeliveryStatus(String.valueOf(deliveryDetails.getStatus()));
                    deliveryDtoList.add(deliveryDto);
                }
            }
            log.debug("deliveryDtoList:{}", deliveryDtoList.toString());
            deliveryDtoList.sort((a, b) -> Math.toIntExact(b.getId() - a.getId()));
            return deliveryDtoList;
        } catch (Exception e) {
            log.error("Error accoured at getAllActiveDeliveries"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateDeliveryStatus(Long id, String status) {
        try {
            Optional<DeliveryDetailsEntity> deliveryDetailsEntity = deliveryRepository.findById(id);
            deliveryDetailsEntity.get().setStatus(DeliveryStatus.valueOf(status));
            if (status.equalsIgnoreCase("Delivered")) {
                updateStatus(deliveryDetailsEntity.get().getOrderId(), OrderStatus.DELIVERED.toString());
            }
            deliveryRepository.save(deliveryDetailsEntity.get());
        } catch (Exception e) {
            log.error("Error accoured at updateDeliveryStatus"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Object getAllDeliveries(Long id) {
        try {
            log.debug("getAllDeliveries:{}", id);
            List<DeliveryDetailsEntity> detailsEntityList = deliveryRepository.findByDeliveryPersonIdAndStatus(id, DeliveryStatus.DELIVERED);
            List<DeliveryDto> deliveryDtoList = new ArrayList<>();
            for (DeliveryDetailsEntity deliveryDetails : detailsEntityList) {
                DeliveryDto deliveryDto = new DeliveryDto();
                deliveryDto.setId(deliveryDetails.getId());
                deliveryDto.setOrderId(deliveryDetails.getOrderId());
                deliveryDto.setRestaurantName(deliveryDetails.getRestaurantName());
                deliveryDto.setRestaurantAddress(deliveryDetails.getPickupLocation());
                deliveryDto.setCustomerAddress(deliveryDetails.getDeliveryLocation());
                deliveryDto.setCustomerName(deliveryDetails.getCustomerName());
                deliveryDto.setDeliveryPersonId(deliveryDetails.getDeliveryPersonId());
                deliveryDto.setDeliveryStatus(String.valueOf(deliveryDetails.getStatus()));
                deliveryDtoList.add(deliveryDto);
            }
            log.debug("deliveryDtoList:{}", deliveryDtoList.toString());
            deliveryDtoList.sort((a, b) -> Math.toIntExact(b.getId() - a.getId()));
            return deliveryDtoList;
        } catch (Exception e) {
            log.error("Error accoured at getAllDeliveries"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateRating(Long id, Long rating) {
        try{
        Optional<OrderDetailsEntity> orderDetails=ordersRepository.findById(id);
        if(orderDetails.isPresent()){
            orderDetails.get().setRating(rating);
        }
        ordersRepository.save(orderDetails.get());
        }catch (Exception e){
            log.error("Error accoured at updateRating"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public ReportDto appReport() {
        try {
            List<OrderDetailsEntity> totalOrders = ordersRepository.todayOrdersByTimestamp(LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
            Map<Long, recordRestaurant> restaurantsData = restaurantClient.getRestaurantsIdNameAddress();

            log.debug("restaurantsData:{}", restaurantsData.toString());
            log.debug("totalOrders:{}", totalOrders);
            Map<Long, Long> orderCount = new HashMap<>();
            Map<Long, Double> revenue = new HashMap<>();
            double totalRevenue = 0.0;
            for (OrderDetailsEntity orderDetails : totalOrders) {
                Long restaurantId = orderDetails.getRestaurantId();
                double orderTotal = orderDetails.getTotalPrice();

                orderCount.put(restaurantId, orderCount.getOrDefault(restaurantId, 0L) + 1);
                revenue.put(restaurantId, revenue.getOrDefault(restaurantId, 0.0) + orderTotal);
                totalRevenue += orderTotal;
            }
            List<Order> topRestaurants = new ArrayList<>();
            long maxOrderCount = -1L;

            for (Map.Entry<Long, Long> entry : orderCount.entrySet()) {
                Long restaurantId = entry.getKey();
                Long count = entry.getValue();
                double restaurantRevenue = revenue.getOrDefault(restaurantId, 0.0);
                if (count > maxOrderCount) {
                    maxOrderCount = count;
                    topRestaurants.clear();
                    topRestaurants.add(new Order(
                            restaurantId,
                            restaurantsData.get(restaurantId).name(),
                            count,
                            restaurantRevenue
                    ));
                } else if (count == maxOrderCount) {
                    topRestaurants.add(new Order(
                            restaurantId,
                            restaurantsData.get(restaurantId).name(),
                            count,
                            restaurantRevenue
                    ));
                }
            }
            ReportDto reportDto = new ReportDto();
            reportDto.setTotalOrders((long) totalOrders.size());
            reportDto.setTotalRevenue(totalRevenue);
            reportDto.setTopRestaurants(topRestaurants);

            log.debug("reportDto:{}", reportDto.toString());
            return reportDto;
        }catch (Exception e){
        log.error("Error accoured at appReport"+e.getMessage());
        throw new RuntimeException(e);
        }
    }

}
