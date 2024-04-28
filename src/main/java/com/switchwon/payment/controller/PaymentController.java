package com.switchwon.payment.controller;

import com.switchwon.payment.dto.*;
import com.switchwon.payment.service.PaymentService;
import com.switchwon.payment.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {

    private final WalletService walletService;
    private final PaymentService paymentService;

    public PaymentController(WalletService walletService, PaymentService paymentService) {
        this.walletService = walletService;
        this.paymentService = paymentService;
    }

    @Operation(summary = "잔액 조회 API", description = "user의 잔액을 조회합니다.")
    @GetMapping("/api/payment/balance/{userId}")
    public ResponseEntity<BalanceResponse> getBalanceByUserId(@PathVariable("userId") String userId) {
        BalanceResponse response = walletService.getBalanceByUserId(userId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "결제 예상 결과 조회 API", description = "결제 예상 결과 조회.")
    @PostMapping("/api/payment/estimate")
    public ResponseEntity<PaymentEstimateResponse> estimateOrder(@RequestBody PaymentEstimateRequest request) {
        PaymentEstimateResponse response = paymentService.estimate(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "결제 요청 승인 API", description = "결제 요청 승인.")
    @PostMapping("/api/payment/approval")
    public ResponseEntity<PaymentApprovalResponse> approveOrder(@RequestBody PaymentApprovalRequest request) {
        PaymentApprovalResponse response = paymentService.approval(request);
        return ResponseEntity.ok(response);
    }
}
