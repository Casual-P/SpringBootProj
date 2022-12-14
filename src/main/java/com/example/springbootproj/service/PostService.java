package com.example.springbootproj.service;

import com.example.springbootproj.dto.CommentDto;
import com.example.springbootproj.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    CommentDto savePost(CommentDto commentDto);

    List<CommentDto> deleteAllPostsByUser(User user);

    CommentDto deletePostById(Long id);

    Page<CommentDto> getPostsByDateTimeAfter(LocalDateTime localDateTime, PageRequest pageRequest);

    CommentDto getPostById(Long id);

    CommentDto updatePost(CommentDto commentDto);

    Page<CommentDto> getPageablePosts(Pageable pageable);
}
