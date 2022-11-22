package com.example.fishingthenet.user;

import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;

import java.util.List;

public interface UserService {
    User saveUser(User user) throws AlreadyUsedException;
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
}
