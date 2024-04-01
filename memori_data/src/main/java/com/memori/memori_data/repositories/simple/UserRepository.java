package com.memori.memori_data.repositories.simple;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.memori.memori_domain.User;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
