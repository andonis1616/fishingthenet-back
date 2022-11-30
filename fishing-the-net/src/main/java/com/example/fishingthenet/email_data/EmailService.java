package com.example.fishingthenet.email_data;

import com.example.fishingthenet.user.User;
import com.example.fishingthenet.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmailService {

    private final EmailRepository repository;

    private final UserRepository userRepository;
    public EmailService(EmailRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    List<EmailData> findAllByOwner(String username){

        User owner = userRepository.findByUsername(username).orElseThrow();
       List<EmailData> list = repository.findAllByOwner(Optional.of(owner));
       return list;
    }

    EmailData saveEmail(EmailDataDto dto){

        EmailData emailData = new EmailData();
        emailData.setContent(dto.getContent());
        emailData.setSender(dto.getSender());
        emailData.setSubject(dto.getSubject());
        emailData.setIsFishing(true);
        emailData.setDateReceived(LocalDateTime.now());

        emailData.setOwner(userRepository.findByUsername(dto.getOwnerUsername()).orElseThrow());
        repository.save(emailData);

        return emailData;
    }

    public List<EmailDataDto> findAllFishing(String username) {

        Optional<User> user = userRepository.findByUsername(username);
        List<EmailData> list = repository.findAllByOwnerAndIsFishingTrue(user);

        List<EmailDataDto> mappedList = list.stream().map(email -> {
            EmailDataDto dto = new EmailDataDto();
            dto.setSubject(email.getSubject());
            dto.setSender(email.getSender());
            dto.setOwnerUsername(email.getOwner().getUsername());
            dto.setContent(email.getContent());

            return dto;
        })
                .collect(Collectors.toList());
        return mappedList;
    }

    public EmailChartData getGraphData(String username, TimeSlot timeframe) {

        Optional<User> user = userRepository.findByUsername(username);
        List<EmailData> list = repository.findAllByOwnerAndIsFishingTrue(user);

        var chartData = new EmailChartData();

        switch (timeframe){
            case LAST_DAY -> {
                LocalDateTime start = LocalDateTime.now().minusHours(23);
                LocalDateTime end = LocalDateTime.now();

                Predicate<LocalDateTime> isBetween = x -> (x.isAfter(start)) && (x.isBefore(end));
                var emails = list.stream().map(EmailData::getDateReceived).filter(isBetween).toList();;

                List<ChartData> dataList = new ArrayList<>();
                for (int i =0; i<= 24; i++){
                    int hourOfDay = start.getHour();
                    int finalI = i;
                    int numberOfEmails = (int) emails.stream().filter(x -> x.getHour() == (hourOfDay + finalI) % 24).count();
                    var dataPoint = new ChartData(numberOfEmails, start.plusHours(finalI).withMinute(0).withSecond(0).withNano(0));

                    dataList.add(dataPoint);
                }

                chartData.setChartData(dataList);
                chartData.setTimeFrame(TimeSlot.LAST_DAY);
            }
            case LAST_WEEK -> {
                    LocalDateTime start = LocalDateTime.now().minusWeeks(1);
                    LocalDateTime end = LocalDateTime.now();
                    Predicate<LocalDateTime> isBetween = x -> (x.isAfter(start)) && (x.isBefore(end));

                    var emails = list.stream().map(EmailData::getDateReceived).filter(isBetween).toList();

                    List<ChartData> dataList = new ArrayList<>();
                    for (int i =0; i<=7; i++){
                        int dayOfMonth = start.getDayOfMonth();
                        int finalI = i;
                        int numberOfEmails = (int) emails.stream().filter(x -> x.getDayOfMonth() == (dayOfMonth + finalI)).count();
                        var dataPoint = new ChartData(numberOfEmails, start.plusDays(finalI).withHour(0).withMinute(0).withSecond(0).withNano(0));

                        dataList.add(dataPoint);
                    }
                    chartData.setChartData(dataList);
                    chartData.setTimeFrame(TimeSlot.LAST_WEEK);
            }

            case LAST_MONTH -> {
                LocalDateTime start = LocalDateTime.now().minusMonths(1);
                LocalDateTime end = LocalDateTime.now();

                Predicate<LocalDateTime> isBetween = x -> (x.isAfter(start)) && (x.isBefore(end));
                var emails = list.stream().map(EmailData::getDateReceived).filter(isBetween).toList();

                List<ChartData> dataList = new ArrayList<>();
                for (int i =0; i<= 31; i++){
                    int dayOfMonth = start.getDayOfMonth();
                    int finalI = i;
                    int numberOfEmails = (int) emails.stream().filter(x -> x.getDayOfMonth() == (dayOfMonth + finalI) % 31).count();
                    var dataPoint = new ChartData(numberOfEmails, start.plusDays(finalI).withHour(0).withMinute(0).withSecond(0).withNano(0));

                    dataList.add(dataPoint);
                }

                chartData.setChartData(dataList);
                chartData.setTimeFrame(TimeSlot.LAST_MONTH);
            }

            case LAST_YEAR -> {
                LocalDateTime start = LocalDateTime.now().minusYears(1).plusMonths(1);
                LocalDateTime end = LocalDateTime.now();

                Predicate<LocalDateTime> isBetween = x -> (x.isAfter(start)) && (x.isBefore(end));
                var emails = list.stream().map(EmailData::getDateReceived).filter(isBetween).toList();

                log.info("Number of yearly emails: " + emails.size());
                List<ChartData> dataList = new ArrayList<>();
                for (int i =0; i<= 11; i++){
                    int Month = start.getMonthValue();
                    log.info("Get Month Value: " + Month);
                    int finalI = i;
                    int numberOfEmails = (int) emails.stream().filter(x -> x.getMonthValue() == (Month + finalI) % 12).count();
                    var dataPoint = new ChartData(numberOfEmails, start.plusMonths(finalI ).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0));

                    dataList.add(dataPoint);
                }

                chartData.setChartData(dataList);
                chartData.setTimeFrame(TimeSlot.LAST_YEAR);
            }
        }

        chartData.getChartData().sort(Comparator.comparing(ChartData::timestamp));
        return chartData;

    }
}
