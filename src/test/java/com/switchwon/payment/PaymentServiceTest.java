package com.switchwon.payment;

import com.switchwon.payment.domain.*;
import com.switchwon.payment.exception.DoNotMatchedAmountException;
import com.switchwon.payment.repository.ChargePaymentRepository;
import com.switchwon.payment.repository.PaymentRepository;
import com.switchwon.payment.dto.*;
import com.switchwon.payment.exception.DupliatedMerchantIdException;
import com.switchwon.payment.repository.BalanceRepository;
import com.switchwon.payment.service.PaymentService;
import com.switchwon.user.domain.User;
import com.switchwon.user.repository.UserRepository;
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
    BalanceRepository balanceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ChargePaymentRepository chargePaymentRepository;

    @BeforeEach
    void setUp() {
        User testUser1 = userRepository.save(new User("test1"));
        User testUser2 = userRepository.save(new User("test2"));
        User testUser3 = userRepository.save(new User("test3"));
        balanceRepository.save(new Balance(testUser1, 150, CurrencyCode.USD));
        balanceRepository.save(new Balance(testUser2, 100, CurrencyCode.USD));
        balanceRepository.save(new Balance(testUser3, 170, CurrencyCode.USD));
    }

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
        chargePaymentRepository.deleteAll();
        balanceRepository.deleteAll();
        userRepository.deleteAll();
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
        paymentService.estimate(new PaymentEstimateRequest(150.00, CurrencyCode.USD, "merchantId2", "test2"));

        assertThatThrownBy(() -> paymentService.estimate(new PaymentEstimateRequest(150.00, CurrencyCode.USD, "merchantId2", "test2")))
                .isInstanceOf(DupliatedMerchantIdException.class);
    }

    @Test
    void 결제_승인_요청_balance가_남는경우() {
        String testId = "test1";
        String merchantId = "merchantId22";
        double amount = 150.00;
        CurrencyCode currency = CurrencyCode.USD;
        PaymentMethod paymentMethod = PaymentMethod.creditCard;

        User testUser = getUser(testId);
        paymentRepository.save(Payment.of("merchantId22", 150.00, CurrencyCode.USD, testUser));

        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest("1234-5678-1234-1234", "12/24", "123");


        PaymentApprovalRequest request = new PaymentApprovalRequest(testId, amount, currency, merchantId, paymentMethod, paymentDetailRequest);

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

    private User getUser(String userId) {
        return userRepository.findByUserId(userId).get();
    }

    @Test
    void 결제_승인_요청_balance가_적은경우() {
        String testId = "test2";
        String merchantId = "merchantId11";
        double amount = 150.00;
        CurrencyCode currency = CurrencyCode.USD;
        PaymentMethod paymentMethod = PaymentMethod.creditCard;

        User testUser = getUser(testId);
        paymentRepository.save(Payment.of("merchantId11", 150.00, CurrencyCode.USD, testUser));

        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest("1234-5678-1234-1234", "12/24", "123");

        PaymentApprovalRequest request = new PaymentApprovalRequest(testId, amount, currency, merchantId, paymentMethod, paymentDetailRequest);

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

        User user = userRepository.findByUserId(testId).get();
        assertThat(user.getBalance().getBalance()).isEqualTo(0.00);
    }

    @Test
    void 결제_승인_금액검증실패() {
        String testId = "test1";
        String merchantId = "merchantId22";
        double amount = 100.00;
        CurrencyCode currency = CurrencyCode.USD;
        PaymentMethod paymentMethod = PaymentMethod.creditCard;

        User testUser = getUser(testId);
        paymentRepository.save(Payment.of("merchantId22", 150.00, CurrencyCode.USD, testUser));

        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest("1234-5678-1234-1234", "12/24", "123");

        PaymentApprovalRequest request = new PaymentApprovalRequest(testId, amount, currency, merchantId, paymentMethod, paymentDetailRequest);

        assertThatThrownBy(() -> paymentService.approval(request))
                .isInstanceOf(DoNotMatchedAmountException.class);
    }
}
