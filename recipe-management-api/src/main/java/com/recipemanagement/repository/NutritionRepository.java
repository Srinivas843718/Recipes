package com.recipemanagement.repository;

import com.recipemanagement.entity.NutritionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionRepository extends MongoRepository<NutritionEntity, String> {
    Optional<NutritionEntity> findByRecipeId(String recipeId);
    // Add any custom queries if needed
}
