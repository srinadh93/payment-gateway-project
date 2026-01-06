package com.gateway.controllers;

import com.gateway.models.Payment;
import com.gateway.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "*") // Allow frontend to call this
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> processPayment(@RequestBody Map<String, Object> payload) {
        // 1. Extract Data from JSON
        String orderId = (String) payload.get("order_id");
        String method = (String) payload.get("method");
        String vpa = (String) payload.get("vpa");
        
        // Handle nested card object if present
        String cardNumber = null;
        if (payload.containsKey("card")) {
            Map<String, String> cardMap = (Map<String, String>) payload.get("card");
            cardNumber = cardMap.get("number");
        }

        // 2. Process via Service
        try {
            Payment payment = paymentService.processPayment(orderId, method, vpa, cardNumber);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(@PathVariable String id) {
        Payment payment = paymentService.getPayment(id);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }
}