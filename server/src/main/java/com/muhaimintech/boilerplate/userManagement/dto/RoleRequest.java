package com.muhaimintech.boilerplate.userManagement.dto;

import java.util.Set;

public class RoleRequest {
    private String name;
    private Set<Integer> permissionIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Integer> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(Set<Integer> permissionIds) {
        this.permissionIds = permissionIds;
    }
}