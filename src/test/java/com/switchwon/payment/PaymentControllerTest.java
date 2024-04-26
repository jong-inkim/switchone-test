package com.switchwon.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.domain.UserBalance;
import com.switchwon.payment.dto.PaymentApprovalRequest;
import com.switchwon.payment.dto.PaymentDetailRequest;
import com.switchwon.payment.dto.PaymentEstimateRequest;
import com.switchwon.payment.repository.PaymentDetailRepository;
import com.switchwon.payment.repository.PaymentRepository;
import com.switchwon.payment.repository.UserBalanceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserBalanceRepository userBalanceRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentDetailRepository paymentDetailRepository;

    @AfterEach
    void tearDown() {
        userBalanceRepository.deleteAll();
        paymentRepository.deleteAll();
        paymentDetailRepository.deleteAll();
    }

    @Test
    void 잔액_조회_API() throws Exception {
        userBalanceRepository.save(new UserBalance("test1", 1000.00, CurrencyCode.USD));

        String userId = "test1";

        mockMvc.perform(get("/api/payment/balance/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").exists())
                .andExpect(jsonPath("$.currency").exists());
    }

    @Test
    void 결제_예상_조회() throws Exception {
        PaymentEstimateRequest request = new PaymentEstimateRequest(150, CurrencyCode.USD, "merchantId12", "test2");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/payment/estimate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.estimatedTotal").value(154.50))
                .andExpect(jsonPath("$.fees").value(4.50));

    }

    @Test
    void 결제_승인_요청() throws Exception {
        userBalanceRepository.save(new UserBalance("test1", 1000.00, CurrencyCode.USD));
        결제_예상_조회();
        PaymentDetailRequest paymentDetails = new PaymentDetailRequest("1234-1234-1234-1234", "11/24", "123");
        PaymentApprovalRequest request = new PaymentApprovalRequest("test1", 150.00, CurrencyCode.USD, "merchantId12", "creditCard", paymentDetails);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/payment/approval")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.amountTotal").value(154.50));

    }
}
