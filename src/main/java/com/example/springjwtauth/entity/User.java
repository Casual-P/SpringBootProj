package com.example.springjwtauth.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Document(collection = "people")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class User {
    @MongoId(FieldType.OBJECT_ID)
    private String _id;
    private String username;
    private String password;
    private String email;
    private Set<Role> roles;
    private boolean isBanned;
    private String provider;
    private String userOauthId;
}
