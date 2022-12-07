package com.example.fishingthenet.email_data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzedEmailDto {
    private String isPhishing;
    private Float procentage;
}
