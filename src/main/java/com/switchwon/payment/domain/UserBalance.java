package com.switchwon.payment.domain;

import com.switchwon.payment.exception.NotEnoughBalanceException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private Double balance;
    private CurrencyCode currency;

    public UserBalance(String userId, double balance, CurrencyCode currency) {
        this.userId = userId;
        this.balance = balance;
        this.currency = currency;
    }

    public void useBalance(double usedAmount) {
        if (this.balance < usedAmount) {
            throw new NotEnoughBalanceException();
        }
        this.balance -= usedAmount;
    }

    public void charge(double chargeAmount) {
        this.balance += chargeAmount;
    }
}
