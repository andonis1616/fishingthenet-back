package com.example.fishingthenet.email_data;

import com.example.fishingthenet.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<EmailData, Long> {

    List<EmailData> findAllByOwner(Optional<User> owner);
    List<EmailData> findAllByOwnerAndIsFishingTrue(Optional<User> owner);

    Boolean existsByOwnerAndSubject(User owner, String Subject);

    List<EmailData> findByOwnerAndSubject(User owner, String Subject);
}
