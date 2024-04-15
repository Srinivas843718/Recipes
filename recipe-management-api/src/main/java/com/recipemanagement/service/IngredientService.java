package com.recipemanagement.service;

import com.recipemanagement.dto.IngredientsDto;
import com.recipemanagement.exception.IngredientsNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IngredientService {
    List<IngredientsDto> getIngredients();

    Optional<IngredientsDto> getIngredient(String id) throws IngredientsNotFoundException;

    IngredientsDto addIngredient(IngredientsDto ingredientDto);

    Optional<IngredientsDto> updateIngredient(String id, IngredientsDto updatedIngredientsDto) throws IngredientsNotFoundException;

    void deleteIngredient(String id) throws IngredientsNotFoundException;
}
