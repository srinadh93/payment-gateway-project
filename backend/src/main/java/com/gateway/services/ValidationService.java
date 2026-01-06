package com.gateway.services;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public boolean isValidVpa(String vpa) {
        return vpa != null && vpa.matches("^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$");
    }

    public boolean isValidCard(String number) {
        if (number == null || !number.matches("\\d{13,19}")) {
            return false;
        }
        return luhnCheck(number);
    }

    // Luhn Algorithm for Credit Card Validation
    private boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}