package com.switchwon.payment.controller;

import com.switchwon.payment.dto.*;
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
    public ResponseEntity<PaymentEstimateResponse> estimateOrder(@RequestBody PaymentEstimateRequest request) {
        PaymentEstimateResponse response = paymentService.estimate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/payment/approval")
    public ResponseEntity<PaymentApprovalResponse> approveOrder(@RequestBody PaymentApprovalRequest request) {
        PaymentApprovalResponse response = paymentService.approval(request);
        return ResponseEntity.ok(response);
    }
}
