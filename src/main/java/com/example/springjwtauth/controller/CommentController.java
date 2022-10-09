package com.example.springjwtauth.controller;

import com.example.springjwtauth.dto.CommentDto;
import com.example.springjwtauth.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
