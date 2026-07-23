package com.jc4balos.user_service.service.role;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jc4balos.user_service.dto.request.role.NewRoleDto;

@Service
public interface RoleService {
    CompletableFuture<ResponseEntity<?>> createRole(NewRoleDto newRoleDto);

    CompletableFuture<ResponseEntity<?>> getAllRoles(int pageIndex, int itemsPerPage, String searchParam,
            String sortBy,
            String order);

    CompletableFuture<ResponseEntity<?>> modifyRoles(String roleUUID, NewRoleDto newRoleDto);

    CompletableFuture<ResponseEntity<?>> deactivateRole(String roleUUID);

    CompletableFuture<ResponseEntity<?>> assignRole(String userUUID, String roleUUID);

    CompletableFuture<ResponseEntity<?>> removeRole(String userUUID, String roleUUID);

    CompletableFuture<ResponseEntity<?>> getRolesFromUser(String userUUID);

}
