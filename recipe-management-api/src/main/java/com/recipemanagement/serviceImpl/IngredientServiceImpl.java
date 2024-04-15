package com.recipemanagement.serviceImpl;

import com.recipemanagement.dto.IngredientsDto;
import com.recipemanagement.entity.IngredientsEntity;
import com.recipemanagement.exception.IngredientsNotFoundException;
import com.recipemanagement.repository.IngredientRepository;
import com.recipemanagement.service.IngredientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<IngredientsDto> getIngredients() {
        List<IngredientsEntity> ingredients = ingredientRepository.findAll();
        return ingredients.stream().map(ingredientEntity -> modelMapper.map(ingredientEntity, IngredientsDto.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<IngredientsDto> getIngredient(String id) throws IngredientsNotFoundException {
        Optional<IngredientsEntity> ingredientEntity = ingredientRepository.findById(id);
        if (ingredientEntity.isEmpty()) {
            throw new IngredientsNotFoundException("Ingredient not found");
        } else {
            IngredientsDto ingredientDto = modelMapper.map(ingredientEntity.get(), IngredientsDto.class);
            return Optional.of(ingredientDto);
        }
    }

    @Override
    public IngredientsDto addIngredient(IngredientsDto ingredientDto) {
        IngredientsEntity ingredientEntity = modelMapper.map(ingredientDto, IngredientsEntity.class);
        IngredientsEntity savedIngredientsEntity = ingredientRepository.save(ingredientEntity);
        return modelMapper.map(savedIngredientsEntity, IngredientsDto.class);
    }

    @Override
    public Optional<IngredientsDto> updateIngredient(String id, IngredientsDto updatedIngredientsDto) throws IngredientsNotFoundException {
        Optional<IngredientsEntity> existingIngredientsEntity = ingredientRepository.findById(id);
        if (existingIngredientsEntity.isEmpty()) throw new IngredientsNotFoundException("Ingredient not found");

        IngredientsEntity updatedIngredientsEntity = modelMapper.map(updatedIngredientsDto, IngredientsEntity.class);
        updatedIngredientsEntity.setIngredientId(id);
        ingredientRepository.save(updatedIngredientsEntity);

        return Optional.of(modelMapper.map(updatedIngredientsEntity, IngredientsDto.class));
    }

    @Override
    public void deleteIngredient(String id) throws IngredientsNotFoundException {
        Optional<IngredientsEntity> existingIngredientsEntity = ingredientRepository.findById(id);
        if (existingIngredientsEntity.isEmpty()) throw new IngredientsNotFoundException("Ingredient not found");

        ingredientRepository.deleteById(id);
    }
}
