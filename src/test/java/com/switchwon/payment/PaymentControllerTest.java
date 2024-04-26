package com.switchwon.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.switchwon.order.dto.OrderEstimateRequest;
import com.switchwon.payment.domain.CurrencyCode;
import com.switchwon.payment.domain.UserBalance;
import com.switchwon.payment.repository.UserBalanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserBalanceRepository userBalanceRepository;

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
        OrderEstimateRequest request = new OrderEstimateRequest(150, CurrencyCode.USD, "merchantId12", "test2");
        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/payment/estimate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.estimatedTotal").value(154.50))
                .andExpect(jsonPath("$.fees").value(4.50));

    }
}
