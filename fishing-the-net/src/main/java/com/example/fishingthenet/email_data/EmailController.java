package com.example.fishingthenet.email_data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PreAuthorize("hasAnyAuthority()")
    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailData> createEmail(@RequestBody EmailDataDto dto) {
        log.info("Trying to save emails");
        return ResponseEntity.ok(emailService.saveEmail(dto));
    }

    @PreAuthorize("hasAnyAuthority()")
    @GetMapping(path = "/username/{username}")
    public ResponseEntity<List<EmailData>> getEmailsByUSer(@PathVariable String username) {
        return ResponseEntity.ok(emailService.findAllByOwner(username));
    }



}
