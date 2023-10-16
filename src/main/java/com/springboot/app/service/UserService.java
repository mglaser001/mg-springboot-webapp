package com.springboot.app.service;

import com.springboot.app.model.User;
import com.springboot.app.repo.UserRepository;
import com.springboot.app.token.ConfirmationToken;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfimationTokenService confimationTokenService;

    private final static String USER_NOT_FOUND = "User with email %s Not Found";
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.isPresent() ? user.get() : null;
    }
    public User getUserByEmail(String email){
        User user = userRepository.findUserByEmail(email).orElseThrow( () ->new UsernameNotFoundException(String.format(USER_NOT_FOUND, email)));
        return user;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username).orElseThrow( () ->new UsernameNotFoundException(String.format(USER_NOT_FOUND, username)));
        return user;
    }
    public void createNewUser(User user) throws UsernameNotFoundException {
        User newUser = userRepository.findUserByEmail(user.getEmail()).orElseThrow( () ->new UsernameNotFoundException(""));
        userRepository.save(newUser);
    }

    public void deleteUser(Long id) {
        if(userRepository.existsById(id)){
            userRepository.deleteById(id);
        }else{
            throw new IllegalArgumentException("Student does not Exist with Id: "  + id);
        }

    }
    @Transactional
    public void updateUser(Long id, User user) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() ->new IllegalArgumentException("Student does not Exist with Id: "  + id));

        if(user == null) throw new IllegalArgumentException("Empty Request Body");

        if(user.getEnabled() != null){
            userToUpdate.setEnabled(user.getEnabled());
        }
        if(user.getFirstName() != null){
            userToUpdate.setFirstName(user.getFirstName());
        }
        if(user.getEmail() != null && !user.getEmail().isEmpty()){
            Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
            if(userOptional.isPresent()){
                throw new IllegalArgumentException("Email Address already in use.");
            }else{
                userToUpdate.setEmail(user.getEmail());
            }
        }
    }


    public String signUpUser(User user){
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        if(existingUser.isPresent()){
            if(existingUser.get().hasSameAttributes(user)){
                //TODO: Expire previous tokens
                expirePreviousTokens(existingUser.get());
                return generateAndSaveToken(existingUser.get());
            }
            throw new IllegalStateException("Email Taken && Confirmed");
        }

        userRepository.save(user);
        return generateAndSaveToken(user);
    }

    public void enableUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()){
            throw new IllegalStateException("No User Found");
        }
        User updateUser = user.get();
        updateUser.setEnabled(true);

        userRepository.save(updateUser);
    }

    private void expirePreviousTokens(User user) {

        confimationTokenService.expireTokensByUserId(user);

    }
    private String generateAndSaveToken(User user){
        String uid = UUID.randomUUID().toString();

        ConfirmationToken token = new ConfirmationToken(
                uid,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15L),
                user);

        confimationTokenService.saveConfirmationToken(token);
        return uid;
    }
}
