package com.example.fishingthenet.home;

import com.example.fishingthenet.config.CustomAuthorizationFilter;
import com.example.fishingthenet.email_data.EmailData;
import com.example.fishingthenet.email_data.EmailDataDto;
import com.example.fishingthenet.user.*;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Slf4j
public class HomeController {


    @Autowired
    private HomeService homeService;


    @GetMapping("/all")
    public ResponseEntity<String> getHomeInfo(){
        return ResponseEntity.ok("Test");
    }

    @PreAuthorize("hasAnyAuthority()")
    @GetMapping(path = "/{username}")
    public ResponseEntity<HomeInfoDto> getUsernameHomeInfo(@PathVariable String username) {
        return ResponseEntity.ok(homeService.getByInfoUsername(username));
    }
}