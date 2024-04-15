package com.recipemanagement.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipemanagement.dto.CommentDto;
import com.recipemanagement.dto.RecipeDto;
import com.recipemanagement.exception.RecipeNotFoundException;
import com.recipemanagement.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping
    public ResponseEntity<List<RecipeDto>> getRecipes() {
        List<RecipeDto> recipes = recipeService.getRecipes();
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto> getRecipe(@PathVariable String id) throws RecipeNotFoundException {
        Optional<RecipeDto> recipe = recipeService.getRecipe(id);
        return ResponseEntity.ok(recipe.orElse(null));
    }

    @PutMapping("/{id}/status-approval")
    public ResponseEntity<RecipeDto> approveOrRejectRecipe(@PathVariable String id
            , @RequestBody RecipeDto recipeDto) throws RecipeNotFoundException {
        Optional<RecipeDto> recipe = recipeService.approveOrRejectRecipe(id, recipeDto);
        return ResponseEntity.ok(recipe.orElse(null));
    }

    @PutMapping("/{id}/likes")
    public ResponseEntity<RecipeDto> updateLikes(@PathVariable String id
            , @RequestBody RecipeDto recipeDto) throws RecipeNotFoundException {
        Optional<RecipeDto> recipe = recipeService.updateLikes(id, recipeDto);
        return ResponseEntity.ok(recipe.orElse(null));
    }

    @PutMapping("/{id}/rating")
    public ResponseEntity<RecipeDto> updateRating(@PathVariable String id
            , @RequestBody RecipeDto recipeDto) throws RecipeNotFoundException {
        Optional<RecipeDto> recipe = recipeService.updateRating(id, recipeDto);
        return ResponseEntity.ok(recipe.orElse(null));
    }

    @PutMapping("/{id}/views")
    public ResponseEntity<RecipeDto> updateViews(@PathVariable String id
            , @RequestBody RecipeDto recipeDto) throws RecipeNotFoundException {
        Optional<RecipeDto> recipe = recipeService.updateViews(id, recipeDto);
        return ResponseEntity.ok(recipe.orElse(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeDto> updateRecipe(@PathVariable String id
            , @RequestPart(name = "image", required = false) MultipartFile image
            , @RequestPart("recipeDto") String recipeDtoJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RecipeDto recipeDto = objectMapper.readValue(recipeDtoJson, RecipeDto.class);
        recipeDto.setRecipeImage(image);
        Optional<RecipeDto> updatedRecipe = recipeService.updateRecipe(id, recipeDto);
        return ResponseEntity.ok(updatedRecipe.orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) throws RecipeNotFoundException {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<RecipeDto> addRecipe(@RequestPart("image") MultipartFile image,
                                               @RequestPart("recipeDto") String recipeDtoJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RecipeDto recipeDto = objectMapper.readValue(recipeDtoJson, RecipeDto.class);
        recipeDto.setRecipeImage(image);
        RecipeDto newRecipe = recipeService.addRecipe(recipeDto);
        return ResponseEntity.ok(newRecipe);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getRecipeComments(@PathVariable String id) throws RecipeNotFoundException {
        List<CommentDto> comments = recipeService.getRecipeComments(id);
        return ResponseEntity.ok(comments);
    }

}
