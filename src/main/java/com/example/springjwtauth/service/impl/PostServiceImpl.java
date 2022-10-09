package com.example.springjwtauth.service.impl;

import com.example.springjwtauth.dto.CommentDto;
import com.example.springjwtauth.entity.Comment;
import com.example.springjwtauth.entity.User;
import com.example.springjwtauth.mapper.PostMapper;
import com.example.springjwtauth.repository.PostRepository;
import com.example.springjwtauth.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapper mapper;

    @Override
    public CommentDto savePost(CommentDto commentDto) {
        commentDto.setDate(LocalDateTime.now());
        Comment comment = mapper.postDtoToPost(commentDto);
        comment.setDate(LocalDateTime.now());
        return mapper.postToPostDto(postRepository.save(comment));
    }

    @Override
    public List<CommentDto> deleteAllPostsByUser(User user) {
        return postRepository.deleteAllByFrom(user).stream().map(mapper::postToPostDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto deletePostById(String id) {
        Comment comment = postRepository.findById(id).orElseThrow();
        postRepository.delete(comment);
        return mapper.postToPostDto(comment);
    }

    @Override
    public Page<CommentDto> getPostsByDateTimeAfter(LocalDateTime localDateTime, PageRequest pageRequest) {
        return postRepository.findAllByDateAfter(localDateTime, pageRequest).map(mapper::postToPostDto);
    }

    @Override
    public CommentDto getPostById(String id) {
        return mapper.postToPostDto(postRepository.findById(id).orElseThrow());
    }

    @Override
    public CommentDto updatePost(CommentDto commentDto) {
        Comment comment = postRepository.findById(commentDto.get_id()).orElseThrow();
        comment.setText(commentDto.getText());
        comment.setDate(LocalDateTime.now());
        postRepository.save(comment);
        return mapper.postToPostDto(comment);
    }

    @Override
    public Page<CommentDto> getPageablePosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(mapper::postToPostDto);
    }
}
