package com.jc4balos.user_service.mapper.role_mapper;

import org.springframework.stereotype.Component;

import com.jc4balos.user_service.dto.request.role.NewRoleDto;
import com.jc4balos.user_service.model.Role;

@Component
public class RoleMapper {
    public Role newRoleDto(NewRoleDto newRoleDto) {
        Role role = Role.builder().roleName(newRoleDto.getRoleName()).roleDescription(newRoleDto.getRoleDescription())
                .isActive(true).build();
        return role;
    }
}
