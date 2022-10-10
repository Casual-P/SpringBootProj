package com.example.springbootproj.repository;

import com.example.springbootproj.entity.Comment;
import com.example.springbootproj.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Comment, Long> {

    List<Comment> deleteAllByFrom(User from);

    Page<Comment> findAllByDateAfter(LocalDateTime localDateTime, PageRequest pageRequest);
}
