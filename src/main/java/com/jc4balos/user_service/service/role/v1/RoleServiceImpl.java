package com.jc4balos.user_service.service.role.v1;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jc4balos.user_service.dto.request.role.NewRoleDto;
import com.jc4balos.user_service.mapper.role_mapper.RoleMapper;
import com.jc4balos.user_service.model.Role;
import com.jc4balos.user_service.repository.RoleRepository;
import com.jc4balos.user_service.service.role.RoleService;
import com.jc4balos.user_service.service.users.v1.UserServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional
    @Async
    @Override
    public CompletableFuture<ResponseEntity<?>> createRole(NewRoleDto newRoleDto) {

        Role newRole = roleMapper.newRoleDto(newRoleDto);

        roleRepository.save(newRole);
        String message = "Role " + newRole.getRoleName() + " created successfully.";
        logger.info(message);
        ResponseEntity<?> response = new ResponseEntity<>(Map.of("message", message), HttpStatus.CREATED);
        return CompletableFuture.completedFuture(response);

    }

    @Override
    public CompletableFuture<ResponseEntity<?>> getAllRoles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllRoles'");
    }

    @Override
    public CompletableFuture<ResponseEntity<?>> modifyRoles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifyRoles'");
    }

    @Override
    public CompletableFuture<ResponseEntity<?>> deactivateRole() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivateRole'");
    }

}
