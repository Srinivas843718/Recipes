package com.recipemanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemQuantEntity {

    private String itemName;
    private String quantity;
    private String measurement;
}
