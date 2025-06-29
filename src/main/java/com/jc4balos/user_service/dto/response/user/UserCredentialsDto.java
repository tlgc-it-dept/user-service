package com.jc4balos.user_service.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsDto {

    private String userUUID;

    private String username;

    private String password;

    private String email;

    private Boolean isActive;
}
