package com.jc4balos.user_service.service.users.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jc4balos.user_service.dto.request.user.ChangeEmailDto;
import com.jc4balos.user_service.dto.request.user.ChangePasswordDto;
import com.jc4balos.user_service.dto.request.user.LoginDto;
import com.jc4balos.user_service.dto.request.user.ModifyUserInfoDto;
import com.jc4balos.user_service.dto.request.user.NewUserDto;
import com.jc4balos.user_service.dto.response.user.LoginResponseDto;
import com.jc4balos.user_service.dto.response.user.UserCredentialsDto;
import com.jc4balos.user_service.dto.response.user.ViewUserDto;
import com.jc4balos.user_service.mapper.user_mapper.UserMapper;
import com.jc4balos.user_service.model.Role;
import com.jc4balos.user_service.model.User;
import com.jc4balos.user_service.repository.RoleAssignmentRepository;
import com.jc4balos.user_service.repository.UserRepository;
import com.jc4balos.user_service.service.users.UserService;
import com.jc4balos.user_service.utils.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleAssignmentRepository roleAssignmentRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    @Transactional
    @Async
    public CompletableFuture<ResponseEntity<?>> createUser(NewUserDto newUserDto) {

        User newUser = userMapper.newUserDto(newUserDto);

        Optional.ofNullable(userRepository.findByUsername(newUserDto.getUsername()))
                .ifPresent(user -> {
                    throw new RuntimeException("Username already used.");
                });

        Optional.ofNullable(userRepository.findByEmail(newUserDto.getEmail()))
                .ifPresent(user -> {
                    throw new RuntimeException("Email already used.");
                });

        userRepository.save(newUser);
        String message = "User " + newUser.getUsername() + " created successfully.";
        logger.info(message);
        ResponseEntity<?> response = new ResponseEntity<>(Map.of("message", message), HttpStatus.CREATED);
        return CompletableFuture.completedFuture(response);

    }

    @Override
    public CompletableFuture<ResponseEntity<?>> getAllUsers(int pageIndex, int itemsPerPage, String searchParam,
            String sortBy,
            String order) {

        List<ViewUserDto> viewUserDtos = new ArrayList<>();
        Sort sort;

        System.out.println("SORTING ORDER: " + order);

        if (order.equals("ASCENDING")) {
            sort = Sort.by(sortBy).ascending();
        } else if (order.equals("DESCENDING")) {
            sort = Sort.by(sortBy).descending();
        } else {
            String message = "Cannot fetch users. Unknown sorting order.";
            logger.error(message);
            throw new RuntimeException(message);
        }

        Pageable pageAndSort = PageRequest.of(pageIndex, itemsPerPage, sort);
        Page<User> users = userRepository.findBySearchParam(searchParam, pageAndSort);

        for (User user : users) {
            ViewUserDto mappedUser = userMapper.viewUserDto(user);
            viewUserDtos.add(mappedUser);
        }

        Map<String, Object> data = Map.of("pageIndex", users.getNumber(),
                "totalPages", users.getTotalPages(),
                "users", viewUserDtos);

        ResponseEntity<?> response = new ResponseEntity<>(data, HttpStatus.OK);

        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ResponseEntity<?>> modifyUserInfo(String userUUID, ModifyUserInfoDto modifyUserInfoDto) {
        User userToBeModified = userRepository.findByUserUUID(userUUID);
        if (userToBeModified == null) {
            throw new RuntimeException("User doesn't exist");
        }

        User modifiedUser = userMapper.modifyUserInfoDto(modifyUserInfoDto, userToBeModified);
        userRepository.save(modifiedUser);
        Map<String, String> data = Map.of("message", modifiedUser.getUsername() + " successfully modified.");

        ResponseEntity<?> response = new ResponseEntity<>(data, HttpStatus.OK);
        return CompletableFuture
                .completedFuture(response);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ResponseEntity<?>> changeEmail(String userUUID, ChangeEmailDto changeEmailDto) {
        User thisUser = userRepository.findByUserUUID(userUUID);

        if (thisUser == null) {
            throw new RuntimeException("User doesn't exist.");
        }

        thisUser.setEmail(changeEmailDto.getNewEmail());
        userRepository.save(thisUser);

        Map<String, String> data = Map.of("message",
                thisUser.getUsername() + " email was successfully modified.");
        ResponseEntity<?> response = new ResponseEntity<>(data, HttpStatus.OK);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<ResponseEntity<?>> changePassword(String userUUID, ChangePasswordDto changePasswordDto) {
        User thisUser = userRepository.findByUserUUID(userUUID);

        if (thisUser == null) {
            throw new RuntimeException("User doesn't exist.");
        }

        // check if old password matches the password in db
        Boolean isPasswordCorrect = bCryptPasswordEncoder.matches(changePasswordDto.getOldPassword(),
                thisUser.getPassword());
        if (!isPasswordCorrect) {
            throw new RuntimeException("Incorrect old Password. Please try again.");
        }

        // check if newpassword matches confirm new password
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
            throw new RuntimeException("New password and confirm password doesn't match. Please try again.");
        }

        String hashedNewPassword = bCryptPasswordEncoder.encode(changePasswordDto.getNewPassword());

        thisUser.setPassword(hashedNewPassword);
        userRepository.save(thisUser);
        Map<String, String> data = Map.of("message", thisUser.getUsername() + " password was successfully modified.");
        ResponseEntity<?> response = new ResponseEntity<>(data, HttpStatus.OK);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Async
    public CompletableFuture<ResponseEntity<?>> getUser(String username) {
        User optionalUser = userRepository.findByUsername(username);

        if (optionalUser == null) {
            Map<String, String> data = Map.of("message", "User " + username + " doesn't exist.");
            ResponseEntity<?> response = new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            return CompletableFuture.completedFuture(response);
        }

        UserCredentialsDto userCredentialsDto = userMapper.userCredentialsDto(optionalUser);
        ResponseEntity<?> response = new ResponseEntity<>(userCredentialsDto, HttpStatus.OK);
        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Async
    public CompletableFuture<ResponseEntity<?>> login(LoginDto loginDto) {
        User optionalUser = userRepository.findByUsername(loginDto.getUsername());

        if (optionalUser == null || optionalUser.getIsActive() == false) {
            Map<String, String> data = Map.of("message", "Incorrect username or password. Please try again.");
            ResponseEntity<?> response = new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            return CompletableFuture.completedFuture(response);
        }

        Boolean isPasswordCorrect = bCryptPasswordEncoder.matches(loginDto.getPassword(), optionalUser.getPassword());

        if (!isPasswordCorrect) {
            Map<String, String> data = Map.of("message", "Incorrect username or password. Please try again.");
            ResponseEntity<?> response = new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            return CompletableFuture.completedFuture(response);
        }

        List<Role> userRoles = roleAssignmentRepository.findRolesByUser(optionalUser.getUserId());

        System.out.println("ROLES: " + userRoles);

        List<String> roleNames = new ArrayList<>();
        List<String> roleUUIDs = new ArrayList<>();
        for (Role role : userRoles) {
            roleNames.add(role.getRoleName());
            roleUUIDs.add(role.getRoleUUID());
        }

        String token = jwtUtil.generateToken(optionalUser,
                roleNames, roleUUIDs);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .message("Login successful.")
                .token(token)
                .build();

        ResponseEntity<?> response = ResponseEntity.ok().body(loginResponseDto);
        return CompletableFuture.completedFuture(response);
    }

}
