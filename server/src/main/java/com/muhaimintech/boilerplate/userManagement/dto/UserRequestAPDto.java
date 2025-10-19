package com.muhaimintech.boilerplate.userManagement.dto;

import java.util.Set;

public class UserRequestAPDto {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String name;
    private String userType;
    private Set<Integer> accessIds;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Set<Integer> getAccessIds() {
        return accessIds;
    }

    public void setAccessIds(Set<Integer> accessIds) {
        this.accessIds = accessIds;
    }
}
