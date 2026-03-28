package com.jc4balos.user_service.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRoleDto {

    @NotBlank(message = "Role is not valid.")
    private String roleName;

    @NotBlank(message = "Role description is not valid.")
    private String roleDescription;
}
