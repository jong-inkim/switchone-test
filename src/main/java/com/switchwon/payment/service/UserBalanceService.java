package com.switchwon.payment.service;

import com.switchwon.payment.domain.UserBalance;
import com.switchwon.payment.dto.BalanceResponse;
import com.switchwon.payment.repository.UserBalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserBalanceService {

    UserBalanceRepository userBalanceRepository;

    public UserBalanceService(UserBalanceRepository paymentRepository) {
        this.userBalanceRepository = paymentRepository;
    }

    public BalanceResponse getBalanceByUserId(String userId) {
        UserBalance member = userBalanceRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId + " : 찾을 수 없는 USER ID 입니다."));

        return BalanceResponse.from(member);
    }
}