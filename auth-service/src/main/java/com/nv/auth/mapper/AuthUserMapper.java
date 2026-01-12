package com.nv.auth.mapper;

import com.nv.auth.dto.UserResponse;
import com.nv.auth.entity.AuthUser;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;

import java.util.List;

@Component
public class AuthUserMapper {

    public UserResponse toDto(AuthUser user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setStatus(user.getStatus());
        dto.setIsEmailVerified(user.getIsEmailVerified());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public List<UserResponse> toDtoList(List<AuthUser> users) {
        return users.stream().map(this::toDto).toList();
    }

    public Page<UserResponse> toDtoPage(Page<AuthUser> users) {
        return users.map(this::toDto);
    }
}
