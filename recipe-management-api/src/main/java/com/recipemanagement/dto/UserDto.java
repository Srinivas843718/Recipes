package com.recipemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String username;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String token;
    private String role;
    private String dob;
    private String address;
    private List<GrantedAuthority> authorities;
}
