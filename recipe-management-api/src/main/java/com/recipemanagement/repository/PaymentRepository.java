package com.recipemanagement.repository;

import com.recipemanagement.entity.PaymentsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentsEntity, String> {
    // Add any custom queries if needed
}
