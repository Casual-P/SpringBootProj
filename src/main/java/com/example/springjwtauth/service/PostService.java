package com.example.springjwtauth.service;

import com.example.springjwtauth.dto.CommentDto;
import com.example.springjwtauth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    CommentDto savePost(CommentDto commentDto);

    List<CommentDto> deleteAllPostsByUser(User user);

    CommentDto deletePostById(String id);

    Page<CommentDto> getPostsByDateTimeAfter(LocalDateTime localDateTime, PageRequest pageRequest);

    CommentDto getPostById(String id);

    CommentDto updatePost(CommentDto commentDto);

    Page<CommentDto> getPageablePosts(Pageable pageable);
}
