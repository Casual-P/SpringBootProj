package com.example.springbootproj.service;

import com.example.springbootproj.dto.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    UserDto saveUser(UserDto user);

    UserDto deleteUserByUsername(String userName);

    UserDto deleteUserByEmail(String email);

    UserDto deleteUserById(Long id);

    UserDto getUserByUsername(String username);

    UserDto getUserByEmail(String eMail);

    UserDto getUserById(Long id);

    UserDto updateUser(UserDto userDto);

    List<UserDto> getPageableUsers(Pageable pageable);

    UserDto getAuthenticatedUser(Authentication authentication);
}
