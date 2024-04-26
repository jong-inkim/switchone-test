package com.switchwon.payment.dto;

import org.springframework.util.Assert;

public record PaymentDetailRequest(String cardNumber, String expiryDate, String cvv) {
    public PaymentDetailRequest {
        Assert.hasText(cardNumber, "CardNumber must not be null");
        Assert.hasText(expiryDate, "ExpiryDate must not be null");
        Assert.hasText(cvv, "Cvv must not be null");
    }
}
