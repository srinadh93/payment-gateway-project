package com.gateway.services;

import com.gateway.models.Order;
import com.gateway.models.Payment;
import com.gateway.repositories.OrderRepository;
import com.gateway.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ValidationService validationService;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository, ValidationService validationService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.validationService = validationService;
    }

    public Payment processPayment(String orderId, String method, String vpa, String cardNumber) {
        // 1. Find the Order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2. Create the Payment Record
        Payment payment = new Payment();
        payment.setId("pay_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        payment.setOrder(order);
        payment.setAmount(order.getAmount());
        payment.setMethod(method);

        // 3. Validation Logic
        boolean isValid = true;
        String error = null;

        if ("upi".equalsIgnoreCase(method)) {
            payment.setVpa(vpa);
            if (!validationService.isValidVpa(vpa)) {
                isValid = false;
                error = "Invalid VPA address";
            }
        } else if ("card".equalsIgnoreCase(method)) {
            payment.setCardDetails("unknown", cardNumber.substring(cardNumber.length() - 4));
            if (!validationService.isValidCard(cardNumber)) {
                isValid = false;
                error = "Invalid Card Number (Luhn Check Failed)";
            }
        } else {
            isValid = false;
            error = "Unsupported payment method";
        }

        // 4. Simulate Bank Delay (2 seconds)
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 5. Finalize Status
        if (isValid) {
            payment.setStatus("success");
            order.setStatus("paid"); // Update Order status too
            orderRepository.save(order);
        } else {
            payment.setStatus("failed");
            payment.setErrorDescription(error);
        }

        return paymentRepository.save(payment);
    }
    
    public Payment getPayment(String id) {
        return paymentRepository.findById(id).orElse(null);
    }
}