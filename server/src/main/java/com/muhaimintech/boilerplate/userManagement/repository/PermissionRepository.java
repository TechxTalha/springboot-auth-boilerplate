package com.muhaimintech.boilerplate.userManagement.repository;

import com.muhaimintech.boilerplate.userManagement.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Integer> {
}
