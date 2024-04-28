package com.switchwon.payment.service;

import com.switchwon.payment.domain.ChargePayment;
import com.switchwon.payment.domain.Wallet;
import com.switchwon.payment.dto.BalanceResponse;
import com.switchwon.payment.dto.PaymentDetailRequest;
import com.switchwon.payment.repository.ChargePaymentRepository;
import com.switchwon.user.domain.User;
import com.switchwon.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    private final ChargePaymentRepository chargePaymentRepository;
    private final UserService userService;


    public WalletService(ChargePaymentRepository chargePaymentRepository, UserService userService) {
        this.chargePaymentRepository = chargePaymentRepository;
        this.userService = userService;
    }

    public BalanceResponse getBalanceByUserId(String userId) {
        User findUser = userService.findByUserId(userId);
        return BalanceResponse.from(findUser.getWallet());
    }

    @Transactional
    public void charge(double chargeAmount, String userId, PaymentDetailRequest paymentDetailRequest) {
        User findUser = userService.findByUserId(userId);

        Wallet wallet = findUser.getWallet();

        wallet.charge(chargeAmount);
        ChargePayment chargePayment = ChargePayment.of(chargeAmount, paymentDetailRequest, findUser);
        chargePaymentRepository.save(chargePayment);
    }

    @Transactional
    public void use(Double usedAmount, String userId) {
        User findUser = userService.findByUserId(userId);
        Wallet wallet = findUser.getWallet();
        wallet.useBalance(usedAmount);
    }
}