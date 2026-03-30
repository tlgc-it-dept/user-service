package com.jc4balos.user_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jc4balos.user_service.model.RoleAssignment;
import com.jc4balos.user_service.model.User;

public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {

    List<RoleAssignment> findByUser(User user);
}
