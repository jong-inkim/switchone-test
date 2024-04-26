package com.switchwon.order.dto;

import com.switchwon.order.domain.Order;
import com.switchwon.payment.domain.CurrencyCode;

public record OrderEstimateResponse(double estimatedTotal, double fees, CurrencyCode currency) {

    public static OrderEstimateResponse from(Order order) {
        return new OrderEstimateResponse(order.getTotalAmount(), order.getFees(), order.getCurrency());
    }
}
