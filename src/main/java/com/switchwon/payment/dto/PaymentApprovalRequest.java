package com.switchwon.payment.dto;

import com.switchwon.payment.domain.CurrencyCode;
import org.springframework.util.Assert;

public record PaymentApprovalRequest(String userId, Double amount, CurrencyCode currencyCode, String merchantId, String creditCard,
                                     PaymentDetailRequest paymentDetailRequest) {
    public PaymentApprovalRequest {
        Assert.hasText(userId, "userId cannot be empty");
        Assert.isTrue(amount > 0, "Amount must be greater than 0");
        Assert.notNull(currencyCode, "CurrencyCode must not be null");
        Assert.hasText(merchantId, "MerchantId must not be null");
        Assert.hasText(creditCard, "CreditCard must not be null");
    }
}
