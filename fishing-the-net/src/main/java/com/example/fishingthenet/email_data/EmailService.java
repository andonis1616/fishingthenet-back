package com.example.fishingthenet.email_data;

import com.example.fishingthenet.user.User;
import com.example.fishingthenet.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        emailData.setOwner(userRepository.findByUsername(dto.getOwnerUsername()).orElseThrow());
        repository.save(emailData);

        return emailData;
    }

}
