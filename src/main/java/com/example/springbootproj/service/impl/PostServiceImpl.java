package com.example.springbootproj.service.impl;

import com.example.springbootproj.dto.CommentDto;
import com.example.springbootproj.entity.Comment;
import com.example.springbootproj.entity.User;
import com.example.springbootproj.mapper.PostMapper;
import com.example.springbootproj.repository.PostRepository;
import com.example.springbootproj.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapper mapper;

    @Override
    public CommentDto savePost(CommentDto commentDto) {
        commentDto.setToUser(null);
        Comment comment = mapper.postDtoToPost(commentDto);
        comment.setDate(LocalDateTime.now());
        postRepository.save(comment);
        return mapper.postToPostDto(comment);
    }

    @Override
    public List<CommentDto> deleteAllPostsByUser(User user) {
        return postRepository.deleteAllByFromUser(user).stream().map(mapper::postToPostDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto deletePostById(Long id) {
        Comment comment = null;
        try {
            comment = postRepository.findById(id).orElseThrow();
            postRepository.deleteById(id);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return mapper.postToPostDto(comment);
    }

    @Override
    public Page<CommentDto> getPostsByDateTimeAfter(LocalDateTime localDateTime, PageRequest pageRequest) {
        return postRepository.findAllByDateAfter(localDateTime, pageRequest).map(mapper::postToPostDto);
    }

    @Override
    public CommentDto getPostById(Long id) {
        return mapper.postToPostDto(postRepository.findById(id).orElseThrow());
    }

    @Override
    public CommentDto updatePost(CommentDto commentDto) {
        Comment comment = postRepository.findById(commentDto.getId()).orElseThrow();
        comment.setText(commentDto.getText());
//        comment.setDate(LocalDateTime.now());
        postRepository.save(comment);
        return mapper.postToPostDto(comment);
    }

    @Override
    public Page<CommentDto> getPageablePosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(mapper::postToPostDto);
    }
}
