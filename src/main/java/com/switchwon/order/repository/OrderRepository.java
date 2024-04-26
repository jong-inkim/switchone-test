package com.switchwon.order.repository;

import com.switchwon.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByMerchantId(String merchantId);
}
