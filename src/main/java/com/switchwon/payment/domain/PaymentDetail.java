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
public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private double amount;

    private String cardNumber;
    private String expiryDate;
    private String cvv;

    private PaymentDetail(PaymentMethod paymentMethod, double amount, String cardNumber, String expiryDate, String cvv, User user) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.user = user;
    }

    public static PaymentDetail of(double amount, PaymentDetailRequest paymentDetailRequest, User user) {
        return new PaymentDetail(PaymentMethod.creditCard, amount, paymentDetailRequest.cardNumber(), paymentDetailRequest.expiryDate(), paymentDetailRequest.cvv(), user);
    }
}
