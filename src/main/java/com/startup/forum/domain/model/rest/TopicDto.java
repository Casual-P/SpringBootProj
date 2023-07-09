package com.startup.forum.domain.model.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TopicDto {
    private Long id;
    @NotBlank
    @NotNull
    @Size(min = 1, max = 50)
    private String topic_name;
    private LocalDateTime date;
    @ToString.Exclude
    @JsonIgnore
    private UserDto fromUser;
    private List<CommentDto> comments;
}
