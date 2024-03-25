package com.vlinvestment.accountservice.repository;

import com.vlinvestment.accountservice.entity.AccessCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccessCodeRepository extends JpaRepository<AccessCode, Long> {

    @Modifying
    @Query("delete from AccessCode ac where ac.expirationTime < :time")
    void deleteByExpirationTimeBefore(LocalDateTime time);

    boolean existsBySource(String source);

    @Query("select ac.code from AccessCode ac where ac.source = :source")
    Optional<String> findCodeBySource(String source);

    void deleteBySource(String source);

}
