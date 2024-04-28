package com.switchwon.payment.service;

import com.switchwon.payment.domain.PaymentDetail;
import com.switchwon.payment.domain.Wallet;
import com.switchwon.payment.dto.BalanceResponse;
import com.switchwon.payment.dto.PaymentDetailRequest;
import com.switchwon.payment.repository.PaymentDetailRepository;
import com.switchwon.user.domain.User;
import com.switchwon.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    private final PaymentDetailRepository paymentDetailRepository;
    private final UserService userService;


    public WalletService(PaymentDetailRepository paymentDetailRepository, UserService userService) {
        this.paymentDetailRepository = paymentDetailRepository;
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
        PaymentDetail paymentDetail = PaymentDetail.of(chargeAmount, paymentDetailRequest, findUser);
        paymentDetailRepository.save(paymentDetail);
    }

    @Transactional
    public void use(Double usedAmount, String userId) {
        User findUser = userService.findByUserId(userId);
        Wallet wallet = findUser.getWallet();
        wallet.useBalance(usedAmount);
    }
}