package com.switchwon.payment.domain;


import lombok.Getter;

@Getter
public enum CurrencyCode {
    USD(2), KRW(0);

    private final int floorDigit;

    CurrencyCode(int floorDigit) {
        this.floorDigit = floorDigit;
    }

    public double floorToDecimal(double amount) {
        double pow = Math.pow(10, this.floorDigit);
        return Math.floor(amount * pow) / pow;
    }
}
