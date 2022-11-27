package com.example.fishingthenet.utils;

import com.example.fishingthenet.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

    public static User getUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        final Object principal= authentication.getPrincipal();

        if (!(principal instanceof User)) {
            throw new UnauthorizedException("Invalid user type");
        }

        return (User) principal;
    }
}
