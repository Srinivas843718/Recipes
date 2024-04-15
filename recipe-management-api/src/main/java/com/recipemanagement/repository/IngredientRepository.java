package com.recipemanagement.repository;

import com.recipemanagement.entity.IngredientsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientRepository extends MongoRepository<IngredientsEntity, String> {
    Optional<IngredientsEntity> findByRecipeId(String recipeId);
    // Add any custom queries if needed
}
