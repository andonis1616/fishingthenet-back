package com.example.fishingthenet.email_data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.security.Principal;
import java.time.LocalDateTime;
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
    @PostMapping(path = "/create/timestamp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailData> createEmailWithTimeStamp(@RequestBody EmailDataDto dto, @Param(value = "timestamp") String timestamp) {
        log.info("Trying to save emails");
        return ResponseEntity.ok(emailService.saveEmailWithTimestamp(dto, timestamp));
    }

    @PreAuthorize("hasAnyAuthority()")
    @GetMapping(path = "/username/{username}")
    public ResponseEntity<List<EmailData>> getEmailsByUSer(@PathVariable String username) {
        return ResponseEntity.ok(emailService.findAllByOwner(username));
    }


    @PreAuthorize("hasAnyAuthority()")
    @GetMapping(path = "/fishing/{username}")
    public ResponseEntity<List<EmailDataDto>> getFishingEmails(@PathVariable String username) {
        return ResponseEntity.ok(emailService.findAllFishing(username));
    }

    @PreAuthorize("hasAnyAuthority()")
    @GetMapping(path = "/graph")
    public ResponseEntity<EmailChartData> getGraphData(@Param(value = "username") String username,@Param(value = "timeframe") TimeSlot timeframe) {
        return ResponseEntity.ok(emailService.getGraphData(username, timeframe));
    }

    @PreAuthorize("hasAnyAuthority()")
    @GetMapping(path = "/import")
    public ResponseEntity<String> importData() throws FileNotFoundException {
        return ResponseEntity.ok(emailService.importData());
    }

    @PreAuthorize("hasAnyAuthority()")
    @GetMapping(path = "/fish/{username}")
    public ResponseEntity<String> fishUSer(@PathVariable String username) throws FileNotFoundException {
        return ResponseEntity.ok(emailService.fishUser(username));
    }
}
