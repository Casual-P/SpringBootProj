package com.example.springjwtauth.dto;

import com.example.springjwtauth.component.Roles;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"id", "name"})
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
    private Set<Roles> roles;
    private Boolean isBanned;
    private String auth_provider;
    private String userOauthId;
}
