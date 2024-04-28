package com.switchwon.payment.dto;

import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.domain.PaymentMethod;
import org.springframework.util.Assert;

public record PaymentApprovalRequest(String userId, Double amount, CurrencyCode currencyCode, String merchantId, PaymentMethod paymentMethod,
                                     PaymentDetailRequest paymentDetailRequest) {
    public PaymentApprovalRequest {
        Assert.hasText(userId, "userId는 필수 값 입니다.");
        Assert.isTrue(amount > 0, "금액은 0 이상이여야합니다.");
        Assert.notNull(currencyCode, "통화코드는 필수 값 입니다.");
        Assert.hasText(merchantId, "상점아이디는 필수 값 입니다.");
        Assert.notNull(paymentMethod, "결제 방법은 필수 값입니다.");
    }
}
