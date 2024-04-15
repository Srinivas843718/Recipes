package com.recipemanagement.service;

import com.recipemanagement.dto.NutritionDto;
import com.recipemanagement.exception.NutritionNotFoundException;

import java.util.List;
import java.util.Optional;

public interface NutritionService {
    List<NutritionDto> getNutritions();

    Optional<NutritionDto> getNutrition(String id) throws NutritionNotFoundException;

    NutritionDto addNutrition(NutritionDto nutritionDto);

    Optional<NutritionDto> updateNutrition(String id, NutritionDto updatedNutritionDto) throws NutritionNotFoundException;

    void deleteNutrition(String id) throws NutritionNotFoundException;
}
