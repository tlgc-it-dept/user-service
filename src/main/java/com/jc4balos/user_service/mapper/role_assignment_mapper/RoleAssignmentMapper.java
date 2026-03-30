package com.jc4balos.user_service.mapper.role_assignment_mapper;

import org.springframework.stereotype.Component;

import com.jc4balos.user_service.model.Role;
import com.jc4balos.user_service.model.RoleAssignment;
import com.jc4balos.user_service.model.User;

@Component
public class RoleAssignmentMapper {

    public RoleAssignment newRoleAssignment(Role role, User user) {
        RoleAssignment roleAssignment = RoleAssignment.builder()
                .roleAssignmentUUID(java.util.UUID.randomUUID().toString())
                .role(role)
                .user(user)
                .build();
        return roleAssignment;
    }

}
