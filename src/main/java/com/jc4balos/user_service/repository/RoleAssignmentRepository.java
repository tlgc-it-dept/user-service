package com.jc4balos.user_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jc4balos.user_service.model.Role;
import com.jc4balos.user_service.model.RoleAssignment;
import com.jc4balos.user_service.model.User;

public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {

    List<RoleAssignment> findByUser(User user);

    RoleAssignment findByUserAndRole(User user, Role role);

    boolean existsByUserAndRole(User user, Role role);

    @Query("""
                SELECT ra
                FROM RoleAssignment ra
                JOIN FETCH ra.user
                JOIN FETCH ra.role
                WHERE ra.user.userId IN :userIDs
                AND ra.role.isActive = true
            """)
    List<RoleAssignment> findByUserIdsWithActiveRoles(@Param("userIDs") List<Long> userIDs);

    // @Query(nativeQuery = true, value = """
    // SELECT b.* FROM role_assignment a
    // INNER JOIN role b ON a.role_id = b.role_id
    // INNER JOIN users c ON a.user_id = c.user_id
    // WHERE c.user_id = :userID
    // """)
    // List<Role> findRoleNamesByUser(@Param("userID") Long userID);

    @Query("""
                SELECT ra.role
                FROM RoleAssignment ra
                WHERE ra.user.userId = :userID
            """)
    List<Role> findRolesByUser(@Param("userID") Long userID);
}
