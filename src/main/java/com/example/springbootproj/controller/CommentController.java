package com.example.springbootproj.controller;

import com.example.springbootproj.dto.CommentDto;
import com.example.springbootproj.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {

    private final PostService postService;

    @PutMapping("/new")
    public CommentDto createOne(@RequestBody CommentDto commentDto) {
        return postService.savePost(commentDto);
    }

    @GetMapping("/page")
    public List<CommentDto> getPage(@RequestParam int page, @RequestParam int size) {
        return postService.getPageablePosts(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "date"))).toList();
    }
}
