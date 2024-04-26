package com.switchwon.payment;

import com.switchwon.payment.domain.Payment;
import com.switchwon.payment.domain.PaymentStatus;
import com.switchwon.payment.domain.UserBalance;
import com.switchwon.payment.exception.DoNotMatchedAmountException;
import com.switchwon.payment.repository.PaymentRepository;
import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.dto.*;
import com.switchwon.payment.exception.DupliatedMerchantIdException;
import com.switchwon.payment.repository.UserBalanceRepository;
import com.switchwon.payment.service.PaymentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    UserBalanceRepository userBalanceRepository;

    @BeforeEach
    void setUp() {
        userBalanceRepository.save(new UserBalance("test11", 150, CurrencyCode.USD));
        userBalanceRepository.save(new UserBalance("test22", 100, CurrencyCode.USD));
        userBalanceRepository.save(new UserBalance("test33", 170, CurrencyCode.USD));
    }

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
        userBalanceRepository.deleteAll();
    }

    @Test
    void 결제_예상_결과_조회() {
        double amount = 150.00;
        CurrencyCode currency = CurrencyCode.USD;
        String merchantId = "merchantId1";
        String userId = "test1";
        PaymentEstimateRequest request = new PaymentEstimateRequest(amount, currency, merchantId, userId);

        PaymentEstimateResponse response = paymentService.estimate(request);
        assertThat(response.fees()).isEqualTo(4.50);
        assertThat(response.estimatedTotal()).isEqualTo(154.50);
        assertThat(response.currency()).isEqualTo(CurrencyCode.USD);

        Payment findPayment = paymentRepository.findByMerchantId(merchantId).get();

        assertThat(findPayment.getAmount()).isEqualTo(amount);
        assertThat(findPayment.getCurrency()).isEqualTo(CurrencyCode.USD);
        assertThat(findPayment.getMerchantId()).isEqualTo(merchantId);
        assertThat(findPayment.getStatus()).isEqualTo(PaymentStatus.READY);
    }

    @Test
    void 이미_존재하는_상점아이디가_있을경우() {
        paymentService.estimate(new PaymentEstimateRequest(150, CurrencyCode.USD, "merchantId2", "test2"));

        assertThatThrownBy(() -> paymentService.estimate(new PaymentEstimateRequest(150, CurrencyCode.USD, "merchantId2", "test2")))
                .isInstanceOf(DupliatedMerchantIdException.class);
    }

    @Test
    void 결제_승인_요청_balance가_남는경우() {
        paymentRepository.save(Payment.of("merchantId22", 150.00, CurrencyCode.USD));

        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest("1234-5678-1234-1234", "12/24", "123");
        String testId = "test11";
        String merchantId = "merchantId22";
        double amount = 150.00;
        CurrencyCode currency = CurrencyCode.USD;
        String creditCard = "creditCard";
        PaymentApprovalRequest request = new PaymentApprovalRequest(testId, amount, currency, merchantId, creditCard, paymentDetailRequest);

        PaymentApprovalResponse response = paymentService.approval(request);

        assertThat(response.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(response.amountTotal()).isEqualTo(154.50);
        assertThat(response.currency()).isEqualTo(CurrencyCode.USD);

        Payment payment = paymentRepository.findByMerchantId(merchantId).get();

        assertThat(payment.getAmount()).isEqualTo(150.00);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(payment.getTotalAmount()).isEqualTo(154.50);
        assertThat(payment.getCurrency()).isEqualTo(CurrencyCode.USD);
        assertThat(payment.getFees()).isEqualTo(4.50);
    }

    @Test
    void 결제_승인_요청_balance가_적은경우() {
        paymentRepository.save(Payment.of("merchantId11", 150.00, CurrencyCode.USD));

        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest("1234-5678-1234-1234", "12/24", "123");
        String testId = "test11";
        String merchantId = "merchantId11";
        double amount = 150.00;
        CurrencyCode currency = CurrencyCode.USD;
        String creditCard = "creditCard";
        PaymentApprovalRequest request = new PaymentApprovalRequest(testId, amount, currency, merchantId, creditCard, paymentDetailRequest);

        PaymentApprovalResponse response = paymentService.approval(request);

        assertThat(response.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(response.amountTotal()).isEqualTo(154.50);
        assertThat(response.currency()).isEqualTo(CurrencyCode.USD);

        Payment payment = paymentRepository.findByMerchantId(merchantId).get();

        assertThat(payment.getAmount()).isEqualTo(150.00);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(payment.getTotalAmount()).isEqualTo(154.50);
        assertThat(payment.getCurrency()).isEqualTo(CurrencyCode.USD);
        assertThat(payment.getFees()).isEqualTo(4.50);
    }

    @Test
    void 결제_승인_금액검증실패() {
        paymentRepository.save(Payment.of("merchantId22", 150.00, CurrencyCode.USD));

        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest("1234-5678-1234-1234", "12/24", "123");
        String testId = "test11";
        String merchantId = "merchantId22";
        double amount = 100.00;
        CurrencyCode currency = CurrencyCode.USD;
        String creditCard = "creditCard";
        PaymentApprovalRequest request = new PaymentApprovalRequest(testId, amount, currency, merchantId, creditCard, paymentDetailRequest);

        assertThatThrownBy(() -> paymentService.approval(request))
                .isInstanceOf(DoNotMatchedAmountException.class);
    }
}
