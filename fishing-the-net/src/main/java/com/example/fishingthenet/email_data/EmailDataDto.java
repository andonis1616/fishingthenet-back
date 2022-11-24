package com.example.fishingthenet.email_data;

import com.example.fishingthenet.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDataDto {

    private long id;
    private String subject;
    private String sender;
    private String content;
}