package com.recipemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutritionDto {
    private String nutritionId;
    private String recipeId;
    private List<ItemDto> nutritions;
}
