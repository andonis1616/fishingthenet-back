package com.example.fishingthenet.email_data;

import com.example.fishingthenet.user.Role;
import com.example.fishingthenet.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "email")
@NoArgsConstructor
@AllArgsConstructor
public class EmailData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String subject;
    private String sender;
    private String content;
    private Boolean isFishing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

}