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
import com.jc4balos.user_service.mapper.role_assignment_mapper.RoleAssignmentMapper;
import com.jc4balos.user_service.mapper.role_mapper.RoleMapper;
import com.jc4balos.user_service.model.Role;
import com.jc4balos.user_service.model.RoleAssignment;
import com.jc4balos.user_service.model.User;
import com.jc4balos.user_service.repository.RoleAssignmentRepository;
import com.jc4balos.user_service.repository.RoleRepository;
import com.jc4balos.user_service.repository.UserRepository;
import com.jc4balos.user_service.service.role.RoleService;
import com.jc4balos.user_service.service.users.v1.UserServiceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleAssignmentRepository roleAssignmentRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleAssignmentMapper roleAssignmentMapper;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Transactional
    @Async
    @Override
    public CompletableFuture<ResponseEntity<?>> createRole(NewRoleDto newRoleDto) {

        Role newRole = roleMapper.newRoleDto(newRoleDto);

        if (roleRepository.existsByRoleKey(newRoleDto.getRoleKey())) {
            throw new RuntimeException("Role key already used.");
        }

        if (roleRepository.existsByRoleName(newRoleDto.getRoleName())) {
            throw new RuntimeException("Role name already used.");
        }

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

        roleMapper.updateRoleFromDto(thisRole, newRoleDto);
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

        if (roleAssignmentRepository.existsByUserAndRole(thisUser, thisRole)) {
            String alreadyAssignedMessage = "The user has already been assigned the role '" + thisRole.getRoleName()
                    + "'.";
            logger.warn("User '" + thisUser.getUsername() + "' already has the role '" + thisRole.getRoleName()
                    + "'. Duplicate assignment prevented.");
            ResponseEntity<?> response = new ResponseEntity<>(Map.of("message", alreadyAssignedMessage),
                    HttpStatus.CONFLICT);
            return CompletableFuture.completedFuture(response);
        }

        RoleAssignment newRoleAssignment = roleAssignmentMapper.newRoleAssignment(thisRole, thisUser);
        roleAssignmentRepository.save(newRoleAssignment);

        String message = "Role " + thisRole.getRoleName() + " successfully assigned to " + thisUser.getFirstName() + " "
                + thisUser.getFatherSurname() + " " + thisUser.getHusbandSurname() + ".";
        logger.info(message);
        ResponseEntity<?> response = new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<ResponseEntity<?>> deactivateRole(String roleUUID) {
        Role thisRole = roleRepository.findByRoleUUID(roleUUID);

        if (thisRole == null) {
            throw new RuntimeException("Role doesn't exist.");
        }

        if (Boolean.FALSE.equals(thisRole.getIsActive())) {
            throw new RuntimeException("Role is already inactive.");
        }

        thisRole.setIsActive(false);
        roleRepository.save(thisRole);

        String message = "Role " + thisRole.getRoleName() + " successfully deactivated.";
        logger.info(message);
        ResponseEntity<?> response = new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<ResponseEntity<?>> removeRole(String userUUID, String roleUUID) {
        User thisUser = userRepository.findByUserUUID(userUUID);
        Role thisRole = roleRepository.findByRoleUUID(roleUUID);

        if (thisUser == null) {
            throw new RuntimeException("User doesn't exist.");
        }

        if (thisRole == null) {
            throw new RuntimeException("Role doesn't exist.");
        }

        RoleAssignment roleAssignment = roleAssignmentRepository.findByUserAndRole(thisUser, thisRole);
        if (roleAssignment == null) {
            throw new RuntimeException("User doesn't have this role.");
        }

        roleAssignmentRepository.delete(roleAssignment);

        String message = "Role " + thisRole.getRoleName() + " successfully removed from "
                + thisUser.getFirstName() + " " + thisUser.getFatherSurname() + " "
                + thisUser.getHusbandSurname() + ".";
        logger.info(message);
        ResponseEntity<?> response = new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
        return CompletableFuture.completedFuture(response);
    }

}
