package com.recipemanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "subscriptions")
public class SubscriptionEntity {

    @Id
    private String subscriptionId;

    private String userId;

    private String status;
    private double amount;
}
