package com.example.springjwtauth.service.impl;

import com.example.springjwtauth.dto.UserDto;
import com.example.springjwtauth.entity.Role;
import com.example.springjwtauth.entity.User;
import com.example.springjwtauth.exeption.UserAlreadyExistException;
import com.example.springjwtauth.mapper.UserMapper;
import com.example.springjwtauth.repository.UserRepository;
import com.example.springjwtauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
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
            user.setRoles(Collections.singleton(Role.USER));
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
    public UserDto deleteUserById(String id) {
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
    public UserDto getUserById(String id) {
        return userMapper.userToUserDto(userRepository.findById(id).orElseThrow());
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User updatedUser = userMapper.userDtoToUser(userDto);
        User currentUser = userRepository.findById(updatedUser.get_id()).orElseThrow();
        if(!updatedUser.getPassword().equals(currentUser.getPassword()))
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        updatedUser.set_id(currentUser.get_id());
        return userMapper.userToUserDto(userRepository.save(updatedUser));
    }


    @Override
    public List<UserDto> getPageableUsers(Pageable pageable) {
        return userRepository.findAll(pageable).toList()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }
}
