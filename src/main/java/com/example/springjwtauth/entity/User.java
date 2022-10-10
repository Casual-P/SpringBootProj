package com.example.springjwtauth.entity;

import com.example.springjwtauth.component.Roles;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity(name = "people")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @ElementCollection(targetClass = Roles.class, fetch = FetchType.EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;
    private boolean isBanned;
    @Column(name = "oauth_provider")
    private String auth_provider;
    @Column(unique = true)
    private String userOauthId;
}



