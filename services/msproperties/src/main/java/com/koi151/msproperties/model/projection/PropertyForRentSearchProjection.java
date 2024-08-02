package com.koi151.msproperties.model.projection;

import com.koi151.msproperties.enums.PaymentScheduleEnum;

import java.math.BigDecimal;

public interface PropertyForRentSearchProjection {
    BigDecimal getRentalPrice();
    PaymentScheduleEnum getPaymentSchedule();
}
