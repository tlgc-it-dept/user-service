package com.jc4balos.user_service.service.role.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.jc4balos.user_service.dto.request.role.NewRoleDto;
import com.jc4balos.user_service.dto.response.role.ViewRoleDto;
import com.jc4balos.user_service.mapper.role_mapper.RoleMapper;
import com.jc4balos.user_service.model.Role;
import com.jc4balos.user_service.model.User;
import com.jc4balos.user_service.repository.RoleRepository;
import com.jc4balos.user_service.repository.UserRepository;
import com.jc4balos.user_service.service.role.RoleService;
import com.jc4balos.user_service.service.users.v1.UserServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

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
    public CompletableFuture<ResponseEntity<?>> getAllRoles(int pageIndex, int itemsPerPage, String searchParam,
            String sortBy,
            String order) {

        List<ViewRoleDto> viewRoleDtos = new ArrayList<>();
        Sort sort;

        System.out.println("SORTING ORDER: " + order);

        if (order.equals("ASCENDING")) {
            sort = Sort.by(sortBy).ascending();
        } else if (order.equals("DESCENDING")) {
            sort = Sort.by(sortBy).descending();
        } else {
            String message = "Cannot fetch roles. Unknown sorting order.";
            logger.error(message);
            throw new RuntimeException(message);
        }

        Pageable pageAndSort = PageRequest.of(pageIndex, itemsPerPage, sort);
        Page<Role> roles = roleRepository.findBySearchParam(searchParam, pageAndSort);

        for (Role role : roles) {
            ViewRoleDto mappedRole = roleMapper.viewRoleDto(role);
            viewRoleDtos.add(mappedRole);
        }

        Map<String, Object> data = Map.of("pageIndex", roles.getNumber(),
                "totalPages", roles.getTotalPages(),
                "users", viewRoleDtos);

        ResponseEntity<?> response = new ResponseEntity<>(data, HttpStatus.OK);

        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<ResponseEntity<?>> modifyRoles(String roleUUID, NewRoleDto newRoleDto) {
        Role thisRole = roleRepository.findByRoleUUID(roleUUID);

        if (thisRole == null) {
            throw new RuntimeException("Role doesn't exist.");
        }

        thisRole = roleMapper.newRoleDto(newRoleDto);
        roleRepository.save(thisRole);

        String message = "Role " + thisRole.getRoleName() + " successfully modified.";
        logger.info(message);
        ResponseEntity<?> response = new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<ResponseEntity<?>> assignRole(String userUUID, String roleUUID) {
        Role thisRole = roleRepository.findByRoleUUID(roleUUID);
        User thisUser = userRepository.findByUserUUID(userUUID);

        if (thisRole == null) {
            throw new RuntimeException("Role doesn't exist.");
        }

        if (thisUser == null) {
            throw new RuntimeException("User doesn't exist.");
        }

        roleRepository.save(thisRole);

        String message = "Role " + thisRole.getRoleName() + " successfully assigned to user with UUID: " + userUUID;
        logger.info(message);
        ResponseEntity<?> response = new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    public CompletableFuture<ResponseEntity<?>> deactivateRole() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deactivateRole'");
    }

}
