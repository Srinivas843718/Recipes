package com.recipemanagement.controller;

import com.recipemanagement.config.UserAuthenticationProvider;
import com.recipemanagement.dto.RecipeDto;
import com.recipemanagement.dto.UserDto;
import com.recipemanagement.exception.UserNotFoundException;
import com.recipemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) throws UserNotFoundException {
        Optional<UserDto> user = userService.getUser(id);
        return ResponseEntity.ok(user.orElse(null));
    }

    @GetMapping("/{id}/my-recipes")
    public ResponseEntity<List<RecipeDto>> getUserRecipes(@PathVariable String id) throws UserNotFoundException {
        List<RecipeDto> recipes = userService.getUserRecipes(id);
        return ResponseEntity.ok(recipes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody UserDto updatedUserDto) throws UserNotFoundException {
        Optional<UserDto> updatedUser = userService.updateUser(id, updatedUserDto);
        return ResponseEntity.ok(updatedUser.orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) throws UserNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        UserDto newUser = userService.addUser(userDto);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        UserDto user = userService.login(userDto);
        user.setToken(userAuthenticationProvider.createToken(userDto.getEmail(), user.getUserId(), "ROLE_USER"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        UserDto newUser = userService.register(userDto);
        return ResponseEntity.ok(newUser);
    }
}
