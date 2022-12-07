package com.example.fishingthenet.email_data;

import com.example.fishingthenet.user.User;
import com.example.fishingthenet.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmailService {

    private final EmailRepository repository;
    private final BadDomainRepository badDomainRepository;

    @Autowired
    private final EmailSenderService emailSenderService;
    private final UserRepository userRepository;
    public EmailService(EmailRepository repository, BadDomainRepository badDomainRepository, EmailSenderService emailSenderService, UserRepository userRepository) {
        this.repository = repository;
        this.badDomainRepository = badDomainRepository;
        this.emailSenderService = emailSenderService;
        this.userRepository = userRepository;
    }

    List<EmailData> findAllByOwner(String username){

        User owner = userRepository.findByUsername(username).orElseThrow();
       List<EmailData> list = repository.findAllByOwner(Optional.of(owner));
       return list;
    }

    public EmailData saveEmailWithTimestamp(EmailDataDto dto, String timestamp) throws IOException, URISyntaxException {

        String domain = dto.getSender().split("@")[1];

        boolean isBadDomain = checkBadDomain(domain);

        var analyzed = analyzedEmail(dto);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        EmailData emailData = new EmailData();
        emailData.setContent(dto.getContent());
        emailData.setSender(dto.getSender());
        emailData.setSubject(dto.getSubject());
        emailData.setIsFishing(true);
        emailData.setDateReceived(LocalDateTime.parse(timestamp, formatter));
        if(isBadDomain){
            emailData.setPercentage(100);
        }
        else {
            String ip =  executeNSLookUp(domain);
            if (badDomainRepository.existsByDomain(ip))
            {
                emailData.setPercentage(100);
            }
            else{
                if (analyzed != null) {
                    int fromAI = Math.round(analyzed.getProcentage() * 100);
                    if (analyzed.getIsPhishing().equals("Phishing")){
                        emailData.setIsFishing(true);
                    }
                    else {
                        emailData.setIsFishing(false);
                    }
                    emailData.setPercentage(fromAI);
                }
                else{
                    emailData.setPercentage(0);
                }
            }
        }

        emailData.setOwner(userRepository.findByUsername(dto.getOwnerUsername()).orElseThrow());
        repository.save(emailData);

        return emailData;
    }

    private String executeNSLookUp(String domain) {
        InetAddress thisComputer;
        byte[] address;

        // get the bytes of the IP address
        try {
            thisComputer = InetAddress.getByName(domain);
            address = thisComputer.getAddress();
        } catch (UnknownHostException ue) {
            System.out.println("Cannot find host " + domain);
            return "Could not be found";
        }

        if (isHostname(domain)) {
            // Print the IP address
            for (int i = 0; i < address.length; i++) {
                int unsignedByte = address[i] < 0 ? address[i] + 256
                        : address[i];
                System.out.print(unsignedByte + ".");
            }
            System.out.println();
        } else { // this is an IP address
            try {
                System.out.println(InetAddress.getByName(domain));
            } catch (UnknownHostException e) {
                System.out.println("Could not lookup the address " + domain);
            }
        }
        return "Adress was not found";
    }


    private static boolean isHostname(String s) {

        char[] ca = s.toCharArray();
        // if we see a character that is neither a digit nor a period
        // then s is probably a hostname
        for (int i = 0; i < ca.length; i++) {
            if (!Character.isDigit(ca[i])) {
                if (ca[i] != '.') {
                    return true;
                }
            }
        }

        // Everything was either a digit or a period
        // so s looks like an IP address in dotted quad format
        return false;

    } // end isHostName

    EmailData saveEmail(EmailDataDto dto) throws IOException, URISyntaxException {

        String domain = dto.getSender().split("@")[1];

        //Float obj = null;
        AnalyzedEmailDto analyzed = analyzedEmail(dto);

        boolean isBadDomain = checkBadDomain(domain);

        EmailData emailData = new EmailData();
        emailData.setContent(dto.getContent());
        emailData.setSender(dto.getSender());
        emailData.setSubject(dto.getSubject());
        emailData.setIsFishing(true);
        emailData.setDateReceived(LocalDateTime.now());

        if(isBadDomain){
            emailData.setPercentage(100);
        }
        else {
            String ip =  executeNSLookUp(domain);
            if (badDomainRepository.existsByDomain(ip))
            {;
                emailData.setPercentage(100);
            }
            else{
                if (analyzed != null) {
                    int fromAI = Math.round(analyzed.getProcentage() * 100);
                    if (analyzed.getIsPhishing().equals("Phishing")){
                        emailData.setIsFishing(true);
                    }
                    else {
                        emailData.setIsFishing(false);
                    }
                    emailData.setPercentage(fromAI);
                }
                else{
                    emailData.setPercentage(0);
                }
            }
        }
        emailData.setOwner(userRepository.findByUsername(dto.getOwnerUsername()).orElseThrow());
        repository.save(emailData);

        return emailData;
    }

    private AnalyzedEmailDto analyzedEmail(EmailDataDto dto) throws URISyntaxException {

        RestTemplate restTemplate = new RestTemplate();
        URI uri = new URI("http://localhost:8000/body");

        ResponseEntity<AnalyzedEmailDto> response = restTemplate.postForEntity(uri, dto, AnalyzedEmailDto.class);

        return response.getBody();
    }

    private boolean checkBadDomain(String domain) {
        boolean exists = badDomainRepository.existsByDomain(domain);
        return exists;
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
                    LocalDateTime start = LocalDateTime.now().minusWeeks(1).minusHours(LocalDateTime.now().getHour());
                    LocalDateTime end = LocalDateTime.now();
                    Predicate<LocalDateTime> isBetween = x -> (x.isAfter(start)) && (x.isBefore(end));

                    var emails = list.stream().map(EmailData::getDateReceived).filter(isBetween).toList();

                    List<ChartData> dataList = new ArrayList<>();
                    for (int i =0; i<=7; i++){
                        int dayOfMonth = start.getDayOfMonth();
                        int finalI = i;
                        log.info("Day of month: " + dayOfMonth + " increment: " + finalI + "Equal to "  );
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

        chartData.getChartData().sort(Comparator.comparing(ChartData::getTimestamp));
        return chartData;

    }


    public String importData() throws FileNotFoundException {

        File file1 = new File("src/main/resources/ALL-phishing-domains.txt");
        Scanner reader = new Scanner(file1);
        while (reader.hasNextLine()){
            String domain = reader.nextLine();
            BadDomain badDomain = new BadDomain();
            badDomain.setDomain(domain);
            badDomainRepository.save(badDomain);
        }
        File file2 = new File("src/main/resources/ALL-phishing-links.txt");

        reader = new Scanner(file2);
        while (reader.hasNextLine()){
            String domain = reader.nextLine();
            BadDomain badDomain = new BadDomain();
            badDomain.setDomain(domain);
            badDomainRepository.save(badDomain);
        }
        return "Succesfully imported data";
    }

    public String fishUser(String username) {

        emailSenderService.sendEmail(username);
        return "Email was sent successfully";
    }
}
