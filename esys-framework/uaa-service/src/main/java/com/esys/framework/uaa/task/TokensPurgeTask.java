package com.esys.framework.uaa.task;

import com.esys.framework.uaa.repository.IPasswordResetTokenRepository;
import com.esys.framework.uaa.repository.IVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class TokensPurgeTask {

    @Autowired
    IVerificationTokenRepository tokenRepository;

    @Autowired
    IPasswordResetTokenRepository passwordTokenRepository;

    //Her gün saat 05:00 kontol ediliyor
    @Scheduled(cron = "0 0 5 * * ?")
    public void purgeExpired() {

        Date now = Date.from(Instant.now());

        passwordTokenRepository.deleteAllExpiredSince(now);
        tokenRepository.deleteAllExpiredSince(now);
    }
}
