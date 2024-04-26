package com.switchwon.payment;

import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.domain.UserBalance;
import com.switchwon.payment.dto.BalanceResponse;
import com.switchwon.payment.repository.UserBalanceRepository;
import com.switchwon.payment.service.UserBalanceService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class UserBalanceServiceTest {

    @Autowired
    UserBalanceService userBalanceService;

    @Autowired
    UserBalanceRepository userBalanceRepository;

    @Test
    void 잔액_조회() {
        userBalanceRepository.save(new UserBalance("test3", 1000.00, CurrencyCode.USD));

        String userId = "test3";

        BalanceResponse balanceResponse = userBalanceService.getBalanceByUserId(userId);

        assertThat(balanceResponse.balance()).isEqualTo(1000.00);
        assertThat(balanceResponse.currency()).isEqualTo(CurrencyCode.USD);
    }

    @Test
    void 잔액_조회_유저아이디없을때() {
        userBalanceRepository.save(new UserBalance("test2", 1000.00, CurrencyCode.USD));

        String userId = "test3";

        assertThatThrownBy(() -> userBalanceService.getBalanceByUserId(userId)).isInstanceOf(EntityNotFoundException.class);
    }

}
