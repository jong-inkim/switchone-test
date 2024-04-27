package com.switchwon.payment.domain;

import com.switchwon.payment.exception.NotEnoughBalanceException;
import com.switchwon.user.domain.User;
import jakarta.persistence.*;
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
    private Double balance;
    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserBalance(User user, double balance, CurrencyCode currency) {
        this.user = user;
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
