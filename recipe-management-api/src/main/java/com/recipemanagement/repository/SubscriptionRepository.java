package com.recipemanagement.repository;

import com.recipemanagement.entity.SubscriptionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends MongoRepository<SubscriptionEntity, String> {
    Optional<SubscriptionEntity> findByUserId(String id);
    // Add any custom queries if needed
}
