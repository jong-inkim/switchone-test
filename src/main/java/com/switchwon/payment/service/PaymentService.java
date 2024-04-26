package com.switchwon.payment.service;

import com.switchwon.order.domain.Order;
import com.switchwon.order.dto.OrderEstimateRequest;
import com.switchwon.order.dto.OrderEstimateResponse;
import com.switchwon.order.repository.OrderRepository;
import com.switchwon.payment.exception.DupliatedMerchantIdException;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderEstimateResponse estimate(OrderEstimateRequest request) {
        if (orderRepository.findByMerchantId(request.merchantId()).isPresent()) {
            throw new DupliatedMerchantIdException(request.merchantId());
        }
        Order order = Order.of(request.merchantId(), request.amount(), request.currency());
        order.toReady();
        Order savedOrder = orderRepository.save(order);
        return OrderEstimateResponse.from(savedOrder);
    }
}
