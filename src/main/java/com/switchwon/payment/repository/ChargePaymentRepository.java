package com.switchwon.payment.repository;

import com.switchwon.payment.domain.ChargePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChargePaymentRepository extends JpaRepository<ChargePayment, Long> {
}
