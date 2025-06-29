package com.jc4balos.user_service.service.users;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jc4balos.user_service.dto.request.user.ChangeEmailDto;
import com.jc4balos.user_service.dto.request.user.ChangePasswordDto;
import com.jc4balos.user_service.dto.request.user.ModifyUserInfoDto;
import com.jc4balos.user_service.dto.request.user.NewUserDto;

@Service
public interface UserService {

    CompletableFuture<ResponseEntity<?>> changeEmail(String userUUID, ChangeEmailDto changeEmailDto);

    CompletableFuture<ResponseEntity<?>> changePassword(String userUUID, ChangePasswordDto changePasswordDto);

    CompletableFuture<ResponseEntity<?>> createUser(NewUserDto newUserDto);

    CompletableFuture<ResponseEntity<?>> getAllUsers(int pageIndex, int itemsPerPage, String searchParam,
            String sortBy, String order);

    CompletableFuture<ResponseEntity<?>> modifyUserInfo(String userUUID, ModifyUserInfoDto modifyUserInfoDto);

    CompletableFuture<ResponseEntity<?>> getUser(String username);

}
