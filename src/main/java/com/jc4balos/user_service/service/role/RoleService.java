package com.jc4balos.user_service.service.role;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jc4balos.user_service.dto.request.role.NewRoleDto;

@Service
public interface RoleService {
    CompletableFuture<ResponseEntity<?>> createRole(NewRoleDto newRoleDto);

    CompletableFuture<ResponseEntity<?>> getAllRoles();

    CompletableFuture<ResponseEntity<?>> modifyRoles();

    CompletableFuture<ResponseEntity<?>> deactivateRole();

}
