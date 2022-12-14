package com.example.springbootproj.mapper;

import com.example.springbootproj.dto.CommentDto;
import com.example.springbootproj.dto.UserDto;
import com.example.springbootproj.entity.Comment;
import com.example.springbootproj.entity.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "fromUser", expression = "java(userDtoToUser(commentDto.getFromUser()))")
    @Mapping(target = "toUser", expression = "java(userDtoToUser(commentDto.getToUser()))")
    Comment postDtoToPost(CommentDto commentDto);

    @Mapping(target = "fromUser", expression = "java(userToUserDto(comment.getFromUser()))")
    @Mapping(target = "toUser", expression = "java(userToUserDto(comment.getToUser()))")
    CommentDto postToPostDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "fromUser", ignore = true)
    @Mapping(target = "toUser", ignore = true)
    Comment updatePostFromPostDto(CommentDto commentDto, @MappingTarget Comment comment);

    default User userDtoToUser(UserDto userDto) {
        return UserMapper.INSTANCE.userDtoToUser(userDto);
    }

    default UserDto userToUserDto(User user) {
        return UserMapper.INSTANCE.userToUserDto(user);
    }
}
