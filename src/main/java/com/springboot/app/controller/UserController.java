package com.springboot.app.controller;

import com.springboot.app.model.User;
import com.springboot.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/all")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping(path = "{userId}")
    public User getUserById(@PathVariable("userId") Long id){
        return userService.getUserById(id);
    }

    @PostMapping
    public void createUser(@RequestBody User user){
        userService.createNewUser(user);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser( @PathVariable("userId") Long id){
        userService.deleteUser(id);
    }

    @PutMapping(path = "{userId}")
    public void updateUser( @PathVariable("userId") Long id, @RequestBody User user){
        userService.updateUser(id, user);
    }

}
