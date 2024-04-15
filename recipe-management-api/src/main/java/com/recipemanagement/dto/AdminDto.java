package com.recipemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {
    private String adminId;
    private String username; // Add this field

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String token;

    private String role;
    private List<GrantedAuthority> authorities; // Roles and permissions

    public Function setToken;

}

