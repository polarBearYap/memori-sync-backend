package com.memori.memori_data.repositories.simple;

import org.springframework.data.jpa.repository.JpaRepository;

import com.memori.memori_domain.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}