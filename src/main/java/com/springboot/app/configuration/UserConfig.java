package com.springboot.app.configuration;

import com.springboot.app.model.User;
import com.springboot.app.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository repository){
        return args -> {User first =new User( "First",  "te@st", true, LocalDateTime.now());
        repository.saveAll(List.of(first));
        };
    }
}
