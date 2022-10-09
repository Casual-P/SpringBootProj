package com.example.springjwtauth.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document("posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Comment {
    @MongoId
    private String _id;
    private String text;
    private LocalDateTime date;
    private String from;
    private String to;
}
