package com.switchwon.payment.domain;

import com.switchwon.payment.dto.PaymentDetailRequest;
import com.switchwon.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChargePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    private double amount;

    private String cardNumber;
    private String expiryDate;
    private String cvv;

    private ChargePayment(PaymentMethod paymentMethod, double amount, String cardNumber, String expiryDate, String cvv, User user, CurrencyCode currency) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.user = user;
        this.currency = currency;
    }

    public static ChargePayment of(double amount, PaymentDetailRequest paymentDetailRequest, User user, CurrencyCode currency) {
        return new ChargePayment(PaymentMethod.creditCard, currency.floorToDecimal(amount), paymentDetailRequest.cardNumber(), paymentDetailRequest.expiryDate(), paymentDetailRequest.cvv(), user, currency);
    }
}
