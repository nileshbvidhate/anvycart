package com.nv.auth.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nv.auth.dto.AccountStatus;
import com.nv.auth.dto.UserRole;
import com.nv.auth.entity.AuthUser;

public interface UserRepository extends JpaRepository<AuthUser, Long> {
	Optional<AuthUser> findByUsername(String username);
	
	Page<AuthUser> findByStatus(AccountStatus status, Pageable pageable);

    Page<AuthUser> findByRole(UserRole role, Pageable pageable);

    Page<AuthUser> findByStatusAndRole(AccountStatus status, UserRole role, Pageable pageable);
}
