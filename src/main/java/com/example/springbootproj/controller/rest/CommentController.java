package com.example.springbootproj.controller.rest;

import com.example.springbootproj.dto.CommentDto;
import com.example.springbootproj.dto.UserDto;
import com.example.springbootproj.service.PostService;
import com.example.springbootproj.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
@RestController
public class CommentController {

    private final PostService postService;

    private final UserService userService;

    @PutMapping("/new")
    public CommentDto createOne(@RequestBody CommentDto commentDto, Authentication authentication) {
        UserDto curUser = userService.getAuthenticatedUser(authentication);
        commentDto.setFromUser(curUser);
        return postService.savePost(commentDto);
    }

    @GetMapping("/page")
    public List<CommentDto> getPage(@RequestParam int page) {
        return postService.getPageablePosts(
                PageRequest.of(page, 9999, Sort.by(Sort.Direction.ASC, "date"))
        ).toList();
    }

    @GetMapping("/test")
    public List<?> getTest() {
        return postService.getPageablePosts(Pageable.ofSize(999)).toList();
    }

    @DeleteMapping("/delete/{id}")
    public CommentDto deleteOne(@PathVariable Long id, Authentication authentication) {
        log.info("{}", authentication);
        return postService.deletePostById(id);
    }
}


