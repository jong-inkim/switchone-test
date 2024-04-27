package com.switchwon.payment;

import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.domain.UserBalance;
import com.switchwon.payment.dto.BalanceResponse;
import com.switchwon.payment.dto.PaymentDetailRequest;
import com.switchwon.payment.repository.PaymentDetailRepository;
import com.switchwon.payment.repository.UserBalanceRepository;
import com.switchwon.payment.service.UserBalanceService;
import com.switchwon.user.domain.User;
import com.switchwon.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    PaymentDetailRepository paymentDetailRepository;

    @Autowired
    UserRepository userRepository;

    User testUser;

    @BeforeEach
    void setUp() {
        testUser = userRepository.save(new User("test1"));
    }

    @AfterEach
    void tearDown() {
        userBalanceRepository.deleteAll();
        paymentDetailRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void 잔액_조회() {
        userBalanceRepository.save(new UserBalance(testUser, 1000.00, CurrencyCode.USD));

        String userId = "test1";

        BalanceResponse balanceResponse = userBalanceService.getBalanceByUserId(userId);

        assertThat(balanceResponse.balance()).isEqualTo(1000.00);
        assertThat(balanceResponse.currency()).isEqualTo(CurrencyCode.USD);
    }

    @Test
    void 잔액_조회_유저아이디없을때() {
        userBalanceRepository.save(new UserBalance(testUser, 1000.00, CurrencyCode.USD));

        String userId = "test3";

        assertThatThrownBy(() -> userBalanceService.getBalanceByUserId(userId)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void 잔액_충전() {
        String userId = "test1";
        userBalanceRepository.save(new UserBalance(testUser, 1000.00, CurrencyCode.USD));

        double chargeAmount = 500.00;
        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest("1234-1234-1234-1234", "12/24", "123");
        userBalanceService.charge(chargeAmount, userId, paymentDetailRequest);

        UserBalance userBalance = userBalanceRepository.findByUserId(testUser.getId()).get();
        assertThat(userBalance.getBalance()).isEqualTo(1500.00);
    }

}
