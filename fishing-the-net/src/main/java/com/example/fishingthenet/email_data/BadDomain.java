package com.example.fishingthenet.email_data;

import com.example.fishingthenet.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bad_domain")
@NoArgsConstructor
@AllArgsConstructor
public class BadDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String domain;

}