package com.koi151.msproperty.customExceptions;

public class PaymentScheduleNotFoundException extends RuntimeException {
    public PaymentScheduleNotFoundException(String message) {
        super(message);
    }

}
