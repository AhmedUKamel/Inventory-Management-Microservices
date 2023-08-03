package com.ahmedukamel.orderservice.service;

import com.ahmedukamel.orderservice.dto.InventoryResponse;
import com.ahmedukamel.orderservice.dto.OrderLineItemsDto;
import com.ahmedukamel.orderservice.dto.OrderRequest;
import com.ahmedukamel.orderservice.model.Order;
import com.ahmedukamel.orderservice.model.OrderLineItems;
import com.ahmedukamel.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
                .stream().map(this::mapToOrderLineItems).toList();

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderLineItemsList)
                .build();

        // TODO: Call Inventory Service and place Order if Product in Stock
        List<String> skuCodes = orderRequest.getOrderLineItemsDtoList()
                .stream().map(OrderLineItemsDto::getSkuCode).toList();

        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/v1/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean isAllProductInStock = Arrays.stream(inventoryResponseArray != null ? inventoryResponseArray : new InventoryResponse[0])
                .allMatch(InventoryResponse::isInStock);

        if (isAllProductInStock) {
            orderRepository.save(order);
            log.info("Order Number {} Placed Successfully", order.getOrderNumber());
        } else {
            throw new IllegalArgumentException("Product Is Not in Stock");
        }
    }

    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .id(orderLineItemsDto.getId())
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
}
