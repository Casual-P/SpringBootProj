package com.example.springbootproj.controller;

import com.example.springbootproj.dto.CommentDto;
import com.example.springbootproj.dto.UserDto;
import com.example.springbootproj.exeption.UserAlreadyExistException;
import com.example.springbootproj.service.PostService;
import com.example.springbootproj.service.UserService;
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
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/view")
@Slf4j
public class ViewController {

    private final PostService postService;

    private final UserService userService;

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
    public String main() {
        return "main";
    }

    @GetMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String registerForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PutMapping("/register")
    public RedirectView registerUser(@ModelAttribute UserDto userDto){
        log.info("{}", userDto.toString());
        try {
            userService.saveUser(userDto);
            log.info("{}", "Saved new user");
        } catch (UserAlreadyExistException ex) {
            log.info("{}", "Request user already exists");
            return new RedirectView("http://localhost/api/view/register?error");
        }
        return new RedirectView("http://localhost/api/view/login");
    }

    @GetMapping("/authentication")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> authorization(Authentication authentication) {
        return ResponseEntity.ok(authentication);
    }

    @PutMapping("/posts")
    public RedirectView savePost(@ModelAttribute CommentDto commentDto, Authentication authentication) {
        log.info("{}", commentDto.toString());
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost/api/view/posts/0");
        commentDto.setFrom(authentication.getName());
        postService.savePost(commentDto);
        return redirectView;
    }
}
