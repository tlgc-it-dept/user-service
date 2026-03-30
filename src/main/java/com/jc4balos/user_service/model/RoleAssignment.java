package com.jc4balos.user_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_assignment", indexes = {
        @Index(name = "uuid_index", columnList = "role_assignment_uuid"),
        @Index(name = "user_id_index", columnList = "user_id"),
        @Index(name = "role_id_index", columnList = "role_id")
})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "role_assignment_id")
    private Long roleAssignmentId;

    @Column(nullable = false, name = "role_assignment_uuid", unique = true, length = 36)
    private String roleAssignmentUUID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
    private Role role;
}
