package com.example.fishingthenet.email_data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ChartData {

    Integer numberOfEmails;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime timestamp;

}
