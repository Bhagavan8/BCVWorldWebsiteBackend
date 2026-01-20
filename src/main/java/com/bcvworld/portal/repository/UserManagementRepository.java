package com.bcvworld.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.bcvworld.portal.model.User;

@Repository
public interface UserManagementRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

}
