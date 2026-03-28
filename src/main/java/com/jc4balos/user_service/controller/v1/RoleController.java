package com.jc4balos.user_service.controller.v1;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jc4balos.user_service.dto.request.role.NewRoleDto;
import com.jc4balos.user_service.exception.ApplicationExceptionHandler;
import com.jc4balos.user_service.service.role.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/users/roles")
@RequiredArgsConstructor
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    @Async
    CompletableFuture<ResponseEntity<?>> createRole(@Valid @RequestBody NewRoleDto newRoleDto,
            BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            return roleService.createRole(newRoleDto)
                    .exceptionally(e -> ApplicationExceptionHandler.handleCustomException(e));
        } else {
            return CompletableFuture.completedFuture(ApplicationExceptionHandler.handleBadRequest(bindingResult));
        }
    }

    @GetMapping("/get-all")
    @Async
    public CompletableFuture<ResponseEntity<?>> getAllUsers(@RequestParam int pageIndex, @RequestParam int itemsPerPage,
            @RequestParam String searchParam, @RequestParam String sortBy, @RequestParam String order) {

        return roleService.getAllRoles(pageIndex, itemsPerPage, searchParam, sortBy, order)
                .exceptionally(
                        e -> ApplicationExceptionHandler.handleCustomException(e));

    }

    @PatchMapping("/modify/{roleUUID}")
    @Async
    public CompletableFuture<ResponseEntity<?>> modifyRole(@PathVariable String roleUUID,
            @Valid @RequestBody NewRoleDto newRoleDto,
            BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            return roleService.modifyRoles(roleUUID, newRoleDto)
                    .exceptionally(e -> ApplicationExceptionHandler.handleCustomException(e));
        } else {
            return CompletableFuture.completedFuture(ApplicationExceptionHandler.handleBadRequest(bindingResult));
        }
    }
}
