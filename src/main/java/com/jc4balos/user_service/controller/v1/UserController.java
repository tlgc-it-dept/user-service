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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jc4balos.user_service.dto.request.user.ChangeEmailDto;
import com.jc4balos.user_service.dto.request.user.ChangePasswordDto;
import com.jc4balos.user_service.dto.request.user.LoginDto;
import com.jc4balos.user_service.dto.request.user.ModifyUserInfoDto;
import com.jc4balos.user_service.dto.request.user.NewUserDto;
import com.jc4balos.user_service.exception.ApplicationExceptionHandler;
import com.jc4balos.user_service.service.users.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    /*
     * TODO: Implement the security on gateway since the security here is now
     * working. Configure the environment variable for the gateway to use the
     * user_db in authenticating users. This will avoid on clashing on async
     * requests
     */
    @Autowired
    private UserService userService;

    /**
     * @param newUserDto
     * @param bindingResult
     * @return response message
     */

    @PostMapping("/register")
    @Async
    CompletableFuture<ResponseEntity<?>> createUser(@Valid @RequestBody NewUserDto newUserDto,
            BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            return userService.createUser(newUserDto)
                    .exceptionally(e -> ApplicationExceptionHandler.handleCustomException(e));
        } else {
            return CompletableFuture.completedFuture(ApplicationExceptionHandler.handleBadRequest(bindingResult));
        }
    }

    /**
     * @param pageIndex    page index of the users to get
     * @param itemsPerPage the number of records to be returned per page
     * @param searchParam  string that will match when searchng the records
     * @param sortBy       string from the database column name. The response will
     *                     be sorted by this.
     * @param order        sorting mechanism ("ASCENDING", "DESCENDING")
     * @return All users in the parameter
     */

    @GetMapping("/get-all")
    // @Async
    public CompletableFuture<ResponseEntity<?>> getAllUsers(@RequestParam int pageIndex, @RequestParam int itemsPerPage,
            @RequestParam String searchParam, @RequestParam String sortBy, @RequestParam String order) {

        return userService.getAllUsers(pageIndex, itemsPerPage, searchParam, sortBy, order)
                .exceptionally(
                        e -> ApplicationExceptionHandler.handleCustomException(e));

    }

    /**
     * 
     * @param modifyUserInfoDto DTO of editable fields
     * @param bindingResult
     * @param userId            user id of user to be modified
     * @return response message
     */
    @PutMapping("/modify/{userUUID}")
    @Async
    public CompletableFuture<ResponseEntity<?>> modifyUser(@Valid @RequestBody ModifyUserInfoDto modifyUserInfoDto,
            BindingResult bindingResult,
            @PathVariable("userUUID") String userUUID) {

        if (!bindingResult.hasErrors()) {
            return userService.modifyUserInfo(userUUID, modifyUserInfoDto).exceptionally(
                    e -> ApplicationExceptionHandler.handleCustomException(e));

        } else {
            return CompletableFuture.completedFuture(ApplicationExceptionHandler.handleBadRequest(bindingResult));
        }

    }

    @PatchMapping("/change-email/{userUUID}")
    @Async
    public CompletableFuture<ResponseEntity<?>> changeEmail(@PathVariable("userUUID") String userUUID,
            @Valid @RequestBody ChangeEmailDto newEmail,
            BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return userService.changeEmail(userUUID, newEmail).exceptionally(
                    e -> ApplicationExceptionHandler.handleCustomException(e));

        } else {
            return CompletableFuture.completedFuture(ApplicationExceptionHandler.handleBadRequest(bindingResult));
        }

    }

    @PatchMapping("/change-password/{userUUID}")
    @Async
    public CompletableFuture<ResponseEntity<?>> changePassword(@PathVariable("userUUID") String userUUID,
            @Valid @RequestBody ChangePasswordDto passwordDto,
            BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return userService.changePassword(userUUID, passwordDto).exceptionally(
                    e -> ApplicationExceptionHandler.handleCustomException(e));

        } else {
            return CompletableFuture.completedFuture(ApplicationExceptionHandler.handleBadRequest(bindingResult));
        }

    }

    @GetMapping("/user/{username}")
    @Async
    public CompletableFuture<ResponseEntity<?>> getUser(@PathVariable("username") String username) {

        return userService.getUser(username)
                .exceptionally(
                        e -> ApplicationExceptionHandler.handleCustomException(e));
    }

    @GetMapping("/user/id/{userUUID}")
    @Async
    public CompletableFuture<ResponseEntity<?>> getUserById(@PathVariable("userUUID") String userUUID) {

        return userService.getUserByUUID(userUUID)
                .exceptionally(
                        e -> ApplicationExceptionHandler.handleCustomException(e));
    }

    @PostMapping("/user/login")
    public CompletableFuture<ResponseEntity<?>> login(@Valid @RequestBody LoginDto loginDto,
            BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return userService.login(loginDto)
                    .exceptionally(e -> ApplicationExceptionHandler.handleCustomException(e));
        } else {
            return CompletableFuture.completedFuture(ApplicationExceptionHandler.handleBadRequest(bindingResult));
        }
    }

    @GetMapping("/test")
    @Async
    public String test() {
        return "You Bypassed JWTFilter!!";
    }

    // TODO: @PatchMapping("/change-contact-number/{userId}")

}
