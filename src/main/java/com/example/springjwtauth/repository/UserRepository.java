package com.example.springjwtauth.repository;

import com.example.springjwtauth.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> deleteUserByUsername(String username);

    Optional<User> deleteByEmail(String eMail);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> deleteByUserOauthId(String userOauthId);

    Optional<User> findByUserOauthId(String userOauthId);
}
