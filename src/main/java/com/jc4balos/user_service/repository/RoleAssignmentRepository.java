package com.jc4balos.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jc4balos.user_service.model.RoleAssignment;

public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {

}
