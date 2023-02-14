package com.example.springbootproj.service.impl;

import com.example.springbootproj.component.Roles;
import com.example.springbootproj.dto.UserDto;
import com.example.springbootproj.entity.Role;
import com.example.springbootproj.entity.User;
import com.example.springbootproj.exeption.UserAlreadyExistException;
import com.example.springbootproj.exeption.UserNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public UserDto saveUser(UserDto userDto) {
        try {
            getUserByEmail(userDto.getEmail());
            throw new UserAlreadyExistException();
        } catch (UserNotFoundException e) {
            User user = userMapper.userDtoToUser(userDto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Collections.singleton(new Role(Roles.USER)));
            userRepository.save(user);
            return userMapper.userToUserDto(user);
        }
    }

    @Override
    public UserDto deleteUserByUsername(String userName) {
        UserDto deletedUser = userMapper.userToUserDto(userRepository.findByUsername(userName).orElseThrow(UserNotFoundException::new));
        userRepository.deleteUserByUsername(userName);
        return deletedUser;
    }

    @Override
    public UserDto deleteUserByEmail(String email) {
        UserDto deletedUser = userMapper.userToUserDto(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
        userRepository.deleteUserByEmail(email);
        return deletedUser;
    }

    @Override
    public UserDto deleteUserById(Long id) {
        return userMapper.userToUserDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return userMapper.userToUserDto(userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserDto getUserByEmail(String eMail) {
        return userMapper.userToUserDto(userRepository.findByEmail(eMail).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.userToUserDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User currentUser = userRepository.findById(userDto.getId()).orElseThrow(UserNotFoundException::new);
        if(!userDto.getPassword().equals(currentUser.getPassword()))
            currentUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        currentUser.setUsername(userDto.getUsername());
        currentUser.setEmail(userDto.getEmail());
        currentUser.setRoles(userDto.getRoles());
        currentUser.setIsBanned(userDto.getIsBanned());
        return userMapper.userToUserDto(currentUser);
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
