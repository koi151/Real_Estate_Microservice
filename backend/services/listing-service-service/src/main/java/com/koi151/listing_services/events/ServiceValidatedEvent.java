package com.koi151.listing_services.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceValidatedEvent {
    private Long propertyId;
//    private String accountId;
    private BigDecimal totalFee;
}
