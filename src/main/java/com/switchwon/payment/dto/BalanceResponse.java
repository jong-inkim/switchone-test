package com.switchwon.payment.dto;

import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.domain.Balance;

public record BalanceResponse(String userId, double balance, CurrencyCode currency) {

    public static BalanceResponse from(Balance balance) {
        return new BalanceResponse(balance.getUser().getUserId(), balance.getBalance(), balance.getCurrency());
    }
}
