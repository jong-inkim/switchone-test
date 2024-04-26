package com.switchwon.payment;

import com.switchwon.order.domain.Order;
import com.switchwon.order.domain.OrderStatus;
import com.switchwon.order.dto.OrderEstimateRequest;
import com.switchwon.order.dto.OrderEstimateResponse;
import com.switchwon.order.repository.OrderRepository;
import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.exception.DupliatedMerchantIdException;
import com.switchwon.payment.service.PaymentService;
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
    OrderRepository orderRepository;

    @Test
    void 결제_예상_결과_조회() {
        double amount = 150.00;
        CurrencyCode currency = CurrencyCode.USD;
        String merchantId = "merchantId1";
        String userId = "test1";
        OrderEstimateRequest request = new OrderEstimateRequest(amount, currency, merchantId, userId);

        OrderEstimateResponse response = paymentService.estimate(request);
        assertThat(response.fees()).isEqualTo(4.50);
        assertThat(response.estimatedTotal()).isEqualTo(154.50);
        assertThat(response.currency()).isEqualTo(CurrencyCode.USD);

        Order findOrder = orderRepository.findByMerchantId(merchantId).get();

        assertThat(findOrder.getAmount()).isEqualTo(amount);
        assertThat(findOrder.getCurrency()).isEqualTo(CurrencyCode.USD);
        assertThat(findOrder.getMerchantId()).isEqualTo(merchantId);
        assertThat(findOrder.getStatus()).isEqualTo(OrderStatus.READY);
    }

    @Test
    void 이미_존재하는_상점아이디가_있을경우() {
        paymentService.estimate(new OrderEstimateRequest(150, CurrencyCode.USD, "merchantId2", "test2"));

        assertThatThrownBy(() -> paymentService.estimate(new OrderEstimateRequest(150, CurrencyCode.USD, "merchantId2", "test2")))
                .isInstanceOf(DupliatedMerchantIdException.class);
    }

}
