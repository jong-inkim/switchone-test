package com.switchwon.payment.exception;

public class DupliatedMerchantIdException extends RuntimeException {

    public DupliatedMerchantIdException(String merchantId) {
        super("이미 존재하는 merchant id 입니다: " + merchantId);
    }
}
