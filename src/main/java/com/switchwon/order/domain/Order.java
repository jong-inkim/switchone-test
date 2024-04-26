package com.switchwon.order.domain;

import com.switchwon.payment.domain.CurrencyCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String merchantId;
    private Double amount;
    private Double totalAmount;
    private Double fees;
    private CurrencyCode currency;
    private OrderStatus status;

    public Order(String merchantId, Double amount, Double totalAmount, Double fees, CurrencyCode currency) {
        this.merchantId = merchantId;
        this.amount = amount;
        this.totalAmount = totalAmount;
        this.fees = fees;
        this.currency = currency;
    }

    public static Order of(String merchantId, Double amount, CurrencyCode currency) {
        double feeRate = 0.03;
        double fee = currency.floorToDecimal(amount * feeRate);
        double totalAmount = currency.floorToDecimal(amount + fee);
        return new Order(merchantId, amount, totalAmount, fee, currency);
    }

    public void toReady() {
        this.status = OrderStatus.READY;
    }
}
