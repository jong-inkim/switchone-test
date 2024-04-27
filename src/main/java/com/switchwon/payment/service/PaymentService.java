package com.switchwon.payment.service;

import com.switchwon.payment.domain.Payment;
import com.switchwon.payment.domain.UserBalance;
import com.switchwon.payment.dto.PaymentApprovalRequest;
import com.switchwon.payment.dto.PaymentApprovalResponse;
import com.switchwon.payment.dto.PaymentEstimateRequest;
import com.switchwon.payment.dto.PaymentEstimateResponse;
import com.switchwon.payment.exception.DoNotMatchedAmountException;
import com.switchwon.payment.repository.PaymentRepository;
import com.switchwon.payment.exception.DupliatedMerchantIdException;
import com.switchwon.payment.repository.UserBalanceRepository;
import com.switchwon.user.domain.User;
import com.switchwon.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserBalanceRepository userBalanceRepository;
    private final UserBalanceService userBalanceService;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, UserBalanceRepository userBalanceRepository, UserBalanceService userBalanceService, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userBalanceRepository = userBalanceRepository;
        this.userBalanceService = userBalanceService;
        this.userRepository = userRepository;
    }

    @Transactional
    public PaymentEstimateResponse estimate(PaymentEstimateRequest request) {
        if (paymentRepository.findByMerchantId(request.merchantId()).isPresent()) {
            throw new DupliatedMerchantIdException(request.merchantId());
        }

        User findUser = userRepository.findByUserId(request.userId())
                .orElseThrow(() -> new EntityNotFoundException(request.userId() + " : 찾을 수 없는 USER ID 입니다."));

        Payment payment = Payment.of(request.merchantId(), request.amount(), request.currency(), findUser);

        payment.toReady();
        Payment savedPayment = paymentRepository.save(payment);
        return PaymentEstimateResponse.from(savedPayment);
    }

    @Transactional
    public PaymentApprovalResponse approval(PaymentApprovalRequest request) {
        Optional<Payment> optPayment = paymentRepository.findByMerchantId(request.merchantId());

        User findUser = userRepository.findByUserId(request.userId())
                .orElseThrow(() -> new EntityNotFoundException(request.userId() + " : 찾을 수 없는 USER ID 입니다."));

        this.validate(request, optPayment, findUser);

        Payment payment = optPayment.get();

        UserBalance userBalance = userBalanceRepository.findByUserId(findUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(findUser.getId() + " : 해당유저의 userBalance 가 존재하지 않습니다."));

        if (userBalance.getBalance() >= payment.getTotalAmount()) {
            userBalance.useBalance(payment.getTotalAmount());
        } else {
            assert request.paymentDetailRequest() != null;
            double needAmount = payment.getTotalAmount() - userBalance.getBalance();
            userBalanceService.charge(needAmount, request.userId(), request.paymentDetailRequest());
        }

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
            throw new RuntimeException("해당 유저의 merchantId가 아닙니다.");
        }
    }
}
