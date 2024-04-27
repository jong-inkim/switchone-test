package com.switchwon.payment.domain;

import com.switchwon.user.domain.User;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String merchantId;
    private Double amount;
    private Double totalAmount;
    private Double fees;
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Instant approvedAt;

    private Payment(String merchantId, Double amount, Double totalAmount, Double fees, CurrencyCode currency, User user) {
        this.merchantId = merchantId;
        this.amount = amount;
        this.totalAmount = totalAmount;
        this.fees = fees;
        this.currency = currency;
        this.user = user;
    }

    public static Payment of(String merchantId, Double amount, CurrencyCode currency, User user) {
        double feeRate = 0.03;
        double fee = currency.floorToDecimal(amount * feeRate);
        double totalAmount = currency.floorToDecimal(amount + fee);
        return new Payment(merchantId, amount, totalAmount, fee, currency, user);
    }

    public void toReady() {
        this.status = PaymentStatus.READY;
    }

    public void toApproved() {
        this.status = PaymentStatus.APPROVED;
        this.approvedAt = Instant.now();
    }
}
