package com.vlinvestment.accountservice.service.impl;

import com.vlinvestment.accountservice.entity.AccessCode;
import com.vlinvestment.accountservice.repository.AccessCodeRepository;
import com.vlinvestment.accountservice.service.AccessCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AccessCodeServiceImpl implements AccessCodeService {

    private final static int EXPIRATION_TIME = 1;
    private final AccessCodeRepository accessCodeRepository;

    @Override
    public AccessCode create(AccessCode accessCode) {
        return accessCodeRepository.save(accessCode);
    }

    @Scheduled(fixedRate = 30000)
    public void deleteByExpirationTime() {
        accessCodeRepository.deleteByCreatedTimeBefore(LocalDateTime.now().minusMinutes(EXPIRATION_TIME));
    }

    @Override
    public boolean existBySource(String source) {
        return accessCodeRepository.existsBySource(source);
    }

    @Override
    public String findCodeBySource(String source) {
        return accessCodeRepository.findCodeBySource(source);
    }

}
