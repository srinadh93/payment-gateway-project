package com.gateway.services;

import com.gateway.models.Merchant;
import com.gateway.models.Order;
import com.gateway.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Merchant merchant, Integer amount, String currency, String receipt) {
        Order order = new Order();
        // Generate a readable ID like "order_abc123"
        order.setId("order_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        order.setMerchant(merchant);
        order.setAmount(amount);
        order.setCurrency(currency);
        order.setReceipt(receipt);
        order.setStatus("created");
        
        return orderRepository.save(order);
    }

    public Order getOrder(String id) {
        return orderRepository.findById(id).orElse(null);
    }
}