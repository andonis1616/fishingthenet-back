package com.example.fishingthenet.email_data;

import com.example.fishingthenet.user.User;
import com.example.fishingthenet.utils.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.List;

@Service
@Slf4j
public class EmailService {

    private final EmailRepository repository;

    public EmailService(EmailRepository repository) {
        this.repository = repository;
    }

    List<EmailData> findAllByOwner(){
        Long ownerId = getUser().getId();
       List<EmailData> list = repository.findAllByOwner(ownerId);
       return list;
    }

    EmailData saveEmail(EmailDataDto dto){

        EmailData emailData = new EmailData();
        emailData.setContent(dto.getContent());
        emailData.setSender(dto.getSender());
        emailData.setSubject(dto.getSubject());
        emailData.setOwner(getUser());
        repository.save(emailData);

        return emailData;
    }


    public static User getUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UnauthorizedException("User is not authenticated");
        }

        final Object principal= authentication.getPrincipal();

        if (!(principal instanceof User)) {
            throw new UnauthorizedException("Invalid user type");
        }

        return (User) principal;
    }

}
