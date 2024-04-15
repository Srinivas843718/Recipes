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
@Document(collection = "ingredients")
public class IngredientsEntity {

    @Id
    private String ingredientId;

    private String recipeId;
    private List<ItemQuantEntity> itemQuant;
}
