package com.jc4balos.user_service.mapper.role_mapper;

import org.springframework.stereotype.Component;

import com.jc4balos.user_service.dto.request.role.NewRoleDto;
import com.jc4balos.user_service.dto.response.role.ViewRoleDto;
import com.jc4balos.user_service.model.Role;

@Component
public class RoleMapper {
    public Role newRoleDto(NewRoleDto newRoleDto) {
        Role role = Role.builder().roleName(newRoleDto.getRoleName()).roleDescription(newRoleDto.getRoleDescription())
                .roleKey(newRoleDto.getRoleKey())
                .isActive(true).build();
        return role;
    }

    public Role updateRoleFromDto(Role role, NewRoleDto newRoleDto) {
        role.setRoleName(newRoleDto.getRoleName());
        role.setRoleDescription(newRoleDto.getRoleDescription());
        role.setRoleKey(newRoleDto.getRoleKey());
        return role;
    }

    public ViewRoleDto viewRoleDto(Role role) {
        ViewRoleDto viewRoleDto = ViewRoleDto.builder().roleUUID(role.getRoleUUID()).roleName(role.getRoleName())
                .roleDescription(role.getRoleDescription()).roleKey(role.getRoleKey()).build();
        return viewRoleDto;
    }
}
