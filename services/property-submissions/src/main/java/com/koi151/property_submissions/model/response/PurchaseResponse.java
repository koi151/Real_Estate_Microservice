package com.koi151.property_submissions.model.response;

import com.koi151.property_submissions.enums.PaymentMethod;
import com.koi151.property_submissions.enums.PostStatus;

import java.time.LocalDate;

public record PurchaseResponse (
    Integer daysPosted,
    String postingPackage,
    Integer priorityPushes,
    LocalDate postingDate
){}
