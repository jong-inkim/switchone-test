package com.switchwon.payment.exception;

public class DoNotMachedMerchantIdException extends RuntimeException {

    public DoNotMachedMerchantIdException() {
        super("해당 계정의 상점아이디가 아닙니다.");
    }
}
