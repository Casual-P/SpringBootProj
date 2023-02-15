package com.example.springbootproj.entity;

import com.example.springbootproj.component.Roles;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Entity(name = "users")
@NamedEntityGraph(name = "FetchRoles", attributeNodes = @NamedAttributeNode("roles"))
@NamedEntityGraph(name = "FetchComments", attributeNodes = @NamedAttributeNode("comments"))
@NamedEntityGraph(name = "FetchCommentsAndRoles", attributeNodes = {@NamedAttributeNode("comments"), @NamedAttributeNode("roles")})
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private Set<Role> roles;
    private Boolean isBanned;
    @Column(name = "oauth_provider")
    private String auth_provider;
    private String userOauthId;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "toUser")
    @ToString.Exclude
    private List<Comment> comments;
}



