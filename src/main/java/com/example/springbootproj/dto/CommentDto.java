package com.example.springbootproj.dto;

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
    private String from;
    private String to;

}
