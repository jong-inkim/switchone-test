package com.switchwon.payment.dto;

import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.domain.UserBalance;

public record BalanceResponse(String userId, double balance, CurrencyCode currency) {

    public static BalanceResponse from(UserBalance member) {
        return new BalanceResponse(member.getUserId(), member.getBalance(), member.getCurrency());
    }
}
