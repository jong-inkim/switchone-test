package com.switchwon.payment.dto;

import com.switchwon.payment.domain.Payment;
import com.switchwon.payment.domain.PaymentStatus;
import com.switchwon.payment.domain.CurrencyCode;

import java.time.Instant;

public record PaymentApprovalResponse(Long paymentId, PaymentStatus status, Double amountTotal, CurrencyCode currency,
                                      Instant timestamp) {

    public static PaymentApprovalResponse from(Payment payment) {
        return new PaymentApprovalResponse(payment.getId(), payment.getStatus(), payment.getTotalAmount(), payment.getCurrency(), payment.getApprovedAt());
    }
}
