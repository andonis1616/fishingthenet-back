package com.example.fishingthenet.home;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class HomeInfoDto {

    private float percentage;
    private String mostfrequentCountry;
    private int numberOfFishingEmails;
}
