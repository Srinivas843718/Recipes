package com.recipemanagement.service;

import com.recipemanagement.dto.RecipeDto;
import com.recipemanagement.dto.UserDto;
import com.recipemanagement.entity.RecipeEntity;
import com.recipemanagement.exception.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getUsers();

    Optional<UserDto> getUser(String id) throws UserNotFoundException;

    UserDto addUser(UserDto userDto);

    Optional<UserDto> updateUser(String id, UserDto updatedUserDto) throws UserNotFoundException;

    void deleteUser(String id) throws UserNotFoundException;

    UserDto findByEmail(String email) throws UserNotFoundException;

    UserDto register(UserDto userDto);

    UserDto login(UserDto userDto);

    List<RecipeDto> getUserRecipes(String id);

    RecipeDto mapToRecipeDto(RecipeEntity recipeEntity);
}
