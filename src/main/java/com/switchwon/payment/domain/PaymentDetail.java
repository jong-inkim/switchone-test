package com.switchwon.payment.domain;

import com.switchwon.payment.dto.PaymentDetailRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private double amount;

    private String cardNumber;
    private String expiryDate;
    private String cvv;

    public PaymentDetail(PaymentMethod paymentMethod, double amount, String cardNumber, String expiryDate, String cvv) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public static PaymentDetail of(double amount, PaymentDetailRequest paymentDetailRequest) {
        return new PaymentDetail(PaymentMethod.creditCard, amount, paymentDetailRequest.cardNumber(), paymentDetailRequest.expiryDate(), paymentDetailRequest.cvv());
    }
}
