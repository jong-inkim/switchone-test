package com.switchwon.payment.controller;

import com.switchwon.order.dto.OrderEstimateRequest;
import com.switchwon.order.dto.OrderEstimateResponse;
import com.switchwon.payment.dto.BalanceResponse;
import com.switchwon.payment.service.PaymentService;
import com.switchwon.payment.service.UserBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    private final UserBalanceService userBalanceService;
    private final PaymentService paymentService;

    public PaymentController(UserBalanceService userBalanceService, PaymentService paymentService) {
        this.userBalanceService = userBalanceService;
        this.paymentService = paymentService;
    }

    @GetMapping("/api/payment/balance/{userId}")
    public ResponseEntity<BalanceResponse> getBalanceByUserId(@PathVariable("userId") String userId) {
        BalanceResponse response = userBalanceService.getBalanceByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/payment/estimate")
    public ResponseEntity<OrderEstimateResponse> estimateOrder(@RequestBody OrderEstimateRequest request) {
        OrderEstimateResponse response = paymentService.estimate(request);
        return ResponseEntity.ok(response);
    }
}
