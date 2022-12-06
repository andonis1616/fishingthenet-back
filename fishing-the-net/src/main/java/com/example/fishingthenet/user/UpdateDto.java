package com.example.fishingthenet.user;

import lombok.Data;

@Data
public class UpdateDto {


    private String oldName;
    private String newName;
    private String oldUsername;
    private String newUsername;
    private String oldEmail;
    private String newEmail;
    private String oldPassword;
    private String newPassword;
}