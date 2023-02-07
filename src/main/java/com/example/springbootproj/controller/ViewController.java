package com.example.springbootproj.controller;

import com.example.springbootproj.dto.CommentDto;
import com.example.springbootproj.dto.UserDto;
import com.example.springbootproj.service.PostService;
import com.example.springbootproj.service.UserService;
import com.example.springbootproj.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/view")
@Slf4j
public class ViewController {

    private final PostService postService;

    private final UserService userService;

    private final NewsService newsService;

    @GetMapping("/posts/{page}")
    @ResponseStatus(HttpStatus.OK)
    public String getPosts(@PathVariable int page, Model model) {
        model.addAttribute("comments", postService.getPageablePosts(
                PageRequest.of(page, 9999, Sort.by(Sort.Direction.ASC, "date"))
        ).toList());
        model.addAttribute("comment", new CommentDto());
        return "posts";
    }

    @GetMapping("/main")
    @ResponseStatus(HttpStatus.OK)
    public String main(Model model) {
        model.addAttribute("news", newsService.getThreeRandomNews());
        return "main";
    }

    @GetMapping("/authentication")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> authorization(Authentication authentication) {
        log.debug("{}", authentication.toString());
        return ResponseEntity.ok(authentication);
    }

    @PutMapping("/posts")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public RedirectView savePost(@ModelAttribute CommentDto commentDto, Authentication authentication) {
        log.debug("{}", commentDto.toString());
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(baseUrl + "/api/view/posts/0");
        UserDto curUser = userService.getAuthenticatedUser(authentication);
        commentDto.setFromUser(curUser);
        postService.savePost(commentDto);
        return redirectView;
    }

    @GetMapping("/about")
    @ResponseStatus(HttpStatus.OK)
    public String getLinks() {
        return "about";
    }

    @DeleteMapping("/post/delete/{id}")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public RedirectView deletePostById(@PathVariable Long id) {
        log.info("Delete comment by id = {}", id);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(baseUrl + "/api/view/posts/0");
        CommentDto commentDto = postService.deletePostById(id);
        log.info("Deleted comment: {}", commentDto);
        return redirectView;
    }

    @PatchMapping("/post/edit")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public RedirectView editPost(@ModelAttribute CommentDto commentDto) {
        RedirectView redirectView = new RedirectView();
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        redirectView.setUrl(baseUrl + "/api/view/posts/0");
        postService.updatePost(commentDto);
        log.info("Deleted comment: {}", commentDto);
        return redirectView;
    }
}
