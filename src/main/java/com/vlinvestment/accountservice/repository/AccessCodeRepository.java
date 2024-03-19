package com.vlinvestment.accountservice.repository;

import com.vlinvestment.accountservice.entity.AccessCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AccessCodeRepository extends JpaRepository<AccessCode, Long> {

    @Modifying
    void deleteByCreatedTimeBefore(LocalDateTime expirationTime);

    boolean existsBySource(String source);

    @Query("select ac.code from AccessCode ac where ac.source = :source")
    String findCodeBySource(String source);
}
