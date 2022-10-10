package com.example.springjwtauth.service;

import com.example.springjwtauth.dto.UserDto;
import com.example.springjwtauth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.user.OAuth2User;

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
}
