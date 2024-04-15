package com.recipemanagement.controller;

import com.recipemanagement.dto.NutritionDto;
import com.recipemanagement.exception.NutritionNotFoundException;
import com.recipemanagement.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/nutritions")
public class NutritionController {

    @Autowired
    private NutritionService nutritionService;

    @GetMapping
    public ResponseEntity<List<NutritionDto>> getNutritions() {
        List<NutritionDto> nutritions = nutritionService.getNutritions();
        return ResponseEntity.ok(nutritions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionDto> getNutrition(@PathVariable String id) throws NutritionNotFoundException {
        Optional<NutritionDto> nutrition = nutritionService.getNutrition(id);
        return ResponseEntity.ok(nutrition.orElse(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NutritionDto> updateNutrition(@PathVariable String id, @RequestBody NutritionDto updatedNutritionDto) throws NutritionNotFoundException {
        Optional<NutritionDto> updatedNutrition = nutritionService.updateNutrition(id, updatedNutritionDto);
        return ResponseEntity.ok(updatedNutrition.orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNutrition(@PathVariable String id) throws NutritionNotFoundException {
        nutritionService.deleteNutrition(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<NutritionDto> addNutrition(@RequestBody NutritionDto nutritionDto) {
        NutritionDto newNutrition = nutritionService.addNutrition(nutritionDto);
        return ResponseEntity.ok(newNutrition);
    }
}
