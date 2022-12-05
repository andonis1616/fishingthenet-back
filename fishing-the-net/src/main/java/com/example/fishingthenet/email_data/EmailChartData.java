package com.example.fishingthenet.email_data;

import com.example.fishingthenet.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailChartData {

    List<ChartData> chartData = new ArrayList<>();
    private TimeSlot timeFrame;
}