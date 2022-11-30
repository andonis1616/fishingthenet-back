package com.example.fishingthenet.email_data;


import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class EmailDataDtoTimestamp extends EmailDataDto{

    LocalDateTime dateTime;
}
