package com.example.fishingthenet.email_data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<EmailData, Long> {

    List<EmailData> findAllByOwner(Long id);
}
