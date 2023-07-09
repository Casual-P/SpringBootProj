package com.startup.forum.domain.model.rest;

import com.startup.forum.infrastructure.storage.entity.Comment;
import com.startup.forum.infrastructure.storage.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "name"})
@Builder
public class UserDto {
    private Long id;
    @Size(min = 5, max = 50, message = "Username should contains more then 5 characters")
    @NotBlank
    private String username;
    @Size(min = 5, max = 50, message = "Password should contains more then 5 characters")
    @NotBlank
    private String password;
    @Email
    private String email;
    @NotNull
    private Set<Role> roles;
    private Boolean isBanned;
    private String auth_provider;
    private String userOauthId;
    private List<Comment> comments;
}
