package com.gateway.controllers;

import com.gateway.models.Merchant;
import com.gateway.models.Order;
import com.gateway.repositories.MerchantRepository;
import com.gateway.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*") // Allow frontend to call this
public class OrderController {

    private final OrderService orderService;
    private final MerchantRepository merchantRepository;

    public OrderController(OrderService orderService, MerchantRepository merchantRepository) {
        this.orderService = orderService;
        this.merchantRepository = merchantRepository;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestHeader("X-Api-Key") String apiKey,
            @RequestHeader("X-Api-Secret") String apiSecret,
            @RequestBody Map<String, Object> payload) {

        // 1. Authenticate Merchant
        Optional<Merchant> merchantOpt = merchantRepository.findByApiKey(apiKey);
        if (merchantOpt.isEmpty() || !merchantOpt.get().getApiSecret().equals(apiSecret)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid API Credentials");
        }

        // 2. Extract Data
        Integer amount = (Integer) payload.get("amount");
        String currency = (String) payload.get("currency");
        String receipt = (String) payload.get("receipt");

        // 3. Create Order
        Order order = orderService.createOrder(merchantOpt.get(), amount, currency, receipt);

        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable String id) {
        Order order = orderService.getOrder(id);
        if (order == null) {
             return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }
}