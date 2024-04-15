package com.recipemanagement.service;

import com.recipemanagement.dto.SubscriptionDto;
import com.recipemanagement.exception.SubscriptionNotFoundException;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    List<SubscriptionDto> getSubscriptions();

    Optional<SubscriptionDto> getSubscription(String id) throws SubscriptionNotFoundException;

    SubscriptionDto addSubscription(SubscriptionDto subscriptionDto);

    Optional<SubscriptionDto> updateSubscription(String id, SubscriptionDto updatedSubscriptionDto) throws SubscriptionNotFoundException;

    void deleteSubscription(String id) throws SubscriptionNotFoundException;

    SubscriptionDto getUserSubscription(String id);
}
