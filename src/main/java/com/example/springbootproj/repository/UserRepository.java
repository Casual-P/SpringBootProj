package com.example.springbootproj.repository;

import com.example.springbootproj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> deleteUserByUsername(String username);

    Optional<User> deleteByEmail(String eMail);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> deleteByUserOauthId(String userOauthId);

    Optional<User> findByUserOauthId(String userOauthId);
}
