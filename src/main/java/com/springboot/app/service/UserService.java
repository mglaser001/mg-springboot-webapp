package com.springboot.app.service;

import com.springboot.app.model.User;
import com.springboot.app.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.isPresent() ? user.get() : null;
    }

    public void createNewUser(User user) {
        Optional<User> userByEmail = userRepository.findUserByEmail(user.getEmail());

        if(userByEmail.isPresent()){
            throw new IllegalArgumentException("Email Address already in use.");
        }
        userRepository.save(user);
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

        if(user.getActive() != null){
            userToUpdate.setActive(user.getActive());
        }
        if(user.getName() != null){
            userToUpdate.setName(user.getName());
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
}
