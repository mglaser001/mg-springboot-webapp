package com.springboot.app.service;

import com.springboot.app.mail.EmailBuilder;
import com.springboot.app.mail.EmailSender;
import com.springboot.app.model.RegistrationRequest;
import com.springboot.app.model.User;
import com.springboot.app.token.ConfirmationToken;
import com.springboot.app.validators.EmailValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final EmailSender emailSender;
    private final EmailBuilder emailBuilder;
    private final ConfimationTokenService confirmationTokenService;
    public String register(RegistrationRequest request) {
        Boolean isEmailValid = emailValidator.test(request.getEmail());

        if(!isEmailValid){
            throw new IllegalArgumentException("Email Not Valid");
        }
        String token = userService.signUpUser(new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                User.AppUserRole.USER
        ));

        String link = String.format("%s/api/v1/registration/confirm?token=", "http://localhost:8080") + token;
        emailSender.send(request.getEmail(), emailBuilder.buildConfirmationEmail(request.getFirstName() + " " +request.getLastName(), link));

        return token;
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);
        if(confirmationToken == null){
            throw new IllegalStateException("Token not found");
        }
        if(confirmationToken.getConfirmTime() != null){
            throw new IllegalStateException("Token already confirmed");
        }
        if(confirmationToken.getExpiresTime().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token already Expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(confirmationToken.getUser().getId());

        return "confirmed";
    }
}
