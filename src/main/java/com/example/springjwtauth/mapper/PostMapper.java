package com.example.springjwtauth.mapper;

import com.example.springjwtauth.dto.CommentDto;
import com.example.springjwtauth.entity.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    Comment postDtoToPost(CommentDto commentDto);

    CommentDto postToPostDto(Comment comment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Comment updatePostFromPostDto(CommentDto commentDto, @MappingTarget Comment comment);
}
