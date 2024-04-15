package com.recipemanagement.service;

import com.recipemanagement.dto.CommentDto;
import com.recipemanagement.exception.CommentNotFoundException;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<CommentDto> getComments();

    Optional<CommentDto> getComment(String id) throws CommentNotFoundException;

    CommentDto addComment(CommentDto commentDto);

    Optional<CommentDto> updateComment(String id, CommentDto updatedCommentDto) throws CommentNotFoundException;

    void deleteComment(String id) throws CommentNotFoundException;

    Optional<CommentDto> updateCommentStatus(String id, CommentDto updatedCommentDto);
}
