package com.jc4balos.user_service.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role", indexes = {
        @Index(name = "uuid_index", columnList = "role_uuid")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "role_id")
    private Long roleId;

    @Column(nullable = false, name = "role_uuid", unique = true, length = 36)
    private String roleUUID;

    @Column(nullable = false, name = "role_name")
    private String roleName;

    @Column(nullable = false, name = "role_desc")
    private String roleDescription;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;

    @Column(nullable = false, name = "role_key")
    private String roleKey;

    @PrePersist
    public void generateUUID() {
        this.roleUUID = UUID.randomUUID().toString();
    }
}
