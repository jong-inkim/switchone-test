package com.switchwon.payment.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String merchantId;
    private Double amount;
    private Double totalAmount;
    private Double fees;
    private CurrencyCode currency;
    private PaymentStatus status;
    private Instant approvedAt;

    public Payment(String merchantId, Double amount, Double totalAmount, Double fees, CurrencyCode currency) {
        this.merchantId = merchantId;
        this.amount = amount;
        this.totalAmount = totalAmount;
        this.fees = fees;
        this.currency = currency;
    }

    public static Payment of(String merchantId, Double amount, CurrencyCode currency) {
        double feeRate = 0.03;
        double fee = currency.floorToDecimal(amount * feeRate);
        double totalAmount = currency.floorToDecimal(amount + fee);
        return new Payment(merchantId, amount, totalAmount, fee, currency);
    }

    public void toReady() {
        this.status = PaymentStatus.READY;
    }

    public void toApproved() {
        this.status = PaymentStatus.APPROVED;
        this.approvedAt = Instant.now();
    }
}
