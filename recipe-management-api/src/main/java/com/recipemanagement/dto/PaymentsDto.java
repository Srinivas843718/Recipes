package com.recipemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsDto {
    private String paymentId;
    private String subscriptionId;
    private String paymentType;
    private String cardNumber;
    private String cardName;
    private int expiryMonth;
    private int expiryYear;
    private String cvv;
    private String status;
    private double totalAmount;
}
