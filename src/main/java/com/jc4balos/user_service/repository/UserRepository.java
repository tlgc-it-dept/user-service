package com.jc4balos.user_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jc4balos.user_service.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(nativeQuery = true, value = """
            SELECT * FROM users
            WHERE is_active = 1
            AND (
                first_name LIKE CONCAT('%', :searchParam, '%') OR
                father_surname LIKE CONCAT('%', :searchParam, '%') OR
                mother_surname LIKE CONCAT('%', :searchParam, '%') OR
                husband_surname LIKE CONCAT('%', :searchParam, '%')
            )
            """)
    Page<User> findBySearchParam(@Param("searchParam") String searchParam, Pageable pageable);

    User findByUsername(String username);

    User findByEmail(String email);

    User findByUserUUID(String userUUID);
}
