package com.switchwon.payment.repository;

import com.switchwon.payment.domain.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
    Optional<UserBalance> findByUserId(String userId);
}
