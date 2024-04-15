package com.recipemanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class CommentEntity {

    @Id
    private String commentId;
    private String userName;
    private String recipeId;
    private String comment;
    private String status;
}
