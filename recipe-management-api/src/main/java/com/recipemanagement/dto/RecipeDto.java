package com.recipemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDto {
    private String recipeId;
    private String userId;
    private String userName;
    private String title;
    private String listOfIngredients;
    private String listOfNutritions;
    private String preparationTime;
    private List<String> instructions;
    private List<String> likes;
    private String premium;
    private String status;
    private MultipartFile recipeImage;
    private String imageUrl;
    private int numberOfViews;
    private List<CommentDto> comments;
    private double rating;
    private List<RatingDto> ratings;
    private List<ItemDto> ingredientsList;
    private List<ItemDto> nutritionsList;

}
