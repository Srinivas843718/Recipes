package com.recipemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsDto {
    private String ingredientId;
    private String recipeId;
    private List<ItemDto> ingredients;
}
