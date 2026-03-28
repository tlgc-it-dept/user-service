package com.jc4balos.user_service.dto.response.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewRoleDto {
    private String roleUUID;

    private String roleName;

    private String roleDescription;
}
