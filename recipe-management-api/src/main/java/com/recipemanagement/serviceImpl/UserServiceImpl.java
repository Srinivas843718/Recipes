package com.recipemanagement.serviceimpl;

import com.recipemanagement.dto.ItemDto;
import com.recipemanagement.dto.RecipeDto;
import com.recipemanagement.dto.UserDto;
import com.recipemanagement.entity.*;
import com.recipemanagement.exception.BaseException;
import com.recipemanagement.exception.UserNotFoundException;
import com.recipemanagement.repository.IngredientRepository;
import com.recipemanagement.repository.NutritionRepository;
import com.recipemanagement.repository.RecipeRepository;
import com.recipemanagement.repository.UserRepository;
import com.recipemanagement.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientsRepository;

    @Autowired
    private NutritionRepository nutritionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<UserDto> getUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map(userEntity -> {
            UserDto userDto = modelMapper.map(userEntity, UserDto.class);
            userDto.setRole("ROLE_USER");
            return userDto;
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUser(String id) throws UserNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isEmpty()) {
            throw new UserNotFoundException("User not found");
        } else {
            userEntity.get().setPassword(null);
            UserDto userDto = modelMapper.map(userEntity.get(), UserDto.class);
            userDto.setRole("ROLE_USER");
            return Optional.of(userDto);
        }
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return modelMapper.map(savedUserEntity, UserDto.class);
    }

    @Override
    public Optional<UserDto> updateUser(String id, UserDto updatedUserDto) throws UserNotFoundException {
        Optional<UserEntity> existingUserEntity = userRepository.findById(id);
        if (existingUserEntity.isEmpty()) throw new UserNotFoundException("User not found");

        UserEntity updatedUserEntity = modelMapper.map(updatedUserDto, UserEntity.class);
        updatedUserEntity.setUserId(id);
        userRepository.save(updatedUserEntity);

        updatedUserEntity.setPassword(null);
        UserDto userDto = modelMapper.map(updatedUserEntity, UserDto.class);
        userDto.setRole("ROLE_USER");
        return Optional.of(userDto);
    }

    @Override
    public void deleteUser(String id) throws UserNotFoundException {
        Optional<UserEntity> existingUserEntity = userRepository.findById(id);
        if (existingUserEntity.isEmpty()) throw new UserNotFoundException("User not found");

        userRepository.deleteById(id);
    }

    @Override
    public UserDto findByEmail(String email) throws UserNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Unknown user"));

        userEntity.setPassword(null);
        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto register(UserDto userDto) {
        // Check if the email already exists
        Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isPresent()) {
            throw new BaseException("Email already exists");
        }

        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        savedUserEntity.setPassword(null);

        return modelMapper.map(savedUserEntity, UserDto.class);
    }


    @Override
    public UserDto login(UserDto userDto) throws UserNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(userDto.getEmail());

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            if (userEntity.getPassword().equals(userDto.getPassword())) {
                UserDto loggedInUser = modelMapper.map(userEntity, UserDto.class);
                loggedInUser.setRole("ROLE_USER");
                loggedInUser.setPassword(null);
                return loggedInUser;
            } else {
                throw new UserNotFoundException("Invalid password");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public List<RecipeDto> getUserRecipes(String userId) {
        // Find recipes by user ID
        List<RecipeEntity> userRecipes = recipeRepository.findByUserId(userId);

        // Map the List of RecipeEntity to List of RecipeDto
        return userRecipes.stream()
                .map(this::mapToRecipeDto)
                .collect(Collectors.toList());
    }

    public RecipeDto mapToRecipeDto(RecipeEntity recipeEntity) {
        RecipeDto recipeDto = modelMapper.map(recipeEntity, RecipeDto.class);

        // Fetch and set IngredientsList
        Optional<IngredientsEntity> ingredients = ingredientsRepository.findByRecipeId(recipeEntity.getRecipeId());
        if (ingredients.isPresent()) {
            List<ItemDto> ingredientsDtoList = mapToItemDtoList(ingredients.get().getItemQuant());
            recipeDto.setIngredientsList(ingredientsDtoList);
        }

        // Fetch and set nutritionsList
        Optional<NutritionEntity> nutritions = nutritionRepository.findByRecipeId(recipeEntity.getRecipeId());
        if (nutritions.isPresent()) {
            List<ItemDto> nutritionsDtoList = mapToItemDtoList(nutritions.get().getItemQuant());
            recipeDto.setNutritionsList(nutritionsDtoList);
        }

        return recipeDto;
    }

    private List<ItemDto> mapToItemDtoList(List<ItemQuantEntity> items) {
        return items.stream()
                .map(itemQuantEntity -> modelMapper.map(itemQuantEntity, ItemDto.class))
                .collect(Collectors.toList());
    }

}
