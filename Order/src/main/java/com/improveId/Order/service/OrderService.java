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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        orderDetails = ordersRepository.save(orderDetails);

        return orderDetails;
    }
    private List<ItemsDetailsEntity> getItemsDetailsEntities(OrderDto orderDto, OrderDetailsEntity orderDetails) {
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
        Map<Long,String> data=userClient.getIdName();
        List<OrderDetailsEntity> orders=ordersRepository.findByrestaurantId(restaurantId);
        orders.sort((a,b)-> Math.toIntExact(a.getId() - b.getId()));
        List<OrderDetailsDto> orderDetailsDtos = new ArrayList<>();
        for(OrderDetailsEntity order:orders) {
            OrderDetailsDto orderDetailsDto=new OrderDetailsDto();
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

            orderDetailsDtos.add(orderDetailsDto);
        }
        return orderDetailsDtos;
    }
    public RestaurantDto getRestaurantById(Long id) {
        RestaurantDto restaurant = restaurantClient.getRestaurantById(id);
        return restaurant;
    }
    public UserProfileDto getUser(Long id) {
        UserProfileDto user = userClient.getById(id);
        return user;
    }

    public PaymentDto Payment(String request, String method) {
        PaymentDto  result = paymentClient.pay(request,method);
        OrderDetailsEntity orderdata=null;
        if (result!=null){
          Optional<OrderDetailsEntity> entity=  ordersRepository.findById(result.getOrderID());
          entity.get().setPaymentStatus(result.getStatus());
         orderdata= ordersRepository.save(entity.get());
        }
        if(result.getStatus().equals(PaymentStatus.SUCCESS))
        {
            UserProfileDto cutomer=getUser(result.getCustomerID());
            RestaurantDto restaurant=getRestaurantById(result.getRestaurantID());
            MailDto mail=new MailDto();
            mail.setFullName(cutomer.getFullName());
            mail.setMailId(cutomer.getEmail());
            mail.setAmount(result.getAmount());
            mail.setOrderId(result.getOrderID());
            mail.setRestaurantName(restaurant.getName());
            emailClient.sendEmail(mail);
        }
        return result;
    }

    public void updateStatus(Long id,String request) {

        OrderStatus status=OrderStatus.valueOf(request);
        Optional<OrderDetailsEntity> order=ordersRepository.findById(id);
        if(order.isPresent()){
            order.get().setOrderStatus(status);
        }
       ordersRepository.save(order.get());

    }


    public Object getOrdersByCustomerId(Long customerId) {

        Map<Long,String> data=userClient.getIdName();
        List<OrderDetailsEntity> orders=ordersRepository.findBycustomerId(customerId);
        List<OrderDetailsDto> orderDetailsDtos = new ArrayList<>();
        for(OrderDetailsEntity order:orders) {
            OrderDetailsDto orderDetailsDto=new OrderDetailsDto();
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

            orderDetailsDtos.add(orderDetailsDto);
        }
        return orderDetailsDtos;

    }

    public List<DeliveryDto> getOrdersforDelivery() {

       Map<Long, recordRestaurant> restaurantsData=restaurantClient.getRestaurantsIdNameAddress();
        List<OrderDetailsEntity> orders=ordersRepository.findByOrderStatus(OrderStatus.READY_FOR_DELIVERY);
        Map<Long,String> userdata=userClient.getIdName();
        List<DeliveryDto> readyforDeliveryDtoList=new ArrayList<>();
        for(OrderDetailsEntity order:orders) {
           DeliveryDto readyforDeliveryDto=new DeliveryDto();
           readyforDeliveryDto.setOrderId(order.getId());
           readyforDeliveryDto.setRestaurantName(restaurantsData.get(order.getRestaurantId()).name());
           readyforDeliveryDto.setRestaurantAddress(restaurantsData.get(order.getRestaurantId()).Address());
           readyforDeliveryDto.setCustomerAddress(order.getDeliveryAddress());
           readyforDeliveryDto.setCustomerName(userdata.get(order.getCustomerId()));
           readyforDeliveryDto.setDeliveryPersonId(order.getDeliveryPersonId());
           readyforDeliveryDtoList.add(readyforDeliveryDto);
        }
        return readyforDeliveryDtoList;
    }

    public String placeDelivery(DeliveryDto deliveryDto) {

        DeliveryDetailsEntity deliveryDetails=new DeliveryDetailsEntity();
        deliveryDetails.setOrderId(deliveryDto.getOrderId());
        deliveryDetails.setDeliveryPersonId(deliveryDto.getDeliveryPersonId());
        deliveryDetails.setRestaurantName(deliveryDto.getRestaurantName());
        deliveryDetails.setPickupLocation(deliveryDto.getRestaurantAddress());
        deliveryDetails.setCustomerName(deliveryDto.getCustomerName());
        deliveryDetails.setDeliveryLocation(deliveryDto.getCustomerAddress());
        deliveryDetails.setStatus(DeliveryStatus.ASSIGNED);
        deliveryRepository.save(deliveryDetails);
        Optional<OrderDetailsEntity> order=ordersRepository.findById(deliveryDto.getOrderId());
        order.get().setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
        order.get().setDeliveryPersonId(deliveryDto.getDeliveryPersonId());
        ordersRepository.save(order.get());
        return "Success";
    }

    public Object getAllActiveDeliveries(Long id) {
        List<DeliveryDetailsEntity> detailsEntityList=deliveryRepository.findByDeliveryPersonId(id);
        List<DeliveryDto> deliveryDtoList=new ArrayList<>();
        for(DeliveryDetailsEntity deliveryDetails:detailsEntityList){
            DeliveryDto deliveryDto=new DeliveryDto();
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
        return deliveryDtoList;
    }

    public void updateDeliveryStatus(Long id, String status) {
        Optional<DeliveryDetailsEntity> deliveryDetailsEntity=deliveryRepository.findById(id);
        deliveryDetailsEntity.get().setStatus(DeliveryStatus.valueOf(status));
        deliveryRepository.save(deliveryDetailsEntity.get());
    }

    public Object getAllDeliveries(Long id) {
        List<DeliveryDetailsEntity> detailsEntityList=deliveryRepository.findByDeliveryPersonIdAndStatus(id,DeliveryStatus.DELIVERED);
        List<DeliveryDto> deliveryDtoList=new ArrayList<>();
        for(DeliveryDetailsEntity deliveryDetails:detailsEntityList){
            DeliveryDto deliveryDto=new DeliveryDto();
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
        return deliveryDtoList;
    }
}
