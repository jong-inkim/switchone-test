package com.switchwon.payment.service;

import com.switchwon.payment.domain.Payment;
import com.switchwon.payment.domain.Balance;
import com.switchwon.payment.dto.PaymentApprovalRequest;
import com.switchwon.payment.dto.PaymentApprovalResponse;
import com.switchwon.payment.dto.PaymentEstimateRequest;
import com.switchwon.payment.dto.PaymentEstimateResponse;
import com.switchwon.payment.exception.DoNotMachedMerchantIdException;
import com.switchwon.payment.exception.DoNotMatchedAmountException;
import com.switchwon.payment.exception.DupliatedMerchantIdException;
import com.switchwon.payment.repository.PaymentRepository;
import com.switchwon.user.domain.User;
import com.switchwon.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BalanceService balanceService;

    private final UserService userService;

    public PaymentService(PaymentRepository paymentRepository, BalanceService balanceService, UserService userService) {
        this.paymentRepository = paymentRepository;
        this.balanceService = balanceService;
        this.userService = userService;
    }

    @Transactional
    public PaymentEstimateResponse estimate(PaymentEstimateRequest request) {
        if (paymentRepository.findByMerchantId(request.merchantId()).isPresent()) {
            throw new DupliatedMerchantIdException(request.merchantId());
        }

        User findUser = userService.findByUserId(request.userId());

        Payment payment = Payment.of(request.merchantId(), request.amount(), request.currency(), findUser);

        payment.toReady();
        Payment savedPayment = paymentRepository.save(payment);
        return PaymentEstimateResponse.from(savedPayment);
    }

    @Transactional
    public PaymentApprovalResponse approval(PaymentApprovalRequest request) {
        Optional<Payment> optPayment = paymentRepository.findByMerchantId(request.merchantId());

        User findUser = userService.findByUserId(request.userId());

        this.validate(request, optPayment, findUser);

        Payment payment = optPayment.get();

        Balance balance = findUser.getBalance();

        if (balance.getBalance() < payment.getTotalAmount()) {
            assert request.paymentDetailRequest() != null;
            double needAmount = payment.getTotalAmount() - balance.getBalance();
            balanceService.charge(needAmount, request.userId(), request.paymentDetailRequest(), request.currencyCode());
        }

        balanceService.use(payment.getTotalAmount(), request.userId());
        payment.toApproved();
        return PaymentApprovalResponse.from(payment);
    }

    private void validate(PaymentApprovalRequest request, Optional<Payment> optPayment, User user) {
        if (optPayment.isEmpty()) {
            throw new EntityNotFoundException("결제 요청 내역이 존재하지 않습니다.");
        }

        if (!optPayment.get().getAmount().equals(request.amount())) {
            throw new DoNotMatchedAmountException();
        }

        if (!optPayment.get().getUser().getId().equals(user.getId())) {
            throw new DoNotMachedMerchantIdException();
        }
    }
}
