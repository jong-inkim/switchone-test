package com.switchwon.payment.exception;

public class DoNotMatchedAmountException extends RuntimeException {
    public DoNotMatchedAmountException() {
        super("금액이 일치하지 않아 결제를 실패했습니다.");
    }
}
