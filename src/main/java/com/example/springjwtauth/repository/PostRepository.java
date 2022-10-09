package com.example.springjwtauth.repository;

import com.example.springjwtauth.entity.Comment;
import com.example.springjwtauth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Comment, String> {

    List<Comment> deleteAllByFrom(User from);

    Page<Comment> findAllByDateAfter(LocalDateTime localDateTime, PageRequest pageRequest);
}
