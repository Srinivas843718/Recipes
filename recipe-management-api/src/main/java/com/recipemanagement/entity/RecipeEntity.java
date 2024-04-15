package com.recipemanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "recipe")
public class RecipeEntity {

    @Id
    private String recipeId;
    private String userId;
    private String userName;
    private String title;
    private String preparationTime;
    private List<String> instructions;
    private List<String> likes;
    private String premium;
    private String status;
    private String imageUrl;
    private int numberOfViews;
    private List<CommentEntity> comments;
    private double rating;
    private List<RatingEntity> ratings;
}
