package com.startup.forum.domain.service;

import com.startup.forum.domain.model.rest.CommentDto;
import com.startup.forum.infrastructure.storage.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
