package com.switchwon.payment.service;

import com.switchwon.payment.domain.Balance;
import com.switchwon.payment.domain.ChargePayment;
import com.switchwon.payment.dto.BalanceResponse;
import com.switchwon.payment.dto.PaymentDetailRequest;
import com.switchwon.payment.repository.ChargePaymentRepository;
import com.switchwon.user.domain.User;
import com.switchwon.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BalanceService {
    private final ChargePaymentRepository chargePaymentRepository;
    private final UserService userService;


    public BalanceService(ChargePaymentRepository chargePaymentRepository, UserService userService) {
        this.chargePaymentRepository = chargePaymentRepository;
        this.userService = userService;
    }

    public BalanceResponse getBalanceByUserId(String userId) {
        User findUser = userService.findByUserId(userId);
        return BalanceResponse.from(findUser.getBalance());
    }

    @Transactional
    public void charge(double chargeAmount, String userId, PaymentDetailRequest paymentDetailRequest) {
        User findUser = userService.findByUserId(userId);

        Balance balance = findUser.getBalance();

        balance.charge(chargeAmount);
        ChargePayment chargePayment = ChargePayment.of(chargeAmount, paymentDetailRequest, findUser);
        chargePaymentRepository.save(chargePayment);
    }

    @Transactional
    public void use(Double usedAmount, String userId) {
        User findUser = userService.findByUserId(userId);
        Balance balance = findUser.getBalance();
        balance.useBalance(usedAmount);
    }
}