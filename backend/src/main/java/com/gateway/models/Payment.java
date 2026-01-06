package com.gateway.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    private String id; // Generated like "pay_xyz789"

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String status; // processing, success, failed

    @Column(nullable = false)
    private String method; // upi, card

    // Optional Details based on method
    private String vpa; // for UPI
    private String cardNetwork; // visa, mastercard
    private String cardLast4;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // New fields for error tracking
    @Column(name = "error_description")
    private String errorDescription;

    @Column(name = "error_code")
    private String errorCode;

    // Relationship: Payment belongs to an Order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Payment() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getVpa() { return vpa; }
    public void setVpa(String vpa) { this.vpa = vpa; }

    public String getCardNetwork() { return cardNetwork; }
    public String getCardLast4() { return cardLast4; }
    
    public void setCardDetails(String network, String last4) {
        this.cardNetwork = network;
        this.cardLast4 = last4;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
    
    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}