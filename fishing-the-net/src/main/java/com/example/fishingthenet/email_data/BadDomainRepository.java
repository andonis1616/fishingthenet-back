package com.example.fishingthenet.email_data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadDomainRepository extends JpaRepository<BadDomain, Long> {
    Boolean existsByDomain(String domain);
}
