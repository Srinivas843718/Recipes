package com.recipemanagement.service;

import com.recipemanagement.dto.CommentDto;
import com.recipemanagement.dto.RecipeDto;
import com.recipemanagement.exception.RecipeNotFoundException;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<RecipeDto> getRecipes();

    Optional<RecipeDto> getRecipe(String id) throws RecipeNotFoundException;

    RecipeDto addRecipe(RecipeDto recipeDto);

    Optional<RecipeDto> updateRecipe(String id, RecipeDto updatedRecipeDto) throws RecipeNotFoundException;

    void deleteRecipe(String id) throws RecipeNotFoundException;

    List<CommentDto> getRecipeComments(String id);

    Optional<RecipeDto> approveOrRejectRecipe(String id, RecipeDto recipeDto);

    Optional<RecipeDto> updateLikes(String id, RecipeDto recipeDto);

    Optional<RecipeDto> updateRating(String id, RecipeDto recipeDto);

    Optional<RecipeDto> updateViews(String id, RecipeDto recipeDto);
}
