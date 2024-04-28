package com.switchwon.payment.dto;

import com.switchwon.payment.domain.CurrencyCode;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.Assert;

@Schema(title = "PaymentEstimateRequest", description = "결졔 예상 조회 요청")
public record PaymentEstimateRequest(Double amount,
                                     CurrencyCode currency,
                                     String merchantId,
                                     String userId) {
    public PaymentEstimateRequest {
        Assert.state(amount > 0, "금액은 0보다 커야합니다.");
        Assert.notNull(currency, "통화는 필수값 입니다.");
        Assert.hasText(merchantId, "상점 아이디는 필수값 입니다..");
        Assert.hasText(userId, "유저 아이디는 필수 값입니다.");
    }
}
