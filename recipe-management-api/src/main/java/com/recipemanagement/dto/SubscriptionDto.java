package com.recipemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private String subscriptionId;
    private String userId;
    private double amount;
    private String status;
    private PaymentsDto paymentDetails;
}
