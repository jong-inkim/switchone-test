package com.switchwon.payment.service;

import com.switchwon.payment.domain.PaymentDetail;
import com.switchwon.payment.domain.UserBalance;
import com.switchwon.payment.dto.BalanceResponse;
import com.switchwon.payment.dto.PaymentDetailRequest;
import com.switchwon.payment.repository.PaymentDetailRepository;
import com.switchwon.payment.repository.UserBalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;
    private final PaymentDetailRepository paymentDetailRepository;


    public UserBalanceService(UserBalanceRepository paymentRepository, PaymentDetailRepository paymentDetailRepository) {
        this.userBalanceRepository = paymentRepository;
        this.paymentDetailRepository = paymentDetailRepository;
    }

    public BalanceResponse getBalanceByUserId(String userId) {
        UserBalance userBalance = userBalanceRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId + " : 찾을 수 없는 USER ID 입니다."));

        return BalanceResponse.from(userBalance);
    }

    @Transactional
    public void charge(double chargeAmount, String userId, PaymentDetailRequest paymentDetailRequest) {
        UserBalance userBalance = userBalanceRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId + " : 찾을 수 없는 USER ID 입니다."));

        userBalance.charge(chargeAmount);
        PaymentDetail paymentDetail = PaymentDetail.of(chargeAmount, paymentDetailRequest);
        paymentDetailRepository.save(paymentDetail);
    }
}