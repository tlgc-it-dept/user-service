package com.jc4balos.user_service.service.role;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;

import com.jc4balos.user_service.dto.request.user.ChangeEmailDto;

public interface RoleService {
    CompletableFuture<ResponseEntity<?>> createRole(ChangeEmailDto changeEmailDto);

    CompletableFuture<ResponseEntity<?>> getAllRoles();

    CompletableFuture<ResponseEntity<?>> modifyRoles();

    CompletableFuture<ResponseEntity<?>> deactivateRole();

}
