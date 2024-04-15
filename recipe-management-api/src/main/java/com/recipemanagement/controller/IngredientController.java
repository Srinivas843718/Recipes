package com.recipemanagement.controller;

import com.recipemanagement.dto.IngredientsDto;
import com.recipemanagement.exception.IngredientsNotFoundException;
import com.recipemanagement.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping
    public ResponseEntity<List<IngredientsDto>> getIngredients() {
        List<IngredientsDto> ingredients = ingredientService.getIngredients();
        return ResponseEntity.ok(ingredients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientsDto> getIngredient(@PathVariable String id) throws IngredientsNotFoundException {
        Optional<IngredientsDto> ingredient = ingredientService.getIngredient(id);
        return ResponseEntity.ok(ingredient.orElse(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientsDto> updateIngredient(@PathVariable String id, @RequestBody IngredientsDto updatedIngredientsDto) throws IngredientsNotFoundException {
        Optional<IngredientsDto> updatedIngredient = ingredientService.updateIngredient(id, updatedIngredientsDto);
        return ResponseEntity.ok(updatedIngredient.orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable String id) throws IngredientsNotFoundException {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<IngredientsDto> addIngredient(@RequestBody IngredientsDto ingredientDto) {
        IngredientsDto newIngredient = ingredientService.addIngredient(ingredientDto);
        return ResponseEntity.ok(newIngredient);
    }
}
