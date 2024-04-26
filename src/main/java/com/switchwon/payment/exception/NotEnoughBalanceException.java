package com.switchwon.payment.exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException() {
        super("사용 할 금액이 충분하지 않습니다.");
    }
}
