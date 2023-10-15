package com.springboot.app.service;

import com.springboot.app.repo.ConfirmationTokenRepository;
import com.springboot.app.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfimationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    public ConfirmationToken getToken(String token){
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);

        return confirmationToken.isPresent() ? confirmationToken.get() : null;
    }

    public void setConfirmedAt(String token) {
        ConfirmationToken confirmationToken =confirmationTokenRepository.findByToken(token).get();
        confirmationToken.setConfirmTime(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
    }
}
