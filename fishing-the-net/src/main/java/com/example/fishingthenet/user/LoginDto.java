package com.example.fishingthenet.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginDto {
    private String username;
    private String password;
}