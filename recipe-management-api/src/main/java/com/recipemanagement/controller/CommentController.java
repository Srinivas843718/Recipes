package com.recipemanagement.controller;

import com.recipemanagement.dto.CommentDto;
import com.recipemanagement.exception.CommentNotFoundException;
import com.recipemanagement.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments() {
        List<CommentDto> comments = commentService.getComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getComment(@PathVariable String id) throws CommentNotFoundException {
        Optional<CommentDto> comment = commentService.getComment(id);
        return ResponseEntity.ok(comment.orElse(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable String id
            , @RequestBody CommentDto updatedCommentDto) throws CommentNotFoundException {
        Optional<CommentDto> updatedComment = commentService.updateComment(id, updatedCommentDto);
        return ResponseEntity.ok(updatedComment.orElse(null));
    }

    @PutMapping("/{id}/status-approval")
    public ResponseEntity<CommentDto> updateCommentStatus(@PathVariable String id
            , @RequestBody CommentDto updatedCommentDto) throws CommentNotFoundException {
        Optional<CommentDto> updatedComment = commentService.updateCommentStatus(id, updatedCommentDto);
        return ResponseEntity.ok(updatedComment.orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable String id) throws CommentNotFoundException {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto) {
        CommentDto newComment = commentService.addComment(commentDto);
        return ResponseEntity.ok(newComment);
    }
}
