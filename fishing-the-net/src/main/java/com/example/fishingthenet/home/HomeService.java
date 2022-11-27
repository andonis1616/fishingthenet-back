package com.example.fishingthenet.home;

import com.example.fishingthenet.email_data.EmailData;
import com.example.fishingthenet.email_data.EmailRepository;
import com.example.fishingthenet.user.User;
import com.example.fishingthenet.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HomeService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserRepository userRepository;


    public HomeInfoDto getByInfoUsername(String username) {
        HomeInfoDto info = new HomeInfoDto();

        Optional<User> user = userRepository.findByUsername(username);
        int fishingEmails = emailRepository.findAllByOwner(user).stream().filter(EmailData::getIsFishing).collect(Collectors.toList()).size();
        int allEmails = emailRepository.findAllByOwner(user).size();

        info.setPercentage(fishingEmails/ allEmails * 100);
        info.setMostfrequentCountry("Russia");
        info.setNumberOfFishingEmails(fishingEmails);

        return info;
    }
}
