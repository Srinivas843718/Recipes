package com.recipemanagement.repository;

import com.recipemanagement.entity.RecipeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<RecipeEntity, String> {
    List<RecipeEntity> findByUserId(String userId);

}
