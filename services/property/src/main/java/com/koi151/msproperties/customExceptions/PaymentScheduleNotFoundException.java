package com.koi151.msproperties.customExceptions;

public class PaymentScheduleNotFoundException extends RuntimeException {
    public PaymentScheduleNotFoundException(String message) {
        super(message);
    }

}
