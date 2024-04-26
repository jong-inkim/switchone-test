package com.switchwon.order.dto;

import com.switchwon.payment.domain.CurrencyCode;
import org.springframework.util.Assert;

public record OrderEstimateRequest(double amount, CurrencyCode currency, String merchantId, String userId) {
    public OrderEstimateRequest {
        Assert.state(amount > 0, "금액은 0보다 커야합니다.");
        Assert.notNull(currency, "통화는 필수값 입니다.");
        Assert.hasText(merchantId, "상점 아이디는 필수값 입니다..");
        Assert.hasText(userId, "유저 아이디는 필수 값입니다.");
    }
}
