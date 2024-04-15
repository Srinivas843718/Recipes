package com.recipemanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "payment_details")
public class PaymentsEntity {

    @Id
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
    private String created_At;
    private String updated_At;
}
