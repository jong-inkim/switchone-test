package com.switchwon.payment.dto;

import com.switchwon.payment.domain.Payment;
import com.switchwon.payment.domain.CurrencyCode;

public record PaymentEstimateResponse(double estimatedTotal, double fees, CurrencyCode currency) {

    public static PaymentEstimateResponse from(Payment payment) {
        return new PaymentEstimateResponse(payment.getTotalAmount(), payment.getFees(), payment.getCurrency());
    }
}
