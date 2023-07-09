package com.startup.forum.interactor.controller;

import com.startup.forum.domain.model.rest.UserDto;
import com.startup.forum.domain.exception.UserAlreadyExistException;
import com.startup.forum.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;

    @PutMapping("/register")
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public RedirectView registerUser(@ModelAttribute UserDto userDto){
        log.debug("{}", userDto.toString());
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        try {
            userService.saveUser(userDto);
            log.debug("{}", "Saved new user");
        } catch (UserAlreadyExistException ex) {
            log.debug("{}", "Request user already exists");
            return new RedirectView(baseUrl + "/api/auth/register?error");
        }
        return new RedirectView(baseUrl + "/api/auth/login");
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
}
