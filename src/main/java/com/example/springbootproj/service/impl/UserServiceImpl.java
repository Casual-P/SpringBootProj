package com.example.springbootproj.service.impl;

import com.example.springbootproj.component.Roles;
import com.example.springbootproj.dto.UserDto;
import com.example.springbootproj.entity.Role;
import com.example.springbootproj.entity.User;
import com.example.springbootproj.exeption.UserAlreadyExistException;
import com.example.springbootproj.mapper.UserMapper;
import com.example.springbootproj.repository.UserRepository;
import com.example.springbootproj.security.CustomUserDetails;
import com.example.springbootproj.security.oauth2.impl.CustomOauth2UserDetails;
import com.example.springbootproj.service.UserService;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public UserDto saveUser(UserDto userDto) {
        try {
            getUserByEmail(userDto.getEmail());
            throw new UserAlreadyExistException();
        } catch (NoSuchElementException e) {
            User user = userMapper.userDtoToUser(userDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singleton(new Role(Roles.USER)));
            userRepository.save(user);
            return userMapper.userToUserDto(user);
        }
    }

    @Override
    public UserDto deleteUserByUsername(String userName) {
        return userMapper.userToUserDto(userRepository.deleteUserByUsername(userName).orElseThrow());
    }

    @Override
    public UserDto deleteUserByEmail(String email) {
        return userMapper.userToUserDto(userRepository.deleteByEmail(email).orElseThrow());
    }

    @Override
    public UserDto deleteUserById(Long id) {
        return userMapper.userToUserDto(userRepository.findById(id).orElseThrow());
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return userMapper.userToUserDto(userRepository.findByUsername(username).orElseThrow());
    }

    @Override
    public UserDto getUserByEmail(String eMail) {
        return userMapper.userToUserDto(userRepository.findByEmail(eMail).orElseThrow());
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.userToUserDto(userRepository.findById(id).orElseThrow());
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User updatedUser = userMapper.userDtoToUser(userDto);
        User currentUser = userRepository.findById(updatedUser.getId()).orElseThrow();
        if(!updatedUser.getPassword().equals(currentUser.getPassword()))
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        updatedUser.setId(currentUser.getId());
        return userMapper.userToUserDto(userRepository.save(updatedUser));
    }


    @Override
    public List<UserDto> getPageableUsers(Pageable pageable) {
        return userRepository.findAll(pageable).toList()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getAuthenticatedUser(Authentication authentication) {
        Assert.notNull(authentication, "Authentication has wrong type or null");
        User user;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomOauth2UserDetails) {
            user = userRepository.findByUserOauthId(((CustomOauth2UserDetails) principal).getUserOauthId()).orElseThrow(() -> new IllegalStateException("OAuth2 user has wrong provider id"));
        }else if (principal instanceof CustomUserDetails) {
            user = userRepository.findById(((CustomUserDetails) principal).getUserId()).orElseThrow(() -> new IllegalStateException("User has wrong id"));
        }
        else {
            throw new IllegalArgumentException("Authentication has wrong type.");
        }
        return userMapper.userToUserDto(user);
    }
}
