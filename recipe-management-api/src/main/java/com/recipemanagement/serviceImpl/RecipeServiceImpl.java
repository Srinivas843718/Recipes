package com.recipemanagement.serviceImpl;

import com.recipemanagement.dto.CommentDto;
import com.recipemanagement.dto.RecipeDto;
import com.recipemanagement.entity.*;
import com.recipemanagement.exception.RecipeNotFoundException;
import com.recipemanagement.repository.CommentRepository;
import com.recipemanagement.repository.IngredientRepository;
import com.recipemanagement.repository.NutritionRepository;
import com.recipemanagement.repository.RecipeRepository;
import com.recipemanagement.service.RecipeService;
import com.recipemanagement.service.UserService;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientsRepository;
    @Autowired
    private NutritionRepository nutritionRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<RecipeDto> getRecipes() {
        List<RecipeEntity> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(userServiceImpl::mapToRecipeDto)
                .collect(Collectors.toList());
        // return recipes.stream().map(recipeEntity -> modelMapper.map(recipeEntity, RecipeDto.class)).collect(Collectors.toList());
    }

    @Override
    public Optional<RecipeDto> getRecipe(String id) throws RecipeNotFoundException {
        Optional<RecipeEntity> recipeEntity = recipeRepository.findById(id);
        if (recipeEntity.isEmpty()) {
            throw new RecipeNotFoundException("Recipe not found");
        } else {

            RecipeDto recipeDto = userServiceImpl.mapToRecipeDto(recipeEntity.get());
            return Optional.of(recipeDto);
        }
    }

    @Override
    public RecipeDto addRecipe(RecipeDto recipeDto) {
        String id = new ObjectId().toString();
        String fileName = handleFileUpload(id, recipeDto.getRecipeImage());
        // Map RecipeDto to RecipeEntity
        RecipeEntity recipeEntity = modelMapper.map(recipeDto, RecipeEntity.class);
        recipeEntity.setRecipeId(id);
        recipeEntity.setImageUrl(fileName);
        recipeEntity.setStatus("Pending");

        // Save RecipeEntity to get the generated id
        RecipeEntity savedRecipeEntity = recipeRepository.save(recipeEntity);

        // Save IngredientsEntity
        IngredientsEntity ingredientsEntity = new IngredientsEntity();
        ingredientsEntity.setRecipeId(id);
        ingredientsEntity.setItemQuant(recipeDto.getIngredientsList()
                .stream()
                .map(itemDto -> new ItemQuantEntity(itemDto.getItemName(), itemDto.getQuantity(), itemDto.getMeasurement()))
                .collect(Collectors.toList()));
        ingredientsRepository.save(ingredientsEntity);

        // Save NutritionEntity
        NutritionEntity nutritionEntity = new NutritionEntity();
        nutritionEntity.setRecipeId(id);
        nutritionEntity.setItemQuant(recipeDto.getNutritionsList()
                .stream()
                .map(itemDto -> new ItemQuantEntity(itemDto.getItemName(), itemDto.getQuantity(), itemDto.getMeasurement()))
                .collect(Collectors.toList()));
        nutritionRepository.save(nutritionEntity);


        // Map the saved RecipeEntity to RecipeDto
        return modelMapper.map(savedRecipeEntity, RecipeDto.class);
    }

    private String handleFileUpload(String recipeId, MultipartFile recipeImage) {
        String uniqueFileName = "";
        try {
            String uploadDir = "C:\\Users\\kdsri\\OneDrive\\Desktop\\UCM-1st SEM\\ADB-5600\\Final Report_12010_Group 4\\recipe-management\\recipe-management\\recipe-management-ui\\src\\images";
            String originalFileName = recipeImage.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            uniqueFileName = recipeId + fileExtension;

            Path filePath = Paths.get(uploadDir, uniqueFileName);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            Files.write(filePath, recipeImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uniqueFileName;
    }


    @Override
    public Optional<RecipeDto> updateRecipe(String id, RecipeDto updatedRecipeDto) throws RecipeNotFoundException {
        // Find the existing recipe by id
        Optional<RecipeEntity> existingRecipeEntity = recipeRepository.findById(id);
        if (existingRecipeEntity.isEmpty()) throw new RecipeNotFoundException("Recipe not found");

        RecipeEntity updatedRecipeEntity = modelMapper.map(updatedRecipeDto, RecipeEntity.class);
        String fileName = "";
        if (updatedRecipeDto.getRecipeImage() != null) {
            fileName = handleFileUpload(id, updatedRecipeDto.getRecipeImage());
            updatedRecipeEntity.setImageUrl(fileName);
        }

        if (existingRecipeEntity.get().getStatus() != null) {
            updatedRecipeEntity.setStatus(existingRecipeEntity.get().getStatus());
        } else {
            updatedRecipeEntity.setStatus("Pending");
        }

        updatedRecipeEntity.setRecipeId(id);
        // Save the updated recipe entity
        RecipeEntity savedRecipeEntity = recipeRepository.save(updatedRecipeEntity);

        // Update IngredientsEntity
        IngredientsEntity ingredientsEntity = ingredientsRepository.findByRecipeId(id)
                .orElseThrow(() -> new RecipeNotFoundException("Ingredients not found for recipe with id: " + id));

        // Update ItemQuantEntities in IngredientsEntity
        ingredientsEntity.setItemQuant(updatedRecipeDto.getIngredientsList()
                .stream()
                .map(itemDto -> new ItemQuantEntity(itemDto.getItemName(), itemDto.getQuantity(), itemDto.getMeasurement()))
                .collect(Collectors.toList()));

        ingredientsRepository.save(ingredientsEntity);

        // Update NutritionEntity
        NutritionEntity nutritionEntity = nutritionRepository.findByRecipeId(id)
                .orElseThrow(() -> new RecipeNotFoundException("Nutrition not found for recipe with id: " + id));

        // Update ItemQuantEntities in NutritionEntity
        nutritionEntity.setItemQuant(updatedRecipeDto.getNutritionsList()
                .stream()
                .map(itemDto -> new ItemQuantEntity(itemDto.getItemName(), itemDto.getQuantity(), itemDto.getMeasurement()))
                .collect(Collectors.toList()));

        nutritionRepository.save(nutritionEntity);

        // Map the saved RecipeEntity to RecipeDto and return it
        return Optional.of(modelMapper.map(savedRecipeEntity, RecipeDto.class));
    }


    @Override
    public void deleteRecipe(String id) throws RecipeNotFoundException {
        Optional<RecipeEntity> existingRecipeEntity = recipeRepository.findById(id);
        if (existingRecipeEntity.isEmpty()) throw new RecipeNotFoundException("Recipe not found");

        recipeRepository.deleteById(id);
    }

    @Override
    public List<CommentDto> getRecipeComments(String id) {
        List<CommentEntity> comments = commentRepository.findByRecipeId(id);
        return comments.stream()
                .map(commentEntity -> modelMapper.map(commentEntity, CommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RecipeDto> approveOrRejectRecipe(String id, RecipeDto recipeDto) {
        Optional<RecipeEntity> existingRecipeEntity = recipeRepository.findById(id);
        if (existingRecipeEntity.isEmpty()) throw new RecipeNotFoundException("Recipe not found");
        existingRecipeEntity.get().setStatus(recipeDto.getStatus());
        recipeRepository.save(existingRecipeEntity.get());
        return Optional.of(modelMapper.map(existingRecipeEntity, RecipeDto.class));
    }

    @Override
    public Optional<RecipeDto> updateLikes(String id, RecipeDto recipeDto) {
        Optional<RecipeEntity> existingRecipeEntity = recipeRepository.findById(id);
        if (existingRecipeEntity.isEmpty()) throw new RecipeNotFoundException("Recipe not found");

        String userId = recipeDto.getLikes().get(0);
        List<String> likes = existingRecipeEntity.get().getLikes() != null ? existingRecipeEntity.get().getLikes() :
                new ArrayList<>();
        if (likes.contains(userId)) likes.remove(userId);
        else likes.add(userId);

        existingRecipeEntity.get().setLikes(likes);
        recipeRepository.save(existingRecipeEntity.get());
        return Optional.of(modelMapper.map(existingRecipeEntity, RecipeDto.class));
    }

    @Override
    public Optional<RecipeDto> updateRating(String id, RecipeDto recipeDto) {
        Optional<RecipeEntity> existingRecipeEntity = recipeRepository.findById(id);
        if (existingRecipeEntity.isEmpty()) throw new RecipeNotFoundException("Recipe not found");

        List<RatingEntity> ratings = existingRecipeEntity.get().getRatings() != null ? existingRecipeEntity.get().getRatings() :
                new ArrayList<RatingEntity>();
        String userIdToUpdate = recipeDto.getRatings().get(0).getUserId();
        double newRatingValue = recipeDto.getRatings().get(0).getRating();

        // Check if the user has already rated the recipe
        Optional<RatingEntity> userRating = Optional.of(ratings)
                .filter(r -> r.size() >= 1).flatMap(r -> r.stream()
                        .filter(rating -> rating.getUserId().equals(userIdToUpdate))
                        .findFirst());


        if (userRating.isPresent()) {
            // If the user has already rated, update the existing rating
            userRating.get().setRating(newRatingValue);
        } else {
            // If the user has not rated, add a new rating
            ratings.add(new RatingEntity(userIdToUpdate, newRatingValue));
        }

        // Calculate the total rating based on the updated ratings list
        double totalRating = ratings.stream()
                .mapToDouble(RatingEntity::getRating)
                .average()
                .orElse(0.0);

// Round the totalRating to a single digit
        double roundedTotalRating = Math.round(totalRating * 10.0) / 10.0;

// Set the updated ratings list and rounded total rating to the recipe entity
        existingRecipeEntity.get().setRatings(ratings);
        existingRecipeEntity.get().setRating(roundedTotalRating);

        // Save the updated recipe entity
        recipeRepository.save(existingRecipeEntity.get());

        return Optional.of(modelMapper.map(existingRecipeEntity.get(), RecipeDto.class));
    }

    @Override
    public Optional<RecipeDto> updateViews(String id, RecipeDto recipeDto) {
        Optional<RecipeEntity> existingRecipeEntity = recipeRepository.findById(id);
        if (existingRecipeEntity.isEmpty()) throw new RecipeNotFoundException("Recipe not found");

        existingRecipeEntity.get().setNumberOfViews(existingRecipeEntity.get().getNumberOfViews() + 1);
        recipeRepository.save(existingRecipeEntity.get());
        return Optional.of(modelMapper.map(existingRecipeEntity, RecipeDto.class));
    }


}
