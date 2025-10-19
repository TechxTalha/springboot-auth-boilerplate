package com.muhaimintech.boilerplate.userManagement.repository;

import com.muhaimintech.boilerplate.userManagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
}
