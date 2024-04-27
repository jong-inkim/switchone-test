package com.switchwon.payment.service;

import com.switchwon.payment.domain.PaymentDetail;
import com.switchwon.payment.domain.UserBalance;
import com.switchwon.payment.dto.BalanceResponse;
import com.switchwon.payment.dto.PaymentDetailRequest;
import com.switchwon.payment.repository.PaymentDetailRepository;
import com.switchwon.payment.repository.UserBalanceRepository;
import com.switchwon.user.domain.User;
import com.switchwon.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;
    private final PaymentDetailRepository paymentDetailRepository;
    private final UserRepository userRepository;


    public UserBalanceService(UserBalanceRepository paymentRepository, PaymentDetailRepository paymentDetailRepository, UserRepository userRepository) {
        this.userBalanceRepository = paymentRepository;
        this.paymentDetailRepository = paymentDetailRepository;
        this.userRepository = userRepository;
    }

    public BalanceResponse getBalanceByUserId(String userId) {
        User findUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId + " : 찾을 수 없는 USER ID 입니다."));

        UserBalance userBalance = userBalanceRepository.findByUserId(findUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(userId + " : 해당유저의 userBalance 가 존재하지 않습니다."));

        return BalanceResponse.from(userBalance);
    }

    @Transactional
    public void charge(double chargeAmount, String userId, PaymentDetailRequest paymentDetailRequest) {
        User findUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId + " : 찾을 수 없는 USER ID 입니다."));

        UserBalance userBalance = userBalanceRepository.findByUserId(findUser.getId())
                .orElseThrow(() -> new EntityNotFoundException(userId + " : 해당유저의 userBalance 가 존재하지 않습니다."));

        userBalance.charge(chargeAmount);
        PaymentDetail paymentDetail = PaymentDetail.of(chargeAmount, paymentDetailRequest, findUser);
        paymentDetailRepository.save(paymentDetail);
    }
}