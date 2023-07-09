package com.startup.forum.domain.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CommentDto {
    private Long id;
    @NotBlank
    @NotNull
    @Size(min = 1, max = 355)
    private String text;
    private LocalDateTime date;
    @ToString.Exclude
    @JsonIgnore
    private UserDto fromUser;
    @ToString.Exclude
    @JsonIgnore
    private UserDto toUser;

    public String getFromUsername() {
        return fromUser == null? "anonymous" : fromUser.getUsername();
    }
}
