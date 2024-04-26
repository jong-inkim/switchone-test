package com.switchwon.payment.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurrencyCodeTest {

    @Test
    void KRW_버림() {
        double krwAmount = 1000.1234;
        double decimal = CurrencyCode.KRW.floorToDecimal(krwAmount);
        assertThat(decimal).isEqualTo(1000);
    }

    @Test
    void USD_버림() {
        double usdAmount = 1000.1234;
        double decimal = CurrencyCode.USD.floorToDecimal(usdAmount);

        assertThat(decimal).isEqualTo(1000.12);
    }

}