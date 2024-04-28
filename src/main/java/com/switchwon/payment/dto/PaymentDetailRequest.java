package com.switchwon.payment.dto;

import org.springframework.util.Assert;

public record PaymentDetailRequest(String cardNumber, String expiryDate, String cvv) {
    public PaymentDetailRequest {
        Assert.hasText(cardNumber, "카드번호는 필수 값입니다.");
        Assert.hasText(expiryDate, "카드 만료일자는 필수 값입니다.");
        Assert.hasText(cvv, "카드 cvv는 필수 입니다.");
    }
}
