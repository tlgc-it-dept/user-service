package com.jc4balos.user_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jc4balos.user_service.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(nativeQuery = true, value = """
            SELECT * FROM role
            WHERE is_active = 1
            AND (role_name LIKE CONCAT('%', :searchParam, '%'))
            """)
    Page<Role> findBySearchParam(@Param("searchParam") String searchParam, Pageable pageable);

    Role findByRoleUUID(String roleUUID);

}
