package com.example.springbootproj.controller.rest;

import com.example.springbootproj.dto.UserDto;
import com.example.springbootproj.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Rest Controller for simple operations with app users.")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/pageable/{page}")
    @Operation(summary = "Get users from database pageable.",description = "Pages starts from 0. Also can receive page size value in request params. Default page size value is 10. ")
    public List<UserDto> getUsers(@PathVariable Integer page, @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        log.debug("Get users pageable request with page = {} and page size = {}", page, pageSize);
        return userService.getPageableUsers(PageRequest.of(page, pageSize));
    }

    @PutMapping("/new")
    @Operation(summary = "Create new user if not exists.")
    public UserDto createOne(@RequestBody UserDto userDto) {
        log.debug("Create a new user request with Dto: {}", userDto.toString());
        return userService.saveUser(userDto);
    }

    @PatchMapping("/update")
    @Operation(summary = "Update user if exists.")
    public UserDto updateOne(@RequestBody UserDto userDto) {
        log.debug("Update user request with Dto: {}", userDto.toString());
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/delete/email")
    @Operation(summary = "Delete user by email if exists.")
    public UserDto deleteOneByEmail(@RequestBody String email) {
        log.debug("Delete user by email request with email: {}", email);
        return userService.deleteUserByEmail(email);
    }

    @DeleteMapping("/delete/username")
    public UserDto deleteOneByUsername(@RequestBody String username) {
        log.debug("Delete user by username request with username: {}", username);
        return userService.deleteUserByUsername(username);
    }
}
